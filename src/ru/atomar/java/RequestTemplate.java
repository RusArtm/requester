package ru.atomar.java;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by RusAr on 31.01.2017.
 */
public class RequestTemplate {

    public static final String EXTENTION = ".rqt";

    public String name;
    public String host;
    public String file;
    public String auth;
    public String payload;

    /**
     * Use this constructor to load request template from file name.rqt
     *
     * @param name - template name
     */
    public RequestTemplate(String name) {
        super();
        this.name = name;
        load();
    }

    public RequestTemplate(String name, String host, String auth, String file, String payload) {
        this.name = name;
        this.host = host;
        this.auth = auth;
        this.file = file;
        this.payload = payload;
    }

    private void load() {
        BufferedReader reader = null;
        try {
            File fsFile = new File(name + ((name.endsWith(EXTENTION)) ? "" : EXTENTION));
            if (!fsFile.exists()) {
                name = "default";
                auth = "";
                host = "localhost";
                file = "/";
                payload = "";
                return;
            }

            reader = new BufferedReader(new FileReader(fsFile));

            String line;

            int ind = 0;

            payload = "";
            while ((line = reader.readLine()) != null) {
                if (ind == 0)
                    host = line;
                else if (ind == 1)
                    auth = line;
                else if (ind == 2)
                    file = line;
                else
                    payload = payload.concat(line);

                ind++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Saves a template to file name.rqt
     */
    public void save() {
        BufferedWriter writer = null;
        try {
            File fsFile = new File(name + ((name.endsWith(EXTENTION)) ? "" : EXTENTION));

            writer = new BufferedWriter(new FileWriter(fsFile));
            writer.write(host);
            writer.write('\n');
            writer.write(auth);
            writer.write('\n');
            writer.write(file);
            writer.write('\n');
            writer.write(payload);
            writer.write('\n');
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    public static ArrayList<String> getTemplateList() {
        ArrayList<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder();

        File curDir = new File(".");
        File[] filesList = curDir.listFiles();
        for (File f : filesList) {
            if (!f.isDirectory() && f.isFile() && f.getName().endsWith(EXTENTION)) {
                list.add(f.getName());
            }
        }
        return list;

    }

    public void delete() {
        File fsFile = new File(name + ((name.endsWith(EXTENTION)) ? "" : EXTENTION));
        if (fsFile.exists())
            fsFile.delete();
    }
}
