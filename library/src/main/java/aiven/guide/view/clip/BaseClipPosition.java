package aiven.guide.view.clip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseClipPosition {
    protected float offsetX;
    protected float offsetY;
    protected float radius;
    @Nullable
    protected Bitmap irregularClip;

    @Nullable
    protected RectF rectF;

    protected boolean eventPassThrough;

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }


    public float getRadius() {
        return radius;
    }

    @Nullable
    public RectF getRectF() {
        return rectF;
    }

    @Nullable
    public Bitmap getIrregularClip() {
        return irregularClip;
    }

    public void build(@Nullable Activity activity) {
    }

    public void build(@Nullable Fragment activity) {
    }

    abstract public void draw(Canvas canvas, Paint paint,float parentWidth, float parentHeight);

    public boolean isEventPassThrough() {
        return eventPassThrough;
    }

}
