package aiven.guide.view.layer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.base.LayerBaseHold;
import aiven.guide.view.clip.BaseClipPosition;

class ClipHold extends LayerBaseHold {

    @Nullable
    protected BaseClipPosition target;

    @Override
    public void build(@Nullable Activity activity) {
        if(target != null){
            target.build(activity);
        }
    }

    @Override
    public void build(@Nullable Fragment fragment) {
        if(target != null){
            target.build(fragment);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint,@Nullable RectF clipRectF, float parentWidth, float parentHeight) {
        if(target != null){
            target.draw(canvas,paint,parentWidth,parentHeight);
        }
    }

    @Nullable
    public RectF getRectF(){
        if(target != null){
            return target.getRectF();
        }
        return null;
    }

    public boolean isEventPassThrough() {
        if(target != null){
            return target.isEventPassThrough();
        }
        return false;
    }


    public void setTarget(@NonNull BaseClipPosition target) {
        this.target = target;
    }
}
