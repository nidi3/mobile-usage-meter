package guru.nidi.mum.infrastructure;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class Event {
    private final long from, to;

    public Event(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public long duration() {
        return to - from;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    @Override
    public String toString() {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return (from == 0 ? "" : format.format(new Date(from))) + " - " +
                (to == 0 ? "" : format.format(new Date(to)));
    }
}
