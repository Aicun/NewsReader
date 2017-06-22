package lac.com.newsreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aicun on 6/17/2017.
 */

public class RSSFeed {
    private String title;
    private String pubDate;
    private ArrayList<RSSItem> items;

    private SimpleDateFormat dateInFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public final static String NEW_FEED = "lac.com.newsreader.NEW_FEED";

    public RSSFeed() {
        items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPubDateMillis() {
        try {
            Date date = dateInFormat.parse(pubDate.trim());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public int addItem(RSSItem item) {
        items.add(item);
        return items.size();
    }

    public RSSItem getItem(int index) {
        return  items.get(index);
    }

    public ArrayList<RSSItem> getAllItems() {
        return items;
    }
}
