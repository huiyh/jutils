package com.huiyh.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by huiyh on 2016/8/30.
 */

public class Command {

    public static String exec(String command) {
        BufferedReader reader = null;
        try {
            Process p = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.close(reader);
        }
    }

}
