package guru.nidi.mum;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 *
 */
public class PopupDatePicker {
    interface DateChangedListener {
        Calendar onDateChanged(Calendar calendar);
    }

    private final TextView input;
    private final Calendar calendar;

    public PopupDatePicker(final Activity activity, TextView input, final Calendar calendar, final DateChangedListener dateChangedListener) {
        this.input = input;
        this.calendar = calendar;
        showDate();

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] cancelled = new boolean[1];
                final DatePickerDialog dialog = new DatePickerDialog(activity, null, calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface di) {
                        if (!cancelled[0]) {
                            final DatePicker picker = dialog.getDatePicker();
                            Calendar cal = Calendar.getInstance();
                            cal.set(YEAR, picker.getYear());
                            cal.set(MONTH, picker.getMonth());
                            cal.set(DAY_OF_MONTH, picker.getDayOfMonth());
                            if (dateChangedListener != null) {
                                cal = dateChangedListener.onDateChanged(cal);
                            }
                            if (cal != null) {
                                calendar.setTime(cal.getTime());
                            }
                            showDate();
                        }
                    }
                });
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelled[0] = true;
                    }
                });
                dialog.show();
            }
        });
    }

    public Calendar getCalendar() {
        return calendar;
    }

    private void showDate() {
        input.setText(DateFormat.getDateInstance().format(calendar.getTime()));
    }
}


