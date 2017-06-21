package lac.com.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView pubDateTextView = (TextView) findViewById(R.id.pubDateTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        TextView linkTextView = (TextView) findViewById(R.id.linkTextView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String pubDate = intent.getStringExtra("pubDate");
        String description = intent.getStringExtra("description");
        String link = intent.getStringExtra("link");

        titleTextView.setText(title);
        pubDateTextView.setText(pubDate);
        descriptionTextView.setText(description);
        linkTextView.setText(link);

        linkTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");
        Uri uri =  Uri.parse(link);

        Intent viewLink = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(viewLink);
    }
}
