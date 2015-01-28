package guru.nidi.mum;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Calendar;

import static java.util.Calendar.*;

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

        final Calendar fromCal = Calendar.getInstance();
        fromCal.set(DATE, fromCal.get(DATE) - 7);
        from = new PopupDatePicker(this, view.from, fromCal, new PopupDatePicker.DateChangedListener() {
            @Override
            public Calendar onDateChanged(Calendar calendar) {
                if (isAtLeastOneDayBefore(calendar, to.getCalendar())) {
                    from.getCalendar().setTime(calendar.getTime());
                    setEvents();
                    return calendar;
                } else {
                    return null;
                }
            }
        });
        to = new PopupDatePicker(this, view.to, Calendar.getInstance(), new PopupDatePicker.DateChangedListener() {
            @Override
            public Calendar onDateChanged(Calendar calendar) {
                if (isAtLeastOneDayBefore(from.getCalendar(), calendar)) {
                    to.getCalendar().setTime(calendar.getTime());
                    setEvents();
                    return calendar;
                } else {
                    return null;
                }
            }
        });
    }

    private boolean isAtLeastOneDayBefore(Calendar a, Calendar b) {
        return a.get(YEAR) <= b.get(YEAR) && a.get(DAY_OF_YEAR) < b.get(DAY_OF_YEAR);
    }

    private void setEvents() {
        graphic.setEvents(new Persister().getEvents(from.getCalendar().getTime(), to.getCalendar().getTime()));
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
