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

        while (true) {
            System.out.print("Choose an endpoint: ");
            handlerMap.getOrDefault(scanner.nextLine(), Handlers::helpHandler).run(scanner, rcm);
        }
    }

    // TODO: implement these in Handlers.java
        // try this with your own image
//        List<String[]> headers = new ArrayList<>();
//        headers.add(new String[]{"Content-Type", "image/jpeg"});
//        RequestConnection[] reqs = new RequestConnection[] {
//            //try to create user that already exists
//            rcm.request("/signup","POST"," {\"email\":\"whatever@gmail.com\",\"passwordHash\":\"passowordpassword\"}"),
//            // create new user
//            rcm.request("/signup","POST"," {\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"passowordpassword\"}"),
//            //login with nonexisitng user
//            rcm.request("/login", "POST", "{\"email\":\"idontexist@gmail.com\",\"passwordHash\":\"password\"}"),
//            //login using wrong password
//            rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"notcorrectpassword\"}"),
//            //login using exisitng user and right password
//            rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"password\"}"),
//            //login in again when logged in already
//            rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"password\"}"),
//            //put photo in a collection that is not public
//            rcm.request("/collection/new", "POST", "{\"name\":\"some collection\",\"isPublic\":false}"),
//            //put photo in a collection that is public
//            rcm.request("/collection/new", "POST", "{\"name\":\"some collection\",\"isPublic\":true}"),
//            //list all the photos in the collection
//            rcm.request("/collection/collection1/photos", "GET"),
//            rcm.request("/collection/collection1/addphoto", "POST", "{\"uri\":\"418120749463199.jpg\"}"),
//            rcm.request("/collection/collection1/addphoto", "POST", "{\"uri\":\"thisimagedoesntexist.jpg\"}"),
//            rcm.request("/collection/collection1/photos", "GET"),
//            rcm.request("/userphotos", "GET"),
//            rcm.request("/usercollections", "GET"),
//            rcm.request("/image/test.png/upload", "PUT", "/home/jon/Downloads/cat2.jpg", headers)
//        };
//
//        for(RequestConnection req : reqs) {
//            System.out.println(req.request());
//            System.out.println(req.response());
//        }

        // this will print ugly stuff to terminal
//        request("/image/cat.jpg", "GET");
}
