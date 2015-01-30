package guru.nidi.mum.infrastructure;

import android.content.SharedPreferences;
import guru.nidi.android.support.AbstractPersister;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class EventPersister extends AbstractPersister {
    private static final int LEN = 15;

    public EventPersister() {
        super("events");
    }

    private String key(Date date) {
        return "events-" + new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private class EventIterator implements Iterator<Event> {
        private Date date;
        private int pos;
        private String event;

        public EventIterator(Date start) {
            date = new Date(start.getTime());
            search();
        }

        @Override
        public boolean hasNext() {
            return event != null;
        }

        @Override
        public Event next() {
            final long t = Long.parseLong(event.substring(1));
            final Event e = event.charAt(0) == 'A' ? new Event(t, 0) : new Event(0, t);
            search();
            return e;
        }

        private void search() {
            String events = getEvents(key(date));
            while (pos >= events.length() && date.getTime() < System.currentTimeMillis() + 24 * 60 * 60 * 1000) {
                date.setDate(date.getDate() + 1);
                pos = 0;
                events = getEvents(key(date));
            }
            if (events.length() <= pos) {
                event = null;
            } else {
                event = events.substring(pos, pos + LEN + 1);
                pos += LEN + 1;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

//    public List<Event> getEvents(Date date) {
//        final String events = getEvents(key(date));
//        final List<Event> es = new ArrayList<>();
//        if (events.length() > 0) {
//            if (events.charAt(0) == 'B') {
//                es.add(new Event(0, Long.parseLong(events.substring(1, LEN + 2))));
//            }
//        }
//        return es;
//    }

    public List<Event> getEvents(Date from, Date to) {
        final List<Event> es = new ArrayList<>();
        final EventIterator iter = new EventIterator(from);
        Event f = null;
        while (iter.hasNext()) {
            f = iter.next();
            if (f.getFrom() != 0 && f.getFrom() > from.getTime()) {
                break;
            }
        }
        while (f != null && f.getFrom() < to.getTime()) {
            Event t = null;
            while (iter.hasNext() && (t == null || t.getTo() == 0)) {
                t = iter.next();
            }
            es.add(new Event(f.getFrom(), t == null ? 0 : t.getTo()));
            f = null;
            while (iter.hasNext() && (f == null || f.getFrom() == 0)) {
                f = iter.next();
            }
        }
        return es;
    }

    public void addEvent(final boolean start) {
        set(new Setter() {
            @Override
            public void set(SharedPreferences.Editor editor) {
                final Date date = new Date();
                final String key = key(date);
                editor.putString(key, getEvents(key) + (start ? "A" : "B") + timeOf(date));
            }
        });
    }

    private String timeOf(Date d) {
        String s = "" + d.getTime();
        while (s.length() < LEN) {
            s = "0" + s;
        }
        return s;
    }

    private String getEvents(String key) {
        return pref.getString(key, "");
    }


}
