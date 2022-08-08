package com.cst2335.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapHelper{

    /**
     * to connect and load the picture online to show
     * @param s string
     * @param context context
     * @return bitmap picture
     */
    public static Bitmap getBitmap(String s, Context context){
        URL url = null;
        Bitmap bitmap = null;
        try{
            url = new URL(s);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
