package guru.nidi.mum;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 *
 */
public class PopupDatePicker {
    interface DateChangedListener {
        Date onDateChanged(Date date);
    }

    private final TextView input;
    private final Date date;

    public PopupDatePicker(final Activity activity, TextView input, final Date date, final DateChangedListener dateChangedListener) {
        this.input = input;
        this.date = date;
        showDate();

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] cancelled = new boolean[1];
                final DatePickerDialog dialog = new DatePickerDialog(activity, null, date.getYear() + 1900, date.getMonth(), date.getDate());
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface di) {
                        if (!cancelled[0]) {
                            final DatePicker picker = dialog.getDatePicker();
                            Date selection = new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth());
                            if (dateChangedListener != null) {
                                selection = dateChangedListener.onDateChanged(selection);
                            }
                            if (selection != null) {
                                date.setTime(selection.getTime());
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

    public Date getDate() {
        return date;
    }

    private void showDate() {
        input.setText(DateFormat.getDateInstance().format(date));
    }
}


