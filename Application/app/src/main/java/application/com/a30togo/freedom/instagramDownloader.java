package application.com.a30togo.freedom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeromehuang on 7/13/16.
 */
public class instagramDownloader {
    private static int downloaded_cnt;

    public static void download (String url) {
        String parsinguRL = "https://www.instagram.com/p/BFsr8QwwZEV/";

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
                    Bitmap imageBitmap = null;
                    URL imageURL = new URL(picUrl);
                    imageBitmap = getBitmapFromURL(picUrl);


                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/Kevin";
                    File dir = new File(file_path);
                    if(!dir.exists())
                        dir.mkdirs();
                    File file = new File(dir, downloaded_cnt+ ".png");
                    downloaded_cnt++;
                    FileOutputStream fOut = new FileOutputStream(file);

                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();
                    imageBitmap.recycle();

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

    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
