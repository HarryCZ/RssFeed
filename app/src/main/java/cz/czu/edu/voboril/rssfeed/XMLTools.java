package cz.czu.edu.voboril.rssfeed;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 06.01.2016.
 */
public class XMLTools {
    Activity activity;

    private List<ItemRSS> rssItems = new ArrayList<ItemRSS>();
    private String urlStr = null;
    private XmlPullParserFactory xmlParserFactoryObj;
    public volatile boolean parsingDone = true;

    public XMLTools(Activity activity){

        super();
        this.activity = activity;
    }

    public void setURL(String url) {
        this.urlStr = url;
    }

    public List<ItemRSS> getRSSItems(){
        return rssItems;
    }

    public void xmlParseAndStore(XmlPullParser xmlParser){
        int event;
        String text = null;

        try {
            event = xmlParser.getEventType();

            ItemRSS item = new ItemRSS();
            while (event != XmlPullParser.END_DOCUMENT){
                String name = xmlParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        if (name.equals("enclosure")) {
                            for (int i = 0; i < xmlParser.getAttributeCount(); i++) {
                                if (xmlParser.getAttributeName(i).equals("url")) {
                                    String fileName = createImageFromBitmap(getImage(xmlParser.getAttributeValue(i)), "itemThumb"+rssItems.size());
                                    item.setEnclosure(fileName);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            item.setTitle(text);
                        } else if (name.equals("link")){
                            item.setLink(text);
                        } else if (name.equals("description")) {
                            item.setDescription(text);
                        } else if (name.equals("pubDate")) {
                            item.setPubDate(text);
                        } else if (name.equals("item")) {
                            rssItems.add(item);
                            item = new ItemRSS();
                        }
                        break;
                }

                event = xmlParser.next();
            }
            parsingDone = false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String createImageFromBitmap(Bitmap bitmap, String fileName) {
        try {
            //Application application = (Application) RssFeedApplication.getmContext();

            System.out.println("MYLOG > storage state >" + Environment.getExternalStorageState());
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/cz.czu.edu.voboril.rssfeed/images/");
            File readTest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/");
            System.out.println("MYLOG > sd read test > " + readTest.exists());
            if (!dir.exists()) {
                Boolean dirCreated = dir.createNewFile();
                System.out.println("MYLOG > dir created > " + dirCreated);
            }
            System.out.println("MYLOG > dir > " + dir.exists() + " / " + dir.isDirectory());
            fileName = fileName + ".png";
            File file = new File(dir, fileName);
            System.out.println("MYLOG > file > " + file.getAbsolutePath());
            fileName = file.getAbsolutePath();
            System.out.println("MYLOG > fOut");
            FileOutputStream fOut = new FileOutputStream(file);
            System.out.println("MYLOG > compress");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            System.out.println("MYLOG > flush");
            fOut.flush();
            System.out.println("MYLOG > close");
            fOut.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
            fileName = null;
        }
        System.out.println("MYLOG > fileName > " + fileName);
        return fileName;
    }

    private Bitmap getImage(final String url) {
        final Bitmap[] imgBitmap = {null};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL imgURL = new URL(url);
                    imgBitmap[0] = BitmapFactory.decodeStream(imgURL.openConnection().getInputStream());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return imgBitmap[0];
    }

    public void getXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    xmlParserFactoryObj = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlParser = xmlParserFactoryObj.newPullParser();

                    xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    xmlParser.setInput(stream, null);

                    xmlParseAndStore(xmlParser);
                    stream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
