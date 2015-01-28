package guru.nidi.mum;

import android.content.SharedPreferences;

/**
 *
 */
public class AbstractPersister {
    protected final SharedPreferences pref;

    protected AbstractPersister(String name) {
        pref = MobileUsageMeter.sharedPreferences(name);
    }

    protected interface Setter {
        void set(SharedPreferences.Editor editor);
    }

    protected void set(Setter setter) {
        final SharedPreferences.Editor edit = pref.edit();
        setter.set(edit);
        edit.apply();
    }
}
