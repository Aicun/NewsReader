package lac.com.newsreader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Aicun on 6/17/2017.
 */

class RSSFeedHandler extends DefaultHandler{
    private RSSFeed feed;
    private RSSItem item;

    private boolean feedTitleHasBeenRead = false;
    private boolean feedPubDatehasBeenRead = false;

    private boolean isTitle = false;
    private boolean isDescription = false;
    private boolean isLink = false;
    private boolean isPubDate = false;

    public RSSFeed getFeed() {
        return feed;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
        item = new RSSItem();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("item")) {
            item = new RSSItem();
            return;
        }else if(qName.equals("title")) {
            isTitle = true;
            return;
        }else if(qName.equals("description")) {
            isDescription = true;
            return;
        }else if(qName.equals("pubDate")) {
            isPubDate = true;
            return;
        }else if(qName.equals("link")) {
            isLink = true;
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("item"));
        feed.addItem(item);
        return;
    }

    @Override
    public void characters(char ch[], int start, int length) {
        String s = new String(ch,start,length);
        if(isTitle) {
            if(feedTitleHasBeenRead == false) {
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            }else {
                item.setTitle(s);
            }
            isTitle = false;
        }else if(isLink) {
            item.setLink(s);
            isLink = false;
        }else if (isDescription) {
            if(s.startsWith("<")) {
                item.setDescription("No description available");
            }else {
                item.setDescription(s);
            }
            isDescription = false;
        }else if(isPubDate) {
            if(feedPubDatehasBeenRead == false) {
                feed.setPubDate(s);
                feedPubDatehasBeenRead = true;
            }else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }
    }
}
