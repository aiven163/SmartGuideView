package aiven.guide.view.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import aiven.guide.view.SmartGuide;

public class IntroPanel {
    private Context context;
    protected int with;
    protected int height;

    protected float offsetX;
    protected float offsetY;

    protected Bitmap introBitmap;

    private RectF rectF;


    protected SmartGuide.AlignX alignX = SmartGuide.AlignX.ALIGN_RIGHT;
    protected SmartGuide.AlignY alignY = SmartGuide.AlignY.ALIGN_BOTTOM;

    private IntroPanel(@NonNull Context context){
        this.context = context;
    }


    public static IntroPanel newIntroPanel(@NonNull Context context){
        return new IntroPanel(context);
    }


    public IntroPanel setOffset(float offsetX,float offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    public IntroPanel setSize(int width,int height){
        this.with = width;
        this.height = height;
        return this;
    }

    public IntroPanel setAlign(SmartGuide.AlignX x,SmartGuide.AlignY y){
        this.alignX = x;
        this.alignY = y;
        return this;
    }

    public IntroPanel setIntroBmp(Bitmap bmp){
        this.introBitmap = bmp;
        return this;
    }
    public IntroPanel setIntroBmp(@DrawableRes int bmpRes){
        this.introBitmap = BitmapFactory.decodeResource(context.getResources(),bmpRes);
        return this;
    }

    public void draw(Canvas canvas, Paint paint, @Nullable RectF clipRectF, float parentWidth, float parentHeight){
        if(clipRectF == null){
            return;
        }
        if(introBitmap == null){
            return;
        }
        if(rectF == null){
            initRectF(clipRectF);
        }
        initBitmap();
        canvas.drawBitmap(introBitmap,rectF.left,rectF.top,paint);
    }




    private void initRectF(@NonNull RectF clipRectF){
        float left;
        float top;
        if(alignX == SmartGuide.AlignX.ALIGN_LEFT){
            left = clipRectF.left - with - offsetX;
        }else{
            left = clipRectF.right + offsetX;
        }

        if(alignY == SmartGuide.AlignY.ALIGN_TOP){
            top = clipRectF.top - height - offsetY;
        }else{
            top = clipRectF.bottom + offsetY;
        }

        rectF = new RectF(left,top,left + with,top + height);
    }


    private void initBitmap(){
        if(introBitmap != null){
            if(Math.abs(introBitmap.getWidth() - with) > 1 || Math.abs(introBitmap.getHeight() - height) > 1){
                introBitmap = Bitmap.createScaledBitmap(introBitmap,with,height,true);
            }
        }
    }


    public RectF getRectF() {
        return rectF;
    }
}
