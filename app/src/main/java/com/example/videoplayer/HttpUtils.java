package com.example.videoplayer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public static String getJsonContent(String path){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            byte[]buf = new byte[1024];
            int hasRead = 0;
            while ((hasRead = is.read(buf))!=-1){
                baos.write(buf,0,hasRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toString();
    }
}
