package cz.czu.edu.voboril.rssfeed.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.czu.edu.voboril.rssfeed.models.ItemRSS;

/**
 * Created by ckkci on 14.01.2016.
 */
public class Utils {

    public static List<ItemRSS> xmlParseAndStore(File xmlFile) throws IOException, XmlPullParserException {
        int event;
        String text = null;
        List<ItemRSS> rssItems = new ArrayList<ItemRSS>();

        FileInputStream fis = new FileInputStream(xmlFile);
        XmlPullParserFactory xmlParserFactoryObj = XmlPullParserFactory.newInstance();
        XmlPullParser xmlParser = xmlParserFactoryObj.newPullParser();

        xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlParser.setInput(fis, null);
        event = xmlParser.getEventType();

        ItemRSS item = new ItemRSS();
        while (event != XmlPullParser.END_DOCUMENT){
            String name = xmlParser.getName();
            switch (event){
                case XmlPullParser.START_TAG:
                    if (name.equals("enclosure")) {
                        for (int i = 0; i < xmlParser.getAttributeCount(); i++) {
                            if (xmlParser.getAttributeName(i).equals("url")) {
                                String imageUrl = xmlParser.getAttributeValue(i);
                                item.setImageUrl(imageUrl);
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
        //parsingDone = false;
        //TODO: exceptions by sa mali handlovat

        return rssItems;
    }
}
