package guru.nidi.mum;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class DateUtils {
    private DateUtils() {
    }

    public static Date todayMidnight() {
        final Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    public static Date addDays(Date date, int days) {
        return addDays(date.getTime(), days);
    }

    public static Date addDays(long date, int days) {
        final Date d = new Date(date);
        d.setDate(d.getDate() + days);
        return d;
    }

    public static Calendar toCalendar(Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static boolean isAtLeastOneDayBefore(Date a, Date b) {
        return a.getYear() <= b.getYear() && a.getDate() < b.getDate();
    }

    public static Calendar nextWeekday(Date date, int weekday) {
        final Calendar cal = toCalendar(date);
        int diff = weekday - cal.get(Calendar.DAY_OF_WEEK);
        if (diff <= 0) {
            diff += 7;
        }
        cal.add(Calendar.DAY_OF_MONTH, diff);
        return cal;
    }
}
