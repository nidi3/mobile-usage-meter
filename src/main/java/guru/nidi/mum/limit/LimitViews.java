package guru.nidi.mum.limit;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import guru.nidi.android.layout.ReflectionViews;
import guru.nidi.mum.R;

/**
 *
 */
class LimitViews extends ReflectionViews {
    CheckBox minutesPerDayEnabled, switchOnPerDayEnabled;
    TextView minutesPerDayValue, switchOnPerDayValue;
    SeekBar minutesPerDaySelect, switchOnPerDaySelect;

    public LimitViews(Activity activity) {
        super(activity, R.layout.limit);
    }
}
