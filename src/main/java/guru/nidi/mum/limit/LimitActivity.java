package guru.nidi.mum.limit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

/**
 *
 */
public class LimitActivity extends Activity {
    private LimitViews view;
    private LimitPersister persister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new LimitViews(this);

        persister = new LimitPersister();

        view.hoursPerDayEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                view.hoursPerDaySelect.setEnabled(isChecked);
                view.hoursPerDayValue.setEnabled(isChecked);
            }
        });
        view.hoursPerDaySelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.hoursPerDayValue.setText(pad(progress / 60) + ":" + pad(progress % 60));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        view.switchOnPerDayEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                view.switchOnPerDaySelect.setEnabled(isChecked);
                view.switchOnPerDayValue.setEnabled(isChecked);
            }
        });
        view.switchOnPerDaySelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.switchOnPerDayValue.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        check(view.hoursPerDayEnabled, persister.isHoursPerDayEnabled());
        view.hoursPerDaySelect.setProgress(persister.getHoursPerDay());

        check(view.switchOnPerDayEnabled, persister.isSwitchOnPerDayEnabled());
        view.switchOnPerDaySelect.setProgress(persister.getSwitchOnPerDay());
    }

    private void check(CheckBox checkBox, boolean checked) {
        checkBox.setChecked(!checked);
        checkBox.setChecked(checked);
    }

    private String pad(int value) {
        return (value < 10 ? "0" : "") + value;
    }

    public void save(View v) {
        persister.save(view.hoursPerDayEnabled.isChecked(), view.hoursPerDaySelect.getProgress(),
                view.switchOnPerDayEnabled.isChecked(), view.switchOnPerDaySelect.getProgress());
        finish();
    }
}
