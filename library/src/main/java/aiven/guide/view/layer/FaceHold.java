package aiven.guide.view.layer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.base.LayerBaseHold;
import aiven.guide.view.face.IntroPanel;

class FaceHold extends LayerBaseHold {
    protected IntroPanel facePanel;

    public void setFacePanel(IntroPanel facePanel) {
        this.facePanel = facePanel;
    }

    @Override
    public void build(@Nullable Activity activity) {
    }

    @Override
    public void build(@Nullable Fragment activity) {
    }

    @Override
    public void draw(Canvas canvas, Paint paint,@Nullable RectF clipRectF, float parentWidth, float parentHeight) {
        if(this.facePanel != null) {
            this.facePanel.draw(canvas, paint, clipRectF, parentWidth, parentHeight);
        }
    }


    @Nullable
    public RectF getRectF() {
        if(facePanel != null){
            return facePanel.getRectF();
        }
        return null;
    }

}
