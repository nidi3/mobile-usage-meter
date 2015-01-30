package guru.nidi.mum.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.widget.ImageView;
import guru.nidi.mum.infrastructure.Event;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static guru.nidi.android.ApplicationContextHolder.displayMetrics;
import static guru.nidi.mum.DateUtils.addDays;

/**
 *
 */
public class Graphic {
    private static final float TOP_BORDER = dpToPixel(5);

    private final Paintable left, graph, right;

    private float scale = 1;
    private int width, height;
    private List<Event> events;
    private Paint back, text, onoff, share, count;

    public Graphic(ImageView left, ImageView graph, ImageView right,float scale) {
        this.left = new Paintable(left, Bitmap.createBitmap(left.getLayoutParams().width, left.getLayoutParams().height, Bitmap.Config.ARGB_8888));
        this.graph = new Paintable(graph);
        this.right = new Paintable(right, Bitmap.createBitmap(right.getLayoutParams().width, right.getLayoutParams().height, Bitmap.Config.ARGB_8888));
        this.scale=scale;
        initPaints();
    }

    private void initPaints() {
        back = new Paint();
        back.setColor(Color.WHITE);
        back.setStyle(Paint.Style.FILL);

        text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(spToPixel(10));

        onoff = new Paint();
        onoff.setColor(Color.BLUE);
        onoff.setStyle(Paint.Style.FILL);
        onoff.setStrokeWidth(dpToPixel(3));
        onoff.setTextSize(spToPixel(10));

        share = onoff;

        count = new Paint();
        count.setColor(Color.RED);
        count.setStrokeWidth(dpToPixel(3));
        count.setTextSize(spToPixel(10));
    }

    public void setSize(int width, int height) {
        scale = Math.min(4096f / width, scale);
        if (graph.getCanvas() == null || width != this.width || graph.getWidth() != width * scale || height != graph.getHeight()) {
            this.width = width;
            this.height = height;
            graph.setBitmap(Bitmap.createBitmap((int) (width * scale), height, Bitmap.Config.ARGB_8888));
            draw();
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
        scale = Math.max(1, scale * factor);
        setSize(width, height);
    }

    private void draw() {
        if (graph.getCanvas() != null && events != null) {
            clear(left);
            clear(graph);
            clear(right);

            final int baseY = graph.getHeight() - (int) spToPixel(20);
            graph.drawLine(0, baseY, graph.getWidth(), baseY, text);

            final Event last = events.get(events.size() - 1);
            if (last.getTo() == 0) {
                events.set(events.size() - 1, new Event(last.getFrom(), System.currentTimeMillis()));
            }

            final long from = events.get(0).getFrom();
            final long to = events.get(events.size() - 1).getTo();
            final float pixelPerMilli = 1f * graph.getWidth() / (to - from);
            final DateIterator dateIterator = DateIterator.fromScale(1 / pixelToSp(pixelPerMilli));

            Date current = addDays(from, -1);
            int index = 0;
            final List<PointF> sharePoints = new ArrayList<>();
            final List<PointF> countPoints = new ArrayList<>();
            while (current.getTime() < to) {
                Date next = dateIterator.next(current);
                while (events.get(index).getTo() < current.getTime()) {
                    index++;
                }
                long sum = 0;
                int ons = 0;
                while (index < events.size() && events.get(index).getFrom() < next.getTime()) {
                    final long sumFrom = Math.max(events.get(index).getFrom(), current.getTime());
                    final long sumTo = Math.min(events.get(index).getTo(), next.getTime());
                    sum += (sumTo - sumFrom);
                    ons++;
                    index++;
                }
                if (index > 0) {
                    index--;
                }

                final float cx = getX((current.getTime() + next.getTime()) / 2, from, pixelPerMilli);
                final long millis = next.getTime() - current.getTime();
                sharePoints.add(new PointF(cx, 1f * sum / millis));
                countPoints.add(new PointF(cx, 1f * ons / millis));

                drawCoord(baseY, from, pixelPerMilli, dateIterator, next);
                current = next;
            }
            drawPoints(sharePoints, countPoints);
            drawOnOff((int) (baseY - dpToPixel(15)), from, pixelPerMilli);
        }
    }

    private void clear(Paintable paintable) {
        paintable.drawRect(0, 0, paintable.getWidth(), paintable.getHeight(), back);
    }

    private void drawPoints(List<PointF> sharePoints, List<PointF> countPoints) {
        PointF last = null;
        for (PointF point : sharePoints) {
            if (last != null) {
                graph.drawLine(last.x, getY(last.y), point.x, getY(point.y), share);
            }
            last = point;
        }
        left.drawLine(left.getWidth() - 1, 0, left.getWidth() - 1, left.getHeight(), text);
        for (float f = 0; f < 1.1; f += .25) {
            final int y = getY(f);
            left.drawLine(0, y, left.getWidth(), y, text);
            left.drawText((int) (100 * f) + "%", 0, y + spToPixel(10), share);
        }

        float max = 0;
        for (PointF point : countPoints) {
            if (point.y > max) {
                max = point.y;
            }
        }
        last = null;
        for (PointF point : countPoints) {
            if (last != null) {
//                graph.drawLine(last.x, getY(last.y / max), point.x, getY(point.y / max), count);
            }
            last = point;
//            graph.drawRect(point.x - dpToPixel(5), getY(0), point.x + dpToPixel(5), getY(point.y / max), count);
            graph.drawCircle(point.x, getY(point.y / max), dpToPixel(5), count);
        }
        right.drawLine(0, 0, 0, right.getHeight(), text);
        final NumberFormat format = new DecimalFormat(max * 60 * 60 * 1000 >= 10 ? "##" : "0.#");
        for (float f = 0; f < 1.1; f += .25) {
            final int y = getY(f);
            right.drawLine(0, y, right.getWidth(), y, text);
            right.drawText(format.format(max * 60 * 60 * 1000 * f) + "/h", 0, y + spToPixel(10), count);
        }
    }

    private void drawCoord(int y, long startMillis, float pixelPerMilli, DateIterator dateIterator, Date date) {
        final float x = getX(date.getTime(), startMillis, pixelPerMilli);
        graph.drawLine(x, 0, x, y + dpToPixel(10), text);
        final String s = dateIterator.format(date);
        final int len = (int) text.measureText(s, 0, s.length());
        graph.drawText(s, x - len / 2, y + spToPixel(18), text);
    }

    private void drawOnOff(int y, long startMillis, float pixelPerMilli) {
        for (Event e : events) {
            final float x1 = getX(e.getFrom(), startMillis, pixelPerMilli);
            final float x2 = getX(e.getTo(), startMillis, pixelPerMilli);
            graph.drawRect(x1, y, x2, y + dpToPixel(10), onoff);
        }
    }

    private float getX(long millis, long startMillis, float pixelPerMilli) {
        return (millis - startMillis) * pixelPerMilli;
    }

    private int getY(float value) {
        return (int) ((left.getHeight() - TOP_BORDER) * (1 - value) + TOP_BORDER - 1);
    }

    private static float dpToPixel(int dp) {
        return dp * displayMetrics().density;
    }

    private static float spToPixel(int sp) {
        return sp * displayMetrics().scaledDensity;
    }

    private static float pixelToSp(float pixel) {
        return pixel / displayMetrics().scaledDensity;
    }

}