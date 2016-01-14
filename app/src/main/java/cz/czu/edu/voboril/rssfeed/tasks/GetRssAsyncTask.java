package cz.czu.edu.voboril.rssfeed.tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.czu.edu.voboril.rssfeed.interfaces.GetRssResponseInterface;

/**
 * Created by ckkci on 14.01.2016.
 */
public class GetRssAsyncTask extends AsyncTask<String,Void,File> {
    private Context ctx;
    private Dialog progressDialog;
    public GetRssResponseInterface delegate;

    public GetRssAsyncTask(Context ctx) {
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog.show();
    }

    @Override
    protected File doInBackground(String... params) {

        File outputFile = new File(ctx.getCacheDir(),"rss.xml");
        if (outputFile.exists()){
            outputFile.delete();
        }
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode()==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int current = 0;
                while ((current = in.read()) != -1){
                    bos.write(current);
                }
                bos.close();
                fos.close();
            }
            //TODO: exceptions by sa mali handlovat
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile;
    }

    @Override
    protected void onPostExecute(File f) {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        delegate.deliverResult(f);
    }
}
