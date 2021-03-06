package ru.atomar.java;

import com.sun.glass.ui.Application;

import javax.net.ssl.*;
import java.io.BufferedReader;
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
    RequestParams mParams;

    AsyncRequestSender(RequestParams params, RequestListener listener) {
        super();
        this.listener = listener;
        this.mParams = params;
    }

    @Override
    public void run() {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        String line;

        try {
            if (mParams.secure) {
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

                socket = factory.createSocket(mParams.host, 443);
            } else {
                sendLine("Opening socket");
                socket = new Socket(mParams.host, 80);
            }
            out = new PrintWriter(socket.getOutputStream(), false);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request;
            if (mParams.request != null)
                request = mParams.request;
            else
                request = mParams.methodName + " " + mParams.file + " HTTP/1.0\n" +
                        "Host: " + mParams.host + "\n" +
                        (mParams.auth.length() == 0 ? "" : "Authorization: " + mParams.auth + "\n") +
                        "Content-Type: " + mParams.contentType + "\n" +
                        "Content-Length: " + String.valueOf(mParams.payload.length()) + "\n" +
                        "\n" +
                        mParams.payload;

            String[] requestLines = request.split("\\r?\\n", -1);

            sendLine("======================= REQUEST =======================");
            for (String requestLine : requestLines) {
                out.println(requestLine);
                sendLine(requestLine);
            }
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
