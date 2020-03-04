package photocol.client;

import photocol.client.util.RequestConnection;
import photocol.client.util.RequestConnectionManager;

import java.util.ArrayList;
import java.util.List;
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
        System.out.print("Username: ");
        String username = in.nextLine();
        System.out.print("Password (shown to user): ");
        String password = in.nextLine();

        // TODO: note this does not prevent JSON injection
        String json = String.format("{\"username\":\"%s\",\"passwordHash\":\"%s\"}",
                username, password);
        rcm.request("/login", "POST", json).printRequestDetails();
    }

    public static void logoutHandler(Scanner in, RequestConnectionManager rcm) {
        rcm.request("/logout").printRequestDetails();
    }

    public static void userphotosHandler(Scanner in, RequestConnectionManager rcm) {
        rcm.request("/userphotos").printRequestDetails();
    }

    public static void usercollectionsHandler(Scanner in, RequestConnectionManager rcm) {
        rcm.request("/usercollections").printRequestDetails();
    }

    public static void collectionphotosHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Enter collection owner user: ");
        String collectionOwner = in.nextLine();
        System.out.print("Enter collection uri: ");
        String collectionUri = in.nextLine();

        rcm.request(String.format("/collection/%s/%s", collectionOwner, collectionUri), "GET").printRequestDetails();
    }

    public static void collectionupdateHandler(Scanner in, RequestConnection rcm) {
        System.out.print("Enter collection owner user: ");
        String collectionOwner = in.nextLine();
        System.out.print("Enter collection uri: ");
        String collectionUri = in.nextLine();

        // TODO: working here
    }

    public static void imageHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Enter image uri: ");
        String imageUri = in.nextLine();
        System.out.print("Download path: ");
        String downloadPath = in.nextLine();

        RequestConnection rc = rcm.request("/image/" + imageUri);
        System.out.println(rc.request());
        rc.saveResponseTo(downloadPath);
    }

    public static void imageuploadHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Enter image path: ");
        String imagePath = in.nextLine();
        String[] fileComponents = imagePath.split("/");
        String filename = fileComponents[fileComponents.length-1];
        String[] fileExtensionParts = imagePath.split("\\.");
        String extension = fileExtensionParts[fileExtensionParts.length-1];

        List<String[]> headers = new ArrayList<>();
        headers.add(new String[]{"Content-Type", "image/" + extension});
        rcm.request("/image/" + filename + "/upload", "PUT", imagePath, headers).printRequestDetails();
    }

    public static void createcollectionHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Enter collection name: ");
        String collectionName = in.nextLine();

        String data = String.format("{\"name\":\"%s\"}", collectionName);
        rcm.request("/collection/new", "POST", data).printRequestDetails();
    }

    public static void updatecollectionHandler(Scanner in, RequestConnectionManager rcm) {
        // TODO: working here
        System.out.print("Enter collection owner user: ");
        String collectionOwner = in.nextLine();
        System.out.print("Enter collection uri: ");
        String collectionUri = in.nextLine();

        System.out.print("Enter new collection name: ");
        String newCollectionName = in.nextLine();
        System.out.print("Enter users to add to the collection: ");
        String person;
        String personString = "";
        while(!(person=in.nextLine()).equals(""))
            personString += "{\"username\":\"" + person + "\",\"role\":\"ROLE_VIEWER\"},";
        personString = personString.substring(0, personString.length()-1);

        String data = String.format("{\"name\":\"%s\",\"aclList\":[%s]}", newCollectionName, personString);
        String url = String.format("/collection/%s/%s/update", collectionOwner, collectionUri);
        rcm.request(url, "POST", data).printRequestDetails();
    }

    public static void addimageHandler(Scanner in, RequestConnectionManager rcm) {
        System.out.print("Enter collection owner user: ");
        String collectionOwner = in.nextLine();
        System.out.print("Enter collection uri: ");
        String collectionUri = in.nextLine();
        System.out.print("Enter image uri: ");
        String imageUri = in.nextLine();

        String data = String.format("{\"uri\":\"%s\"}", imageUri);
        rcm.request("/collection/" + collectionOwner + "/" + collectionUri + "/addphoto", "POST", data).printRequestDetails();
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
                "userphotos\tshow user's photos\n" +
                "usercollections\tshow user's collections\n" +
                "collectionphotos\tshow photos in a given collection\n" +
                "image\tGET/download an image\n" +
                "imageupload\timage an upload\n" +
                "createcollection\tcreate a collection\n" +
                "updatecollection\tupdate a collection\n" +
                "addimage\tadd image to collection\n" +
                "signup\tsign up\n" +
                "login\tlog in\n" +
                "logout\tlog out\n" +
                "help\tview this menu\n" +
                "quit\texit\n");
    }
}
