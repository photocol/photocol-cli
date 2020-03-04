package photocol.client;

import photocol.client.util.RequestConnectionManager;

import java.util.Scanner;

public class Handlers {

    public static void signupHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Email: ");
        String email = in.nextLine();
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password (shown to user): ");
        String password = in.nextLine();

        // TODO: note this does not prevent JSON injection
        String json = String.format("{\"email\":\"%s\",\"username\":\"%s\",\"passwordHash\":\"%s\"}",
                email, username, password);
        rcm.request("/signup", "POST", json).printRequestDetails();
    }

    public static void loginHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Email: ");
        String email = in.nextLine();
        System.out.print("Password (shown to user): ");
        String password = in.nextLine();

        // TODO: note this does not prevent JSON injection
        String json = String.format("{\"email\":\"%s\",\"passwordHash\":\"%s\"}",
                email, password);
        rcm.request("/login", "POST", json).printRequestDetails();
    }

    public static void logoutHandler(Scanner in, RequestConnectionManager rcm) {
        rcm.request("/logout").printRequestDetails();
    }

    // useful for hashmap
    public interface SimpleFunctionalInterface {
        void run(Scanner in, RequestConnectionManager rcm);
    }

    // helper function for quitting program
    public static void quitHandler(Scanner in, RequestConnectionManager rcm) {
        System.exit(0);
    }

    // helper function to view help menu
    public static void helpHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.println("Commands:\n" +
                "signup\tsign up\n" +
                "login\tlog in\n" +
                "logout\tlog out\n" +
                "help\tview this menu\n" +
                "quit\texit\n");
    }

    // helper function to guide users to help
    public static void defaultHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.println("Command not found. Type \"help\" to view a list of available commands\n.");
    }
}
