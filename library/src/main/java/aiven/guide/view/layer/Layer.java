package aiven.guide.view.layer;


import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.clip.BaseClipPosition;
import aiven.guide.view.face.IntroPanel;

class Layer {
    @NonNull
    private ClipHold clipHold;
    @NonNull
    private FaceHold face;
    @NonNull
    protected String tag;

    public Layer(String tag) {
        this.clipHold = new ClipHold();
        this.face = new FaceHold();
        this.tag = tag;
    }


    public void setClipTarget(BaseClipPosition target){
        clipHold.setTarget(target);
    }

    public void setFacePanel(IntroPanel facePanel){
        this.face.setFacePanel(facePanel);
    }


    public void draw(Canvas canvas, Paint paint, boolean clip, float parentWidth, float parentHeight){
        if(clip){
            clipHold.draw(canvas,paint,null,parentWidth,parentHeight);
        }else{
            face.draw(canvas,paint,clipHold.getRectF(),parentWidth,parentHeight);
        }
    }

    protected void build(@Nullable Activity activity) {
        if(clipHold != null){
            clipHold.build(activity);
        }
        if(face != null){
            face.build(activity);
        }
    }


    protected void build(@Nullable Fragment fragment) {
        if(clipHold != null){
            clipHold.build(fragment);
        }
        if(face != null){
            face.build(fragment);
        }
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public boolean isClipEventPassThrough(){
        if(clipHold != null){
            return clipHold.isEventPassThrough();
        }
        return false;
    }


    public boolean isTouchInClip(float x,float y){
        if(clipHold != null && clipHold.getRectF() != null){
            if(clipHold.getRectF().contains(x,y)){
                return true;
            }
        }
        return false;
    }

    public boolean isTouchInIntro(float x,float y){
        if(face != null && face.getRectF() != null){
            if(face.getRectF().contains(x,y)){
                return true;
            }
        }
        return false;
    }
}
