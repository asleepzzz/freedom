package application.com.a30togo.freedom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jeromehuang on 8/16/16.
 */
public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        String title = getIntent().getStringExtra("title");
        //= getIntent().getParcelableExtra("image");

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        int index = Integer.valueOf(title.replace("Image#",""));
        for (int  i =0; i <showSdcardAlbum.imageItems.size();i++) {
            if (showSdcardAlbum.imageItems.get(i).getTitle().equals(title)) {
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageBitmap(showSdcardAlbum.imageItems.get(i).getImage());
            }
        }




    }
}