package guru.nidi.mum;

import android.app.Activity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import guru.nidi.android.layout.ReflectionViews;

/**
 *
 */
class MainViews extends ReflectionViews {
    ImageView left, graph, right;
    HorizontalScrollView scroll;
    TextView from, to;

    MainViews(Activity activity) {
        super(activity, R.layout.main);
    }
}
