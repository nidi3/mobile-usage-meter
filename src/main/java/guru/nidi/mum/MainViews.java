package guru.nidi.mum;

import android.app.Activity;
import android.widget.TextView;

/**
 *
 */
class MainViews implements Views {
    final TextView text;

    MainViews(Activity activity) {
        activity.setContentView(R.layout.main);
        text = (TextView) activity.findViewById(R.id.text);
    }
}
