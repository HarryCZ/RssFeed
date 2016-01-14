package cz.czu.edu.voboril.rssfeed.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ckkci on 14.01.2016.
 */
public class GetBitmapAsyncTask extends AsyncTask<String,Void,Bitmap> {

    private ImageView imageView;

    //TODO: miesto constructoru v ktorom posuvame ImageView sa tiez dal spravit interface ktory by vratil Bitmapu a ShowRSSItemActivity by pak implementovalo ten interface
    public GetBitmapAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        Bitmap bmp = null;
        try {
            url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode()==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                bmp = BitmapFactory.decodeStream(in);
            }
            //TODO: exceptions by sa mali handlovat
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageView!=null && bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
