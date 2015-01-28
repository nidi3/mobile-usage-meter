package guru.nidi.mum;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

/**
 *
 */
public class MobileUsageMeter extends Application {
    private static Context context;
    public static DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        displayMetrics = context.getResources().getDisplayMetrics();
    }

    public static SharedPreferences sharedPreferences(String name) {
        return context.getSharedPreferences(name, MODE_PRIVATE);
    }
}
