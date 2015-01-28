package guru.nidi.mum;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 */
public class ListenerReceiver extends BroadcastReceiver {
    private final Persister persister = new Persister();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            persister.addEvent(true);
        } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
            persister.addEvent(false);
        }
    }
}
