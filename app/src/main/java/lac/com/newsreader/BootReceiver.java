package lac.com.newsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Aicun on 6/21/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("News Reader", "Boot completed");
        Intent service = new Intent(context,NewsReaderService.class);
        context.startService(service);
    }
}
