package photocol.client;

import photocol.client.util.RequestConnectionManager;

public class PhotocolClient {

    public static void main(String[] args) throws Exception {
        String baseUrl = "http://localhost:4567";
        RequestConnectionManager rcm = new RequestConnectionManager("http://localhost:4567");

        rcm.request("/image/cat.jpg", "GET");
//        request("/login", "POST", "{\"email\":\"jlam@cooper.edu\"," +
//                "\"passwordHash\":\"*&^%E(&%^$#*&%*(%($#^%&%*(&(TR*&$*&TU*&$%&^$^#\"}");
        // create new user
        rcm.request("/signup","POST"," {\"email\":\"whatever@gmail.com\",\"passwordHash\":\"passowordpassword\"}");
        //try to create user that already exists
        rcm.request("/signup","POST"," {\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"passowordpassword\"}");
        //login with nonexisitng user
        rcm.request("/login", "POST", "{\"email\":\"idontexist@gmail.com\",\"passwordHash\":\"password\"}");
        //login using wrong password
        rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"notcorrectpassword\"}");
        //login using exisitng user and right password
        rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"password\"}");
        //login in again when logged in already
        rcm.request("/login", "POST", "{\"email\":\"victorzh716@gmail.com\",\"passwordHash\":\"password\"}");
        //put photo in a collection that is not public
        rcm.request("/collection/new", "POST", "{\"name\":\"some collection\",\"isPublic\":false}");
        //put photo in a collection that is public
        rcm.request("/collection/new", "POST", "{\"name\":\"some collection\",\"isPublic\":true}");
        //list all the photos in the collection
        rcm.request("/collection/collection1/photos", "GET");

        rcm.request("/collection/collection1/addphoto", "POST", "{\"uri\":\"418120749463199.jpg\"}");
        rcm.request("/collection/collection1/addphoto", "POST", "{\"uri\":\"thisimagedoesntexist.jpg\"}");

        rcm.request("/collection/collection1/photos", "GET");

        rcm.request("/userphotos", "GET");
        rcm.request("/usercollections", "GET");

        // this will print ugly stuff to terminal
//        request("/image/cat.jpg", "GET");

        // try this with your own image
//        List<String[]> headers = new ArrayList<>();
//        headers.add(new String[]{"Content-Type", "image/jpeg"});
//        request("/image/test.png/upload", "PUT", "/home/jon/Downloads/cat2.jpg", headers);

        rcm.request("/logout", "GET");
        rcm.request("/image/cat.jpg", "GET");
    }
}
