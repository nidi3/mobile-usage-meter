package guru.nidi.mum.limit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import guru.nidi.mum.infrastructure.Notifier;

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

        view.minutesPerDayEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                view.minutesPerDaySelect.setEnabled(isChecked);
                view.minutesPerDayValue.setEnabled(isChecked);
            }
        });
        view.minutesPerDaySelect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.minutesPerDayValue.setText(persister.asHours(progress));
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

        check(view.minutesPerDayEnabled, persister.isMinutesPerDayEnabled());
        view.minutesPerDaySelect.setProgress(persister.getMinutesPerDay());

        check(view.switchOnPerDayEnabled, persister.isSwitchOnPerDayEnabled());
        view.switchOnPerDaySelect.setProgress(persister.getSwitchOnPerDay());
    }

    private void check(CheckBox checkBox, boolean checked) {
        checkBox.setChecked(!checked);
        checkBox.setChecked(checked);
    }


    public void save(View v) {
        persister.save(view.minutesPerDayEnabled.isChecked(), view.minutesPerDaySelect.getProgress(),
                view.switchOnPerDayEnabled.isChecked(), view.switchOnPerDaySelect.getProgress());
        Notifier.start(this);
        finish();
    }
}
