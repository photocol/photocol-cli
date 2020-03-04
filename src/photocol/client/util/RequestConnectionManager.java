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
    }

    // for sending test requests to the webserver; helpful guide to managing HTTP requests from Java: (w/ cookies):
    // https://www.baeldung.com/java-http-request; this imitates a browser session by maintaining cookies
    private CookieManager cookieManager = new CookieManager();

    public int request(String uri, String method, String data, List<String[]> headers) throws Exception {
        String url = baseUrl + uri;
        System.out.printf("Testing %s request to %s with data \"%s\".%n", url, method, data);
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method);

        String requestCookies = cookieManager.getCookieStore().getCookies()
                .stream()
                .map(cookie->cookie.toString())
                .collect(Collectors.joining(";"));
        con.setRequestProperty("Cookie", requestCookies);

        // write all headers
        for (String[] header : headers)
            con.setRequestProperty(header[0], header[1]);

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
        int status = con.getResponseCode();
        if (status != 200)
            return status;

        // parse cookies
        String cookieString;
        if ((cookieString = con.getHeaderField("Set-Cookie")) != null)
            HttpCookie.parse(cookieString).forEach(cookie -> cookieManager.getCookieStore().add(null, cookie));

        // read output
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = in.readLine()) != null)
            response.append(line);
        in.close();
        System.out.printf("Response: %s%n", response.toString());

        con.disconnect();
        return status;
    }

    public int request(String uri, String method) throws Exception {
        return request(uri, method, "", new ArrayList<>());
    }

    public int request(String uri, String method, String data) throws Exception {
        return request(uri, method, data, new ArrayList<>());
    }
}
