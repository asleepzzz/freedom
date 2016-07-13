package application.com.a30togo.freedom;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by jeromehuang on 7/13/16.
 */
public class instagramDownloader {
    public static void download (String url) {
        String parsinguRL = "https://www.instagram.com/p/BHxVvyWDBpb";

        HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
        HttpGet httpget = new HttpGet(parsinguRL); // Set the action you want to do
        HttpResponse response = null; // Executeit
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();
        InputStream is = null; // Create an InputStream with the response
        try {
            is = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "utf8"), 8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {// Read line by line
                sb.append(line + "\n");
                if (line.contains("og:image")) {
                    String picUrl = line.substring(line.indexOf("content=")+9,line.indexOf("/>")-2);
                    Log.e("kevin"," "+line);
                    Log.e("kevin","url: "+picUrl);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String resString = sb.toString(); // Result is here

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
