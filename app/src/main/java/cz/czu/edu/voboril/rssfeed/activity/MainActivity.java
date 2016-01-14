package cz.czu.edu.voboril.rssfeed.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.czu.edu.voboril.rssfeed.Constants;
import cz.czu.edu.voboril.rssfeed.R;
import cz.czu.edu.voboril.rssfeed.interfaces.GetRssResponseInterface;
import cz.czu.edu.voboril.rssfeed.models.ItemRSS;
import cz.czu.edu.voboril.rssfeed.tasks.GetRssAsyncTask;
import cz.czu.edu.voboril.rssfeed.utils.Utils;

public class MainActivity extends AppCompatActivity implements GetRssResponseInterface{
    ListView newsLayout;
    private String rssUrl="http://www.rozhlas.cz/zpravy/rss_aktualne";
    private List<ItemRSS> rssItems = new ArrayList<ItemRSS>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newsLayout = (ListView) findViewById(R.id.listView);

        GetRssAsyncTask getRssAsyncTask = new GetRssAsyncTask(MainActivity.this);
        getRssAsyncTask.delegate = this;
        getRssAsyncTask.execute(rssUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), ShowRSSItemActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void deliverResult(File xmlFile) {
        try {
            rssItems = Utils.xmlParseAndStore(xmlFile);
            handleResult();
            //TODO: exceptions by sa mali handlovat
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResult(){
        final int arrSize = rssItems.size();

        final ArrayList<String> itemTitles = new ArrayList<String>();
        for (int i = 0; i < arrSize; i++) {
            itemTitles.add(rssItems.get(i).getTitle());
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.content_list_view, itemTitles);
        newsLayout.setAdapter(adapter);

        newsLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                ItemRSS currentItem = (ItemRSS) rssItems.get(itemPosition);

                Intent intent = new Intent(getApplicationContext(), ShowRSSItemActivity.class);
                intent.putExtra(Constants.CURRENT_RSS_ITEM_KEY,currentItem);
                startActivity(intent);
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}

