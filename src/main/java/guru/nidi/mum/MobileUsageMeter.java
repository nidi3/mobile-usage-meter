package guru.nidi.mum;

import android.app.Application;
import guru.nidi.android.ApplicationContextHolder;

/**
 *
 */
public class MobileUsageMeter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContextHolder.init(getApplicationContext());
    }
}
