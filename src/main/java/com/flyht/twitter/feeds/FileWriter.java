package com.flyht.twitter.feeds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileWriter {

    void writeTweetsToFile(String topic, String data) {
        String fileName = "./output/"+topic+".txt";
        File f = new File(fileName);
        try {
            PrintWriter out = null;
            if ( f.exists() && !f.isDirectory() ) {
                    out = new PrintWriter(new FileOutputStream(new File(fileName), true));
            }
            else {
                out = new PrintWriter(fileName);
            }
            out.append(data);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
