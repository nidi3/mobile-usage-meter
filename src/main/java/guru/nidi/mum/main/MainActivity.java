package guru.nidi.mum.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import guru.nidi.mum.R;
import guru.nidi.mum.infrastructure.EventPersister;
import guru.nidi.mum.infrastructure.ListenerService;
import guru.nidi.mum.limit.LimitActivity;
import guru.nidi.mum.view.Graphic;
import guru.nidi.mum.view.PopupDatePicker;

import java.util.Date;

import static guru.nidi.mum.DateUtils.addDays;
import static guru.nidi.mum.DateUtils.todayMidnight;

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
                if (!date.before(from.getDate())) {
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
                if (!date.after(to.getDate())) {
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
        graphic.setEvents(new EventPersister().getEvents(from.getDate(), addDays(to.getDate(), 1)));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_limits:
                startActivity(new Intent(this, LimitActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
