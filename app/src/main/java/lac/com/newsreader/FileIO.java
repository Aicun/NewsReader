package lac.com.newsreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Aicun on 6/17/2017.
 */

class FileIO {
    private final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
    private final String FILENAME = "news_feed2.xml";
    private Context context;

    FileIO(Context context) {
        this.context = context;
    }

    void downloadFile() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                URL url = new URL(URL_STRING);
                InputStream is = url.openStream();
                FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int bytesRead = is.read(buffer);
                while (bytesRead != -1) {
                    fos.write(buffer, 0, bytesRead);
                    bytesRead = is.read(buffer);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    RSSFeed readFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            // set content handler
            RSSFeedHandler rssFeedhandler = new RSSFeedHandler();
            reader.setContentHandler(rssFeedhandler);
            //read the file from internal storage
            FileInputStream fis = context.openFileInput(FILENAME);
            InputSource is = new InputSource(fis);
            reader.parse(is);
            return rssFeedhandler.getFeed();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
