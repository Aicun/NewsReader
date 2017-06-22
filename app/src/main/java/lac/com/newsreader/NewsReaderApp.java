package lac.com.newsreader;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aicun on 6/21/2017.
 */

public class NewsReaderApp extends Application {

    private long feedMillis = -1;

    private Intent service;

    public long getFeedMillis() {
        return feedMillis;
    }

    public void setFeedMillis(long feedMillis) {
        this.feedMillis = feedMillis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("News Reader", "Application started");

        service = new Intent(this, NewsReaderService.class);
        startService(service);
    }

    public void stopService(){
        if(service!=null)
            stopService(service);
    }
}
