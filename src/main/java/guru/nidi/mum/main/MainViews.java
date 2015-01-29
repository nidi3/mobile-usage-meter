package guru.nidi.mum.main;

import android.app.Activity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import guru.nidi.android.layout.ReflectionViews;
import guru.nidi.mum.R;

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
