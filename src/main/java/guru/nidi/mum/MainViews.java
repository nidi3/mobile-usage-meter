package guru.nidi.mum;

import android.app.Activity;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *
 */
class MainViews implements Views {
    final ImageView left, graph, right;
    final HorizontalScrollView scroll;
    final TextView from, to;

    MainViews(Activity activity) {
        activity.setContentView(R.layout.main);
        left = (ImageView) activity.findViewById(R.id.left);
        graph = (ImageView) activity.findViewById(R.id.graph);
        right = (ImageView) activity.findViewById(R.id.right);
        scroll = (HorizontalScrollView) activity.findViewById(R.id.scroll);
        from = (TextView) activity.findViewById(R.id.from);
        to = (TextView) activity.findViewById(R.id.to);
    }
}
