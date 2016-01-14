package cz.czu.edu.voboril.rssfeed.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cz.czu.edu.voboril.rssfeed.Constants;
import cz.czu.edu.voboril.rssfeed.R;
import cz.czu.edu.voboril.rssfeed.models.ItemRSS;
import cz.czu.edu.voboril.rssfeed.tasks.GetBitmapAsyncTask;

public class ShowRSSItemActivity extends AppCompatActivity {
    TextView title, description;
    ImageView imageView;
    Button link;
    Bitmap imgVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rssitem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle extras = getIntent().getExtras();
        ItemRSS item = null;

        if (extras!=null){
            item = (ItemRSS) extras.getSerializable(Constants.CURRENT_RSS_ITEM_KEY);
        }

        title = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        description = (TextView) findViewById(R.id.textView2);
        link = (Button) findViewById(R.id.button);

        if (item != null) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());

            GetBitmapAsyncTask bitmapAsyncTask = new GetBitmapAsyncTask(imageView);
            bitmapAsyncTask.execute(item.getImageUrl());

            final ItemRSS finalItem = item;
            link.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(finalItem.getLink()));
                    startActivity(intent2);
                }
            });
        }
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}

