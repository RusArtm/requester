package ru.atomar.java;

import com.sun.glass.ui.Application;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by RusAr on 31.01.2017.
 */
public class AsyncRequestSender extends Thread {

    public interface RequestListener {
        void onNewLine(String line);
    }

    RequestListener listener;
    boolean secure;
    final int requestId;
    String host;
    String auth;
    String file;
    String contentType;
    String payload;

    AsyncRequestSender(int requestId, boolean secure, String host, String auth, String file, String contentType, String payload, RequestListener listener) {
        super();
        this.listener = listener;

        this.requestId = requestId;
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
                SSLSocketFactory factory =
                        (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket =
                        (SSLSocket) factory.createSocket(host, 443);
            } else {
                sendLine("Opening socket");
                socket = new Socket(host, 80);
            }
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = "POST " + file + " HTTP/1.0 \n" +
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

        } catch (IOException e) {
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
                listener.onNewLine("[" + String.valueOf(requestId) + "]" + line + "\n");
            }
        });
    }

}
