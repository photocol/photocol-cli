package photocol.client.util;

import java.io.*;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestConnectionManager {

    private String baseUrl;
    public RequestConnectionManager(String baseUrl) {
        this.baseUrl = baseUrl;

        // necessary for setting the "Origin" header
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }

    // for sending test requests to the webserver; helpful guide to managing HTTP requests from Java: (w/ cookies):
    // https://www.baeldung.com/java-http-request; this imitates a browser session by maintaining cookies
    private CookieManager cookieManager = new CookieManager();

    public RequestConnection request(String uri, String method, String data, List<String[]> headers) {
        try {
            String url = baseUrl + uri;
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            RequestConnection rc = new RequestConnection(con, data);

            // set up request
            con.setRequestMethod(method);

            // write cookies and write all headers
            String requestCookies = cookieManager.getCookieStore().getCookies()
                    .stream()
                    .map(cookie -> cookie.toString())
                    .collect(Collectors.joining(";"));
            headers.add(new String[]{"Cookie", requestCookies});

            // need this header to work with new CORS restriction
            headers.add(new String[]{"Origin", "http://localhost"});

            for (String[] header : headers) {
                System.out.println(header[0] + " " + header[1]);
                con.setRequestProperty(header[0], header[1]);
            }
            rc.setRequestHeaders(con.getRequestProperties());

            // handle input POST vs. PUT
            if (method.equals("POST")) {
                con.setRequestProperty("Content-Type", "application/json");
                if (data.length() > 0) {
                    con.setDoOutput(true);
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
                    out.write(data);
                    out.close();
                }
            } else if (method.equals("PUT")) {
                con.setDoOutput(true);
                InputStream in = new BufferedInputStream(new FileInputStream(data));
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                byte[] buffer = new byte[4096];
                int n;
                while ((n = in.read(buffer)) > -1)
                    out.write(buffer, 0, n);
                in.close();
                out.close();
            }

            // send request and get status
            con.getResponseCode();

            // parse cookies
            String cookieString;
            if ((cookieString = con.getHeaderField("Set-Cookie")) != null)
                HttpCookie.parse(cookieString).forEach(cookie -> cookieManager.getCookieStore().add(null, cookie));

            return rc;
        } catch(Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public RequestConnection request(String uri, String method, String data) {
        return request(uri, method, data, new ArrayList<>());
    }

    public RequestConnection request(String uri, String method) {
        return request(uri, method, "");
    }

    public RequestConnection request(String uri) {
        return request(uri, "GET");
    }

}
