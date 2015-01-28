package guru.nidi.mum;

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
}
