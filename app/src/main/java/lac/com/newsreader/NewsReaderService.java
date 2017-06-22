package lac.com.newsreader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Aicun on 6/21/2017.
 */

public class NewsReaderService extends Service {

    private NewsReaderApp app;
    private Timer timer;
    private FileIO fileIO;

    @Override
    public void onCreate() {
        Log.d("News Rader", "Service created");
        app = (NewsReaderApp) getApplication();
        fileIO = new FileIO(getApplicationContext());
        startTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("News Reader", "Service started");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("News Reader", "Service destroyed");
        stopTimer();
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d("News Rader", "Timer task started");

                fileIO.downloadFile();
                Log.d("News Rader", "File downloaded");

                RSSFeed feed = fileIO.readFile();
                Log.d("News Reader", "File read");

                //if there is new feed
                if (feed.getPubDateMillis() > app.getFeedMillis()) {
                    Log.d("News Reader", "New feed available");

                    app.setFeedMillis(feed.getPubDateMillis());
                    sendNotification("Select to view new feed");
                } else {
                    Log.d("News Reader", "No new feed");
                }
            }
        };
        timer = new Timer(true);
        int delay = 1000; // one hour
        int interval = 1000; // one hour
        timer.schedule(task, delay, interval);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendNotification(String text) {
        // create the intent for the notification
        Intent notificationIntent = new Intent(this, ItemActivity.class);

        //create pending intent
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, flag);

        //create the variables for the notification
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "Updated news feed is available";
        CharSequence contentTitle = getText(R.string.app_name);
        CharSequence contentText = text;

        //create the notification and set its data
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager mananger = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        final int NOTIFICATION_ID = 1;
        mananger.notify(NOTIFICATION_ID,notification);
    }
}
