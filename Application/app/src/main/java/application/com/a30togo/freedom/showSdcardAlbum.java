package application.com.a30togo.freedom;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jeromehuang on 8/5/16.
 */
public class showSdcardAlbum {
//    private GridView gridView;
//    private GridViewAdapter gridAdapter;
    static  ArrayList<ImageItem> imageItems = new ArrayList<>();
    static  GridViewAdapter gridAdapter;
    static void show (Context ctx, GridView gridView) {
        imageItems.clear();
        if (gridAdapter!=null) {
            gridAdapter.clear();
        }
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/IGDownloader";

        final Context mCtx = ctx;

        File dir = new File(file_path);
        if(dir.exists()) {
            File[] listFile;
            String[] FilePathStrings;
            String[] FileNameStrings;

            listFile = dir.listFiles();

            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();

                File fileObj = new  File(FilePathStrings[i]);

                if(fileObj .exists()){
                    Bitmap bitMapObj= BitmapFactory.decodeFile(fileObj.getAbsolutePath());
                    imageItems.add(new ImageItem(bitMapObj, "Image#" + i));
                }
            }





            //GridViewAdapter gridAdapter;
            gridAdapter = new GridViewAdapter(mCtx, R.layout.grid_item_layout, imageItems);
            gridView.setAdapter(gridAdapter);



            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                                                    //Create intent
                                                    Intent intent = new Intent(mCtx, DetailsActivity.class);
                                                    intent.putExtra("title", item.getTitle());
                                                    //intent.putExtra("image", item.getImage());

                                                    //Start details activity
                                                    mCtx.startActivity(intent);
                                                }
                                            });
        } else {
            gridAdapter = new GridViewAdapter(mCtx, R.layout.grid_item_layout, imageItems);
            gridView.setAdapter(gridAdapter);
        }

    }
    static void refresh (Context ctx, GridView gridView) {
        imageItems.clear();
        if (gridAdapter!=null) {
            gridAdapter.clear();
        }
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/IGDownloader";

        final Context mCtx = ctx;

        File dir = new File(file_path);
        if(dir.exists()) {
            File[] listFile;
            String[] FilePathStrings;
            String[] FileNameStrings;

            listFile = dir.listFiles();

            FilePathStrings = new String[listFile.length];
            FileNameStrings = new String[listFile.length];
            for (int i = 0; i < listFile.length; i++) {
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                FileNameStrings[i] = listFile[i].getName();

                File fileObj = new File(FilePathStrings[i]);

                if (fileObj.exists()) {
                    Bitmap bitMapObj = BitmapFactory.decodeFile(fileObj.getAbsolutePath());
                    imageItems.add(new ImageItem(bitMapObj, "Image#" + i));
                }
            }

            gridAdapter.notifyDataSetChanged();
            //gridAdapter = new GridViewAdapter(mCtx, R.layout.grid_item_layout, imageItems);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                    //Create intent
                    Intent intent = new Intent(mCtx, DetailsActivity.class);
                    intent.putExtra("title", item.getTitle());
                    //intent.putExtra("image", item.getImage());

                    //Start details activity
                    mCtx.startActivity(intent);
                }
            });
        }
    }




}
