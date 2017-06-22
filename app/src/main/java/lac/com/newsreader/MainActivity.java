package lac.com.newsreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private NewsReaderApp app;
    private long feedPubDateMillis;

    private RSSFeed feed;
    private FileIO fileIO;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileIO = new FileIO(getApplicationContext());

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

        itemsListView.setOnItemClickListener(this);

        app = (NewsReaderApp) getApplication();
        fileIO = new FileIO(getApplicationContext());

        new DownloadFeed().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        feedPubDateMillis = app.getFeedMillis();

        if (feedPubDateMillis == -1) {
            new DownloadFeed().execute();
        } else if (feed == null) {
            new ReadFeed().execute();
        } else {
            updateDisplay();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new DownloadFeed().execute();
                Toast.makeText(this,"Feed refreshed",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_stopService:
                app.stopService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RSSItem item = feed.getItem(position);

        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("pubdate", item.getPubDate());
        intent.putExtra("title", item.getTitle());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("link", item.getLink());
        startActivity(intent);
    }

    private void updateDisplay() {
        if (feed == null) {
            titleTextView.setText("Unable to get RSS feed");
            return;
        }
        //set text for title
        titleTextView.setText(feed.getTitle());

        List<RSSItem> items = feed.getAllItems();
        List<Map<String, String>> data = new ArrayList<>();
        for (RSSItem item : items) {
            Map<String, String> map = new HashMap<>();
            map.put("date", item.getPubDate());
            map.put("title", item.getTitle());
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_item, new String[]{"date", "title"}, new int[]{R.id.pubDateTextView, R.id.titleTextView});
        itemsListView.setAdapter(adapter);
    }

    private class DownloadFeed extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            fileIO.downloadFile();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new ReadFeed().execute();
        }
    }

    private class ReadFeed extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            feed = fileIO.readFile();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("News Reader", "Feed read");
            app.setFeedMillis(feed.getPubDateMillis());
            MainActivity.this.updateDisplay();
        }
    }
}
