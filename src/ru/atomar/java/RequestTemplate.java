package ru.atomar.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RusAr on 31.01.2017.
 */
public class RequestTemplate {

    public static final String EXTENTION = ".rqt";

    public String name;
    public String host;
    public String file;
    public String params;

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

    public RequestTemplate(String name, String host, String file, String params) {
        this.name = name;
        this.host = host;
        this.file = file;
        this.params = params;
    }

    private void load() {
        BufferedReader reader = null;
        try {
            File fsFile = new File(name + ((name.endsWith(EXTENTION))?"":EXTENTION));
            if (!fsFile.exists()) {
                name = "default";
                host = "localhost";
                file = "//";
                params = "";
                return;
            }

            reader = new BufferedReader(new FileReader(fsFile));

            String line;

            int ind = 0;

            params = "";
            while ((line = reader.readLine()) != null) {
                if (ind == 0)
                    host = line;
                else if (ind == 1)
                    file = line;
                else
                    params = params.concat(line);

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
            File fsFile = new File(name + ((name.endsWith(EXTENTION))?"":EXTENTION));

            writer = new BufferedWriter(new FileWriter(fsFile));
            writer.write(host);
            writer.write('\n');
            writer.write(file);
            writer.write('\n');
            writer.write(params);
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

}
