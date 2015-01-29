package guru.nidi.mum.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

/**
*
*/
class Paintable {
    private final ImageView view;
    private Bitmap bitmap;
    private Canvas canvas;

    public Paintable(ImageView view) {
        this.view = view;
    }

    public Paintable(ImageView view, Bitmap bitmap){
        this(view);
        setBitmap(bitmap);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        canvas = new Canvas(bitmap);
        view.setImageBitmap(bitmap);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public int getHeight() {
        return canvas.getHeight();
    }

    public int getWidth() {
        return canvas.getWidth();
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void drawRect(float left, float top, float right, float bottom, Paint paint) {
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public void drawText(String text, float x, float y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }
}
