package guru.nidi.mum.limit;

import android.content.SharedPreferences;
import guru.nidi.android.support.AbstractPersister;

/**
 *
 */
class LimitPersister extends AbstractPersister {
    private static final String HPD_ENABLED = "hpdEnabled";
    private static final String HPD_VALUE = "hpdValue";
    private static final String SOPD_ENABLED = "sopdEnabled";
    private static final String SOPD_VALUE = "sopdValue";

    public LimitPersister() {
        super("limit");
    }

    public void save(final boolean hoursPerDayEnabled, final int hoursPerDayValue, final boolean switchOnPerDayEnabled, final int switchOnPerDayValue) {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                editor.putBoolean(HPD_ENABLED, hoursPerDayEnabled);
                editor.putInt(HPD_VALUE, hoursPerDayValue);
                editor.putBoolean(SOPD_ENABLED, switchOnPerDayEnabled);
                editor.putInt(SOPD_VALUE, switchOnPerDayValue);
            }
        });
    }

    public boolean isHoursPerDayEnabled() {
        return pref.getBoolean(HPD_ENABLED, false);
    }

    public int getHoursPerDay() {
        return pref.getInt(HPD_VALUE, 0);
    }

    public boolean isSwitchOnPerDayEnabled() {
        return pref.getBoolean(SOPD_ENABLED, false);
    }

    public int getSwitchOnPerDay() {
        return pref.getInt(SOPD_VALUE, 0);
    }
}
