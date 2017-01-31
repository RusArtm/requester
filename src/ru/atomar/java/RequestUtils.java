package ru.atomar.java;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

/**
 * Created by RusAr on 31.01.2017.
 */
public class RequestUtils {

    public static String sendHttp1Request(String host, String file, String params) {
        Socket socket;
        PrintWriter out;
        BufferedReader in;

        StringBuffer result = new StringBuffer();
        String line;

        try {
            socket = new Socket(host, 80);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = "POST " + file + " HTTP/1.0 \n" +
                    "Host: " + host + "\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: " + String.valueOf(params.length()) + "\n" +
                    "\n";

            result.append("======================= REQUEST =======================\n");
            result.append(request);

            out.write(request);
            out.flush();

            result.append("======================= RESPONSE =======================\n");
            while ((line = in.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }

            out.close();
            in.close();
            socket.close();

            return result.toString();

        } catch (IOException e) {
            return e.toString();
        }

    }

}
