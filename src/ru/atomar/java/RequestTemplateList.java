package ru.atomar.java;

import java.util.ArrayList;

/**
 * Created by RusAr on 16.02.2017.
 */
public class RequestTemplateList extends ArrayList<RequestTemplate> {

    /*public void loadFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            File fsFile = new File(fileName);
            if (!fsFile.exists())
                return;

            reader = new BufferedReader(new FileReader(fsFile));

            reader.

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
                else if (ind == 3)
                    contentType = line;
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
    }*/

}
