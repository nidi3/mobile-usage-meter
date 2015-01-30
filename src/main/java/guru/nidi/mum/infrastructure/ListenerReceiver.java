package guru.nidi.mum.infrastructure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 */
public class ListenerReceiver extends BroadcastReceiver {
    private final EventPersister eventPersister = new EventPersister();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
            eventPersister.addEvent(true);
            Notifier.start(context);
        } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
            eventPersister.addEvent(false);
        }
    }
}
