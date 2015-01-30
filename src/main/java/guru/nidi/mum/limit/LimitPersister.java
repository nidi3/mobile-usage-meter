package guru.nidi.mum.limit;

import android.content.SharedPreferences;
import guru.nidi.android.support.AbstractPersister;

/**
 *
 */
public class LimitPersister extends AbstractPersister {
    private static final String MPD_ENABLED = "mpdEnabled";
    private static final String MPD_VALUE = "mpdValue";
    private static final String SOPD_ENABLED = "sopdEnabled";
    private static final String SOPD_VALUE = "sopdValue";

    public LimitPersister() {
        super("limit");
    }

    public void save(final boolean minutesPerDayEnabled, final int minutesPerDayValue, final boolean switchOnPerDayEnabled, final int switchOnPerDayValue) {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                editor.putBoolean(MPD_ENABLED, minutesPerDayEnabled);
                editor.putInt(MPD_VALUE, minutesPerDayValue);
                editor.putBoolean(SOPD_ENABLED, switchOnPerDayEnabled);
                editor.putInt(SOPD_VALUE, switchOnPerDayValue);
            }
        });
    }

    public boolean isMinutesPerDayEnabled() {
        return pref.getBoolean(MPD_ENABLED, false);
    }

    public int getMinutesPerDay() {
        return pref.getInt(MPD_VALUE, 0);
    }

    public boolean isSwitchOnPerDayEnabled() {
        return pref.getBoolean(SOPD_ENABLED, false);
    }

    public int getSwitchOnPerDay() {
        return pref.getInt(SOPD_VALUE, 0);
    }

    public String asHours(int minutes) {
        return pad(minutes / 60) + ":" + pad(minutes % 60);
    }

    private String pad(int value) {
        return (value < 10 ? "0" : "") + value;
    }

}
