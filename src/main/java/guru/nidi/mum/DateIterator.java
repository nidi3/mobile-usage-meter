package guru.nidi.mum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static guru.nidi.mum.DateUtils.nextWeekday;

/**
*
*/
enum DateIterator {
    TEN_MINUTE("HH:mm") {
        @Override
        public Date next(Date last) {
            return new Date(last.getYear(), last.getMonth(), last.getDate(), last.getHours(), last.getMinutes() + 10 - last.getMinutes() % 10);
        }
    },
    HOUR("HH:mm") {
        @Override
        public Date next(Date last) {
            return new Date(last.getYear(), last.getMonth(), last.getDate(), last.getHours() + 1, 0);
        }
    },
    SIX_HOUR("dd. HH'h'") {
        @Override
        public Date next(Date last) {
            return new Date(last.getYear(), last.getMonth(), last.getDate(), last.getHours() + 6 - last.getHours() % 6, 0);
        }
    },
    DAY("dd.MM") {
        @Override
        public Date next(Date last) {
            return new Date(last.getYear(), last.getMonth(), last.getDate() + 1);
        }
    },
    WEEK("dd.MM") {
        @Override
        public Date next(Date last) {
            return nextWeekday(last, Calendar.MONDAY).getTime();
        }
    },
    MONTH("MM.yyyy") {
        @Override
        public Date next(Date last) {
            return new Date(last.getYear(), last.getMonth() + 1, 1);
        }
    };
    private SimpleDateFormat format;

    private DateIterator(String format) {
        this.format = new SimpleDateFormat(format);
    }

    public abstract Date next(Date Date);

    public String format(Date date) {
        return format.format(date);
    }

    public static DateIterator fromScale(float milliPerSp) {
        if (milliPerSp < 15000) {
            return TEN_MINUTE;
        }
        if (milliPerSp < 100000) {
            return HOUR;
        }
        if (milliPerSp < 450000) {
            return SIX_HOUR;
        }
        if (milliPerSp < 2500000) {
            return DAY;
        }
        if (milliPerSp < 9000000) {
            return WEEK;
        }
        return MONTH;
    }
}
