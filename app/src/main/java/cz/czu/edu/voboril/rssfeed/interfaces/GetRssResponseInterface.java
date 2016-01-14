package cz.czu.edu.voboril.rssfeed.interfaces;

import java.io.File;

/**
 * Created by ckkci on 14.01.2016.
 */
//TODO: interface na posunutie rss suboru spat do activity
public interface GetRssResponseInterface {
    void deliverResult(File xmlFile);
}
