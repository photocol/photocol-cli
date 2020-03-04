package photocol.client;

import photocol.client.util.RequestConnectionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PhotocolClient {

    private static Map<String, Handlers.SimpleFunctionalInterface> handlerMap = new HashMap<>();
    public static void main(String[] args) {
        RequestConnectionManager rcm = new RequestConnectionManager("http://localhost:4567");
        Scanner scanner = new Scanner(System.in);

        handlerMap.put("quit",  Handlers::quitHandler);
        handlerMap.put("help",  Handlers::helpHandler);
        handlerMap.put("signup", Handlers::signupHandler);
        handlerMap.put("login", Handlers::loginHandler);
        handlerMap.put("logout", Handlers::logoutHandler);
        handlerMap.put("userphotos", Handlers::userphotosHandler);
        handlerMap.put("usercollections", Handlers::usercollectionsHandler);
        handlerMap.put("collectionphotos", Handlers::collectionphotosHandler);
        handlerMap.put("image", Handlers::imageHandler);
        handlerMap.put("imageupload", Handlers::imageuploadHandler);
        handlerMap.put("createcollection", Handlers::createcollectionHandler);
        handlerMap.put("updatecollection", Handlers::updatecollectionHandler);
        handlerMap.put("addimage", Handlers::addimageHandler);

        while (true) {
            System.out.print("Choose an endpoint: ");
            handlerMap.getOrDefault(scanner.nextLine(), Handlers::helpHandler).run(scanner, rcm);
        }
    }
}
