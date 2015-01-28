package guru.nidi.mum;

import android.app.Activity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 *
 */
class MainViews implements Views {
    final TextView text;
    final ImageView graph;
    final HorizontalScrollView scroll;

    MainViews(Activity activity) {
        activity.setContentView(R.layout.main);
        text = (TextView) activity.findViewById(R.id.text);
        graph = (ImageView) activity.findViewById(R.id.graph);
        scroll = (HorizontalScrollView) activity.findViewById(R.id.scroll);
    }
}
