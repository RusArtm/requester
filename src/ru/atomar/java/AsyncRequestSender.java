package ru.atomar.java;

import com.sun.glass.ui.Application;

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
    String host;
    String file;
    String params;

    AsyncRequestSender(String host, String file, String params, RequestListener listener) {
        super();
        this.listener = listener;

        this.host = host;
        this.file = file;
        this.params = params;
    }

    @Override
    public void run() {
        Socket socket;
        PrintWriter out;
        BufferedReader in;

        String line;

        try {
            socket = new Socket(host, 80);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String request = "POST " + file + " HTTP/1.0 \n" +
                    "Host: " + host + "\n" +
                    "Content-Type: application/x-www-form-urlencoded\n" +
                    "Content-Length: " + String.valueOf(params.length()) + "\n" +
                    "\n" +
                    params;

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

            sendLine("======================= DONE =======================");

        } catch (IOException e) {
            sendLine("======================= ERROR =======================");
            sendLine(e.toString());
        }
        sendLine("======================= ==== =======================");
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
