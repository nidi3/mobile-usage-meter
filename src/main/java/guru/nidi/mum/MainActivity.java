package guru.nidi.mum;

import android.app.Activity;
import android.os.Bundle;

/**
 *
 */
public class MainActivity extends Activity {
    private MainViews view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MainViews(this);
    }
}
