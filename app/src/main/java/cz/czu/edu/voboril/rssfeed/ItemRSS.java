package cz.czu.edu.voboril.rssfeed;

import android.graphics.Bitmap;

/**
 * Created by Jakub on 07.01.2016.
 */
public class ItemRSS {
    private String title = "Title";
    private String link = "Link";
    private String description = "Description";
    private String enclosure = null;
    private String pubDate = "Publish date";

    public ItemRSS(){
        super();
    }

    public void setTitle(String title) { this.title = title; }
    public void setLink(String link) { this.link = link; }
    public void setDescription(String description) { this.description = description; }
    public void setEnclosure(String enclosure) { this.enclosure = enclosure; }
    public void setPubDate(String pubDate) { this.pubDate = pubDate; }

    public String getTitle() { return this.title; }
    public String getLink() { return this.link; }
    public String getDescription() {
        return this.description;
    }
    public String getEnclosure() { return this.enclosure; }
    public String getPubDate() { return this.pubDate; }
}
