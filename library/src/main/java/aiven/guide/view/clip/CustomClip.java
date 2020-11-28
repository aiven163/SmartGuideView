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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.SmartGuide;

public class CustomClip extends BaseClipPosition{

    protected SmartGuide.AlignX alignX = SmartGuide.AlignX.ALIGN_LEFT;
    protected SmartGuide.AlignY alignY = SmartGuide.AlignY.ALIGN_TOP;
    protected int clipWidth;
    protected int clipHeight;

    private float parentWidth;
    private float parentHeight;

    private CustomClip(){}


    public static CustomClip newClipPos(){
        return new CustomClip();
    }

    public CustomClip setClipSize(int width, int height){
        this.clipWidth = width;
        this.clipHeight = height;
        return this;
    }

    public CustomClip setAlignX(@NonNull SmartGuide.AlignX alignX){
        this.alignX = alignX;
        return this;
    }

    public CustomClip setAlignY(@NonNull SmartGuide.AlignY alignY){
        this.alignY = alignY;
        return this;
    }

    public CustomClip setOffsetX(float offsetX){
        this.offsetX = offsetX;
        return this;
    }

    public CustomClip setOffsetY(float offsetY){
        this.offsetY = offsetY;
        return this;
    }

    public CustomClip clipRadius(float radius){
        this.radius = radius;
        return this;
    }


    /**
     * 设置不规则裁剪图形PNG资源
     * @param context
     * @param bitmapId
     * @return
     */
    public CustomClip asIrregularShape(@NonNull Context context, @DrawableRes int bitmapId){
        irregularClip = BitmapFactory.decodeResource(context.getResources(),bitmapId);
        return this;
    }

    /**
     * 设置不规则裁剪图形PNG资源
     * @param bitmap
     * @return
     */
    public CustomClip asIrregularShape(@NonNull Bitmap bitmap){
        irregularClip = bitmap;
        return this;
    }

    /**
     * 是否镂空区域事件穿透
     * @param eventPassThrough
     */
    public CustomClip setEventPassThrough(boolean eventPassThrough) {
        this.eventPassThrough = eventPassThrough;
        return this;
    }


    @Override
    public void build(@Nullable Activity activity) {
        buildDstSizeBitmap();
    }

    @Override
    public void build(@Nullable Fragment activity) {
        buildDstSizeBitmap();
    }

    @Override
    public void draw(Canvas canvas, Paint paint,float parentWidth, float parentHeight) {
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
            float top = 0;
            float left = 0;
            if(alignX == SmartGuide.AlignX.ALIGN_RIGHT){
                left = parentWidth - offsetX - clipWidth;
            }else{
                left = offsetX;
            }
            if(alignY == SmartGuide.AlignY.ALIGN_BOTTOM){
                top = parentHeight - offsetY - clipHeight;
            }else{
                top = offsetY;
            }
            rectF = new RectF(left,top,left + clipWidth,top + clipHeight);
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
            Bitmap bitmap = Bitmap.createScaledBitmap(irregularClip,clipWidth,clipHeight,true);
            if(bitmap != null && !bitmap.isRecycled()){
                irregularClip = bitmap;
            }
        }
    }







}
