package guru.nidi.mum.infrastructure;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import guru.nidi.android.ApplicationContextHolder;
import guru.nidi.mum.R;
import guru.nidi.mum.limit.LimitPersister;
import guru.nidi.mum.main.MainActivity;

import java.util.List;

import static guru.nidi.mum.DateUtils.addDays;
import static guru.nidi.mum.DateUtils.todayMidnight;

/**
 *
 */
public class Notifier extends BroadcastReceiver {
    private static final int ID = 0;

    private long minutes;
    private int switchOns;
    private Boolean over;
    private String switchOnRatio, minuteRatio;

    @Override
    public void onReceive(Context context, Intent intent) {
        calcValues();
        calcRatios();
        drawNotification(context);
    }

    public static void start(Context context) {
        context.sendBroadcast(new Intent(Notifier.class.getName() + ".START"));
    }

    private void calcValues() {
        final List<Event> events = new EventPersister().getEvents(todayMidnight(), addDays(todayMidnight(), 1));
        for (Event event : events) {
            final long start = event.getFrom() == 0 ? todayMidnight().getTime() : event.getFrom();
            final long end = event.getTo() == 0 ? System.currentTimeMillis() : event.getTo();
            minutes += (end - start);

            if (event.getFrom() != 0) {
                switchOns++;
            }
        }
        minutes /= 60 * 1000;
    }

    private void calcRatios() {
        final LimitPersister limitPersister = new LimitPersister();

        over = null;
        switchOnRatio = "";
        minuteRatio = "";

        if (limitPersister.isMinutesPerDayEnabled()) {
            final int minuteLimit = limitPersister.getMinutesPerDay();
            if (minutes > minuteLimit * .8) {
                minuteRatio = "Hours: " + limitPersister.asHours((int) minutes) + "/" + limitPersister.asHours((int) minuteLimit);
                over = minutes > minuteLimit || (over != null && over);
            }
        }

        if (limitPersister.isSwitchOnPerDayEnabled()) {
            final int switchOnLimit = limitPersister.getSwitchOnPerDay();
            if (switchOns > switchOnLimit * .8) {
                switchOnRatio = "Turn on: " + switchOns + "/" + switchOnLimit;
                over = switchOns > switchOnLimit || (over != null && over);
            }
        }
    }

    private void drawNotification(Context context) {
        NotificationManager notificationManager = ApplicationContextHolder.service(Context.NOTIFICATION_SERVICE);

        if (over == null) {
            notificationManager.cancel(ID);
        } else {
            final Notification.Builder builder = new Notification.Builder(context)
                    .setContentTitle(over ? "Over the usage limit" : "Near the usage limit")
                    .setContentText(switchOnRatio + " " + minuteRatio)
                    .setSmallIcon(over ? R.drawable.ic_action_error : R.drawable.ic_action_warning)
                    .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));
            notificationManager.notify(ID, builder.getNotification());
        }
    }

}
