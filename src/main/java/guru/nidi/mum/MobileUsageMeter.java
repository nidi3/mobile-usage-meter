package guru.nidi.mum;

import android.app.Application;
import guru.nidi.android.ApplicationContextHolder;
import guru.nidi.android.log.RemoteLoggingCrashHandler;

/**
 *
 */
public class MobileUsageMeter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContextHolder.init(getApplicationContext());
        RemoteLoggingCrashHandler.install("http://lpc.nidi.guru/crash.php");
    }
}
