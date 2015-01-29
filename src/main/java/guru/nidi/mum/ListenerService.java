package guru.nidi.mum;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 *
 */
public class ListenerService extends Service {
    private ListenerReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();

        receiver = new ListenerReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public static void start(final Context context) {
        context.startService(new Intent(context, ListenerService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
