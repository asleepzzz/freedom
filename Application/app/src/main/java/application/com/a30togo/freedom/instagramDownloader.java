package application.com.a30togo.freedom;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
    private static int downloaded_cnt ;
    static String count_key = "count";
    static String val = "val";


    public static int getPreviousInstallCount() {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/IGDownloader";
        File dir = new File(file_path);
        if(dir.exists()) {
            File[] listFile;
            String[] FilePathStrings;
            String[] FileNameStrings;
            int maxIndex = -1;

            listFile = dir.listFiles();
            if (listFile.length > 0) {
                FilePathStrings = new String[listFile.length];
                FileNameStrings = new String[listFile.length];
                for (int i = 0; i < listFile.length; i++) {
                    FilePathStrings[i] = listFile[i].getAbsolutePath();
                    FileNameStrings[i] = listFile[i].getName();

                    int index = Integer.parseInt(FileNameStrings[i].replace(".png",""));
                    if (index > maxIndex) {
                        maxIndex = index;
                    }
                }
                return maxIndex+1;
            }
        }
        return -1;
    }

    public static void setCnt(Context ctx,int count) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(count_key, Context.MODE_PRIVATE).edit();
        editor.putInt(val,count).commit();
        downloaded_cnt = count;
    }

//    public static int getCnt(Context ctx) {
//        SharedPreferences prefs = ctx.getSharedPreferences(count_key, Context.MODE_PRIVATE);
//        int tmp = prefs.getInt(val,0);
//        downloaded_cnt = tmp;
//        return tmp;
//    }

    public static synchronized void download (String url, Context ctx) {
        Toast.makeText(ctx,"downloading",Toast.LENGTH_SHORT).show();

        String parsinguRL = url;//"https://www.instagram.com/p/BFsr8QwwZEV/";

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


                    Bitmap imageBitmap = null;
                    URL imageURL = new URL(picUrl);
                    imageBitmap = getBitmapFromURL(picUrl);


                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/IGDownloader";
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
                    notifyMeidaStore(file, ctx);
                    Toast.makeText(ctx,"download complete",Toast.LENGTH_SHORT).show();
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


    private static void notifyMeidaStore(File photoFile, Context context) {
        if(context != null) {
            try {
                ContentValues intent = new ContentValues(2);
                intent.put("mime_type", "image/png");
                intent.put("_data", photoFile.getAbsolutePath());
                intent.put("datetaken", Long.valueOf(photoFile.lastModified()));
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, intent);
            } catch (Exception var3) {
                ;
            }

            Intent intent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent1.setData(Uri.fromFile(photoFile));
            context.sendBroadcast(intent1);
        }
    }


    public static boolean isIGUrl (String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        if (url.contains("https://www.instagram.com")) {
            return true;
        }
        return false;
    }
}
