package guru.nidi.mum;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Date;
import java.util.List;

/**
 *
 */
public class MainActivity extends Activity {
    private MainViews view;
    private ListenerReceiver listenerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MainViews(this);

        ListenerService.start(this);
    }

    ScaleGestureDetector mScaleDetector;

    @Override
    protected void onStart() {
        super.onStart();
        List<Event> events = new Persister().getEvents(new Date(115, 0, 26), new Date(115, 0, 30));
//        events = new Persister().getEvents(new Date(115, 0, 27), new Date(115, 0, 27));
        view.text.setText("");
        final Graphic graphic = new Graphic(view.graph);
//        graphic.setEvents(Arrays.asList(
//                new Event(new Date(114, 11, 25).getTime(), 0),
//                new Event(0, new Date(115, 0, 1).getTime())));
        graphic.setEvents(events);
        ((View) view.graph.getParent()).addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                graphic.setSize(right - left, bottom - top);
            }
        });
        view.scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                return mScaleDetector.isInProgress();
            }
        });

        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

//                if (detector.getFocusX() > getRelativeLeft(view.graph) && detector.getFocusX() < getRelativeLeft(view.graph) + view.graph.getWidth()
//                        && detector.getFocusY() > getRelativeTop(view.graph) && detector.getFocusY() < getRelativeTop(view.graph) + view.graph.getHeight()) {
//                System.out.println("scale end"+detector.getCurrentSpan());
                graphic.scale(detector.getScaleFactor());
                System.out.println(detector.getScaleFactor());
//                view.graph.
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                System.out.println("scale begin" + detector.getCurrentSpan());
                view.scroll.setHorizontalScrollBarEnabled(false);
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                System.out.println("scale " + detector.getCurrentSpan());
                return false;
            }
        });

    }

    private static int getRelativeLeft(View view) {
        return view.getLeft() + ((view.getParent() == view.getRootView())
                ? 0
                : getRelativeLeft((View) view.getParent()));
    }

    private static int getRelativeTop(View view) {
        return view.getTop() + ((view.getParent() == view.getRootView())
                ? 0
                : getRelativeTop((View) view.getParent()));
    }
}
