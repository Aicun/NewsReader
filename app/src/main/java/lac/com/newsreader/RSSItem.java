package lac.com.newsreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aicun on 6/16/2017.
 */

public class RSSItem {
    private String title;
    private String description;
    private String link;
    private String pubDate;

    private SimpleDateFormat dateOutFormat = new SimpleDateFormat("EEEE h:mm a (MMM d)");
    private SimpleDateFormat dateInFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        /*try {
            Date date = dateInFormat.parse(pubDate.trim());
            String pubDateFormatted = dateOutFormat.format(date);
            return pubDateFormatted;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }*/
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
