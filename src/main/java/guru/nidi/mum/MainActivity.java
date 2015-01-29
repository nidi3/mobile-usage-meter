package guru.nidi.mum;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Date;

import static guru.nidi.mum.DateUtils.*;

/**
 *
 */
public class MainActivity extends Activity {
    private MainViews view;
    private ScaleGestureDetector scaleDetector;
    private Graphic graphic;
    private PopupDatePicker from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MainViews(this);

        graphic = new Graphic(view.left, view.graph, view.right);

        ListenerService.start(this);

        scaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                graphic.scale(detector.getScaleFactor());
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                view.scroll.setHorizontalScrollBarEnabled(false);
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }
        });

        to = new PopupDatePicker(this, view.to, todayMidnight(), new PopupDatePicker.DateChangedListener() {
            @Override
            public Date onDateChanged(Date date) {
                if (isAtLeastOneDayBefore(from.getDate(), date)) {
                    to.getDate().setTime(date.getTime());
                    setEvents();
                    return date;
                } else {
                    return null;
                }
            }
        });

        from = new PopupDatePicker(this, view.from, addDays(todayMidnight(), -7), new PopupDatePicker.DateChangedListener() {
            @Override
            public Date onDateChanged(Date date) {
                if (isAtLeastOneDayBefore(date, to.getDate())) {
                    from.getDate().setTime(date.getTime());
                    setEvents();
                    return date;
                } else {
                    return null;
                }
            }
        });
    }

    private void setEvents() {
        graphic.setEvents(new Persister().getEvents(from.getDate(), addDays(to.getDate(), 1)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        setEvents();
        if (view.scroll.getWidth() != 0) {
            graphic.setSize(view.scroll.getWidth(), view.scroll.getHeight());
        }
        view.scroll.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                graphic.setSize(right - left, bottom - top);
            }
        });
        view.scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleDetector.onTouchEvent(event);
                return scaleDetector.isInProgress();
            }
        });
    }
}
