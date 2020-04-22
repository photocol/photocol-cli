package photocol.client.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class RequestConnection {

    private HttpURLConnection conn;
    private Map<String, List<String>> headers;
    private String data;
    public RequestConnection(HttpURLConnection conn, String data) {
        this.conn = conn;
        this.data = data;
    }

    public void setRequestHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public int responseCode() {
        try {
            return conn.getResponseCode();
        } catch(IOException err) {
            err.printStackTrace();
        }
        return -1;
    }

    public String request() {
        String headerString = "========\nREQUEST\n========\n" + conn.getRequestMethod() + " " + conn.getURL() +
                (data.length()>0 ? "data: " + data + "\n" : "") + "\nHEADERS:\n";
        for(String header : headers.keySet()) {
            headerString += header + ": ";
            for(String value : headers.get(header))
                headerString += value + " ";
            headerString += "\n";
        }
        return headerString;
    }

    public String response() {
        String responseMessage = "========\nRESPONSE:\n========\n";
        try {
            responseMessage += responseCode() + " " + conn.getResponseMessage() + "\n";
        } catch(IOException err) {
            err.printStackTrace();
        }
//        if(responseCode() != 200)
//            return responseMessage;

        try(BufferedReader in = new BufferedReader(new InputStreamReader(responseCode() < 400
                ? conn.getInputStream() : conn.getErrorStream()))) {
            String line;
            while((line=in.readLine()) != null) {
                responseMessage += line + "\n";
            }
        } catch(IOException err) {
            err.printStackTrace();
        }
        return responseMessage + "========\n";
    }

    public void saveResponseTo(String downloadPath) {
        try(BufferedInputStream in = new BufferedInputStream(responseCode() < 400 ?
                conn.getInputStream() : conn.getErrorStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(downloadPath))) {
            in.transferTo(out);
        } catch(IOException err) {
            err.printStackTrace();
        }
    }

    public void printRequestDetails() {
        System.out.println(request());
        System.out.println(response());
    }
}
