package ru.atomar.java;

import com.sun.glass.ui.Application;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * Created by RusAr on 31.01.2017.
 */
public class AsyncRequestSender extends Thread {

    public interface RequestListener {
        void onNewLine(String line);
    }

    RequestListener listener;
    boolean secure;
    String host;
    String auth;
    String file;
    String contentType;
    String payload;
    String methodName;

    AsyncRequestSender(String methodName, boolean secure, String host, String auth, String file, String contentType, String payload, RequestListener listener) {
        super();
        this.listener = listener;

        this.methodName = methodName;
        this.secure = secure;
        this.host = host;
        this.auth = auth;
        this.file = file;
        this.contentType = contentType;
        this.payload = payload;
    }

    @Override
    public void run() {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        String line;

        try {
            if (secure) {
                sendLine("Opening secure socket");

                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }};
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                SSLSocketFactory factory = sc.getSocketFactory();

//                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

                socket = factory.createSocket(host, 443);
            } else {
                sendLine("Opening socket");
                socket = new Socket(host, 80);
            }
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = methodName + " " + file + " HTTP/1.0 \n" +
                    "Host: " + host + "\n" +
                    (auth.length() == 0 ? "" : "Authorization: " + auth + "\n") +
                    "Content-Type: " + contentType + "\n" +
                    "Content-Length: " + String.valueOf(payload.length()) + "\n" +
                    "\n" +
                    payload;

            sendLine("======================= REQUEST =======================");
            sendLine(request);

            out.write(request);
            out.flush();

            sendLine("======================= RESPONSE =======================");
            while ((line = in.readLine()) != null) {
                sendLine(line);
            }

            out.close();
            in.close();
            socket.close();

        } catch (Exception e) {
            sendLine("======================= ERROR =======================");
            sendLine(e.toString());
        }
        sendLine("======================= DONE =======================");
        sendLine("");
    }

    private void sendLine(final String line) {
        if (listener == null)
            return;
        Application.invokeLater(new Runnable() {
            @Override
            public void run() {
                listener.onNewLine(line + "\n");
            }
        });
    }

}
