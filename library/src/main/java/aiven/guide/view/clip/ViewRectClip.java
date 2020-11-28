package aiven.guide.view.clip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public class ViewRectClip extends BaseClipPosition{

    protected float padding;
    protected @IdRes int clipViewId;
    @Nullable
    protected View clipDestView;

    private float parentWidth;
    private float parentHeight;


    private ViewRectClip(){}


    public static ViewRectClip newClipPos(){
        return new ViewRectClip();
    }

    public ViewRectClip setDstView(View view){
        this.clipDestView = view;
        return this;
    }

    public ViewRectClip setDstView(@IdRes int viewId){
        this.clipViewId = viewId;
        return this;
    }

    public ViewRectClip setPadding(float padding){
        this.padding = padding;
        return this;
    }

    public ViewRectClip setOffsetX(float offsetX){
        this.offsetX = offsetX;
        return this;
    }

    public ViewRectClip setOffsetY(float offsetY){
        this.offsetY = offsetY;
        return this;
    }

    public ViewRectClip clipRadius(float radius){
        this.radius = radius;
        return this;
    }

    /**
     * 设置不规则裁剪图形PNG资源
     * @param context
     * @param bitmapId
     * @return
     */
    public ViewRectClip asIrregularShape(@NonNull Context context, @DrawableRes int bitmapId){
        irregularClip = BitmapFactory.decodeResource(context.getResources(),bitmapId);
        return this;
    }

    /**
     * 设置不规则裁剪图形PNG资源
     * @param bitmap
     * @return
     */
    public ViewRectClip asIrregularShape(@NonNull Bitmap bitmap){
        irregularClip = bitmap;
        return this;
    }


    /**
     * 是否镂空区域事件穿透
     * @param eventPassThrough
     */
    public ViewRectClip setEventPassThrough(boolean eventPassThrough) {
        this.eventPassThrough = eventPassThrough;
        return this;
    }


    @Override
    public void build(@Nullable Activity activity) {
        if(clipDestView == null && clipViewId != 0){
            clipDestView = activity.findViewById(clipViewId);
        }
    }

    @Override
    public void build(@Nullable Fragment fragment) {
        if(clipDestView == null && clipViewId != 0 && fragment.getView() != null){
            clipDestView = fragment.getView().findViewById(clipViewId);
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float parentWidth, float parentHeight) {
        initRect(parentWidth,parentHeight);
        this.parentHeight = parentHeight;
        this.parentWidth = parentWidth;
        if(irregularClip == null){
            drawClipShape(canvas,paint);
        }else{
            drawClipBitmap(canvas,paint);
        }
    }

    private void initRect(float parentWidth, float parentHeight){
        if(rectF != null || parentHeight != this.parentHeight || parentWidth != this.parentWidth){
            if(clipDestView != null) {
                int[] pos = new int[2];
                clipDestView.getLocationInWindow(pos);
                float left = pos[0] + offsetX - padding;
                float top = pos[1] + offsetY - padding;

                float width = clipDestView.getMeasuredWidth() + (padding * 2);
                float height = clipDestView.getHeight()+ (padding * 2);

                rectF = new RectF(left,top,left+width,top+height);
                buildDstSizeBitmap();
            }
        }
    }

    private void drawClipShape(Canvas canvas, Paint paint){
        if(rectF == null){
            return;
        }
        PorterDuffXfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        paint.setXfermode(xFermode);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(null);
    }

    private void drawClipBitmap(Canvas canvas,Paint paint){
        if(rectF == null){
            return;
        }
        PorterDuffXfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        paint.setXfermode(xFermode);
        canvas.drawBitmap(irregularClip, rectF.left, rectF.top, paint);
        paint.setXfermode(null);
    }

    private void buildDstSizeBitmap(){
        if(irregularClip != null){
            Bitmap bitmap = Bitmap.createScaledBitmap(irregularClip,Math.round(rectF.width()),Math.round(rectF.height()),true);
            if(bitmap != null && !bitmap.isRecycled()){
                irregularClip = bitmap;
            }
        }
    }
}
