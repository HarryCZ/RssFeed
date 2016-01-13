package cz.czu.edu.voboril.rssfeed;

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

        final Bundle exteas = getIntent().getExtras();

        title = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        description = (TextView) findViewById(R.id.textView2);
        link = (Button) findViewById(R.id.button);

        if (exteas != null) {
            title.setText(exteas.getString("title"));
            description.setText(exteas.getString("description"));

            imgVal = BitmapFactory.decodeFile(exteas.getString("enclosure"));
            System.out.println("MYLOG > filePath > " + exteas.getString("enclosure"));
            imageView.setImageBitmap(imgVal);



            link.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(exteas.getString("link")));
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
