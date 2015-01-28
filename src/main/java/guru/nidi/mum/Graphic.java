package guru.nidi.mum;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class Graphic {
    private static final float BORDER = dpToPixel(10);

    private final ImageView imageView;

    private float scale = 1;
    private int width;
    private Canvas canvas;
    private List<Event> events;

    public Graphic(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setSize(int width, int height) {
        if (canvas == null || width != this.width || canvas.getWidth() != width * scale || height != canvas.getHeight()) {
            this.width = width;
            final Bitmap bitmap = Bitmap.createBitmap((int) (width * scale), height, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            draw();
            imageView.setImageBitmap(bitmap);
        }
    }

    public void setEvents(List<Event> events) {
        if (events.isEmpty()) {
            events.add(new Event(System.currentTimeMillis(), System.currentTimeMillis()));
        }
        this.events = events;
        draw();
    }

    public void scale(float factor) {
        scale = Math.min(4096f / width, Math.max(1, scale * factor));
        setSize(width, canvas.getHeight());
    }

    private void draw() {
        if (canvas != null && events != null) {
            final Paint back = new Paint();
            back.setColor(Color.WHITE);
            back.setStyle(Paint.Style.FILL);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), back);

            final Paint text = new Paint();
            text.setColor(Color.BLACK);
            text.setTextSize(spToPixel(10));

            final Paint onoff = new Paint();
            onoff.setColor(Color.BLUE);
            onoff.setStyle(Paint.Style.FILL);

            final int baseY = canvas.getHeight() - (int) spToPixel(20);
            canvas.drawLine(BORDER, baseY, canvas.getWidth() - BORDER, baseY, text);

            final Event last = events.get(events.size() - 1);
            if (last.getTo() == 0) {
                events.set(events.size() - 1, new Event(last.getFrom(), System.currentTimeMillis()));
            }

            final long from = events.get(0).getFrom();
            final long to = events.get(events.size() - 1).getTo();
            final float drawableWidth = canvas.getWidth() - 2 * BORDER;
            final float pixelPerMillis = drawableWidth / (to - from);
            final float millisPerSp = 1 / pixelToSp(pixelPerMillis);
            final DateIterator dateIterator;
            if (millisPerSp < 15000) {
                dateIterator = DateIterator.TEN_MINUTE;
            } else if (millisPerSp < 100000) {
                dateIterator = DateIterator.HOUR;
            } else if (millisPerSp < 450000) {
                dateIterator = DateIterator.SIX_HOUR;
            } else if (millisPerSp < 2500000) {
                dateIterator = DateIterator.DAY;
            } else if (millisPerSp < 9000000) {
                dateIterator = DateIterator.WEEK;
            } else {
                dateIterator = DateIterator.MONTH;
            }
            Date current = new Date(from - 24 * 60 * 60 * 1000);
            int index = 0;
            while (current.getTime() < to) {
                Date next = dateIterator.next(current);
                while (events.get(index).getTo() < current.getTime()) {
                    index++;
                }
                long sum = 0;
                while (index < events.size() && events.get(index).getFrom() < next.getTime()) {
                    final long sumFrom = Math.max(events.get(index).getFrom(), current.getTime());
                    final long sumTo = Math.min(events.get(index).getTo(), next.getTime());
                    sum += (sumTo - sumFrom);
                    index++;
                }
                if (index > 0) {
                    index--;
                }

                final float share = 1 - 1.0f * sum / (next.getTime() - current.getTime());
                final float cx = BORDER + ((current.getTime() + next.getTime()) / 2 - from) * pixelPerMillis;
                canvas.drawCircle(cx, (int) ((baseY - BORDER - dpToPixel(20)) * share), dpToPixel(5), onoff);

                final float x = BORDER + (next.getTime() - from) * pixelPerMillis;
                canvas.drawLine(x, BORDER, x, baseY + dpToPixel(10), text);
                final String s = dateIterator.format(next);
                final int len = (int) text.measureText(s, 0, s.length());
                canvas.drawText(s, x - len / 2, baseY + spToPixel(18), text);
                current = next;
            }

            for (Event e : events) {
                final float x1 = BORDER + (e.getFrom() - from) * pixelPerMillis;
                final float x2 = BORDER + (e.getTo() - from) * pixelPerMillis;
                canvas.drawRect(x1, baseY - dpToPixel(15), x2, baseY - dpToPixel(5), onoff);
            }
        }
    }

    private enum DateIterator {
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
                final Calendar cal = Calendar.getInstance();
                cal.set(last.getYear() + 1900, last.getMonth(), last.getDate());
                int diff = Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK);
                if (diff <= 0) {
                    diff += 7;
                }
                cal.add(Calendar.DAY_OF_MONTH, diff);
                return cal.getTime();
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

    }

    private static float dpToPixel(int dp) {
        return dp * MobileUsageMeter.displayMetrics.density;
    }

    private static float spToPixel(int sp) {
        return sp * MobileUsageMeter.displayMetrics.scaledDensity;
    }

    private static float pixelToSp(float pixel) {
        return pixel / MobileUsageMeter.displayMetrics.scaledDensity;
    }

}