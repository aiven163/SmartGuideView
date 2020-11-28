package aiven.guide.view.layer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import aiven.guide.view.util.MLog;
import aiven.guide.view.util.SmartUtils;

public class GuidView extends View {
    private static final String TAG ="smartGuide998";
    private Paint mPaint;
    private List<Layer> mLayerList;
    private int drawTaskId;
    private RectF mRectF;
    private int backgroundColor = 0X80000000;
    private float mMinTouchSlop;

    @Nullable
    private InnerOnGuidClickListener mClickListener;


    public GuidView(Context context) {
        this(context,null);
    }

    public GuidView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLayerList = new ArrayList<>();
        mMinTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        if(mRectF == null || mRectF.width() < width || mRectF.height() < height){
            mRectF = new RectF(0,0,width,height);
        }
        postInvalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mRectF == null){
            return;
        }
        drawTaskId = canvas.saveLayer(mRectF,mPaint);
        canvas.drawColor(backgroundColor);
        if(mLayerList != null){
            for(int i = 0;i < mLayerList.size(); i++){
                mLayerList.get(i).draw(canvas,mPaint,true,mRectF.width(),mRectF.height());
            }
            for(int i = 0;i < mLayerList.size(); i++){
                mLayerList.get(i).draw(canvas,mPaint,false,mRectF.width(),mRectF.height());
            }
        }
        canvas.restoreToCount(drawTaskId);
    }


    public void addLayer(Layer layer){
        if(layer == null){
            return;
        }
        mLayerList.add(layer);
        postInvalidate();
    }



    public void build(@Nullable Activity activity) {
        FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if(rootView != null){
            View oldView = rootView.findViewWithTag(TAG);
            if(oldView != null) {
                rootView.removeView(oldView);
            }
            setTag(TAG);
            rootView.addView(this,params);
        }else{
            return;
        }
        if(mLayerList != null){
            for(int i = 0; i < mLayerList.size(); i ++) {
                mLayerList.get(i).build(activity);
            }
        }
        postInvalidate();
    }


    public void build(@Nullable Fragment fragment) {
        FrameLayout rootView = (FrameLayout) fragment.requireActivity().getWindow().getDecorView();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if(rootView != null){
            View oldView = rootView.findViewWithTag(TAG);
            if(oldView != null) {
                rootView.removeView(oldView);
            }
            setTag(TAG);
            rootView.addView(this,params);
        }else{
            return;
        }

        if(mLayerList != null){
            for(int i = 0;i<mLayerList.size();i++) {
                mLayerList.get(i).build(fragment);
            }
        }

        postInvalidate();
    }


    public void clearLayers(){
        if(mLayerList != null) {
            mLayerList.clear();
        }
        postInvalidate();
    }

    public void removeLayerByTag(String tag){
        try {
            if (mLayerList != null && !SmartUtils.strIsEmpty(tag)) {
                for (int i = 0; i < mLayerList.size(); i++) {
                    if (tag.equals(mLayerList.get(i).getTag())) {
                        mLayerList.remove(i);
                        i--;
                    }
                }
            }
        }catch (Exception e){
            MLog.E(e);
        }
        postInvalidate();
    }


    public void dismiss(){
       ViewGroup parentView = (ViewGroup)getParent();
       if(parentView != null){
           parentView.removeView(this);
       }
       if(mClickListener != null){
           mClickListener.destroyed();
       }
    }






    private boolean isTouchIn;
    private float mPressX;
    private float mPressY;
    private long mLastPressTime;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isTouchIn = false;
                mPressX = event.getX();
                mPressY = event.getY();
                mLastPressTime = System.currentTimeMillis();
                boolean[] result = checkIsLayerIn(mPressX, mPressY);
                if (result[0] && result[1]) {
                    isTouchIn = false; // 热区内，并且透传事件
                } else {
                    isTouchIn = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (isTouchIn) {
                    if (System.currentTimeMillis() - mLastPressTime < 300) {
                        if (Math.abs(x - mPressX) < mMinTouchSlop && Math.abs(y - mPressY) < mMinTouchSlop) {
                            executeClick(mPressX, mPressY);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouchIn = false;
                break;
            default:
                break;
        }
        return isTouchIn ? isTouchIn : super.onTouchEvent(event);
    }

    private void executeClick(float x, float y) {
        MLog.I(
                "Layer",
                "mPressX:"
                        + mPressX
                        + "   mPressY:"
                        + mPressY
                        + "    mLastPressTime:"
                        + mLastPressTime
                        + "    mMinTouchSlop:"
                        + mMinTouchSlop);
        if(mClickListener == null){
            return;
        }

        for (int i = 0; i < mLayerList.size(); i++) {
            Layer item = mLayerList.get(i);
            if(item != null){
                if(item.isTouchInClip(x,y)){
                    mClickListener.clipClicked(item.getTag());
                    return;
                }else if(item.isTouchInIntro(x,y)){
                    mClickListener.introClicked(item.getTag());
                    return;
                }
            }
        }

        if (isTouchIn && mClickListener != null) {
            boolean result = mClickListener.emptyErrorClicked();
            if(result){
                dismiss();
            }
        }
    }

    private boolean[] checkIsLayerIn(float x, float y) {
        //0为 标识 区域内，1为标识透传事件
        boolean[] result = new boolean[] {false, false};
        for (int i = 0; i < mLayerList.size(); i++) {
            Layer item = mLayerList.get(i);
            if (item != null && item.isTouchInClip(x,y)) {
                result[0] = true;
                result[1] = item.isClipEventPassThrough();
                break;
            }
        }
        return  result;
    }


    /**
     * 设置点击事件
     * @param listener
     */
    protected void setOnInnerOnGuidClickListener(InnerOnGuidClickListener listener){
        this.mClickListener = listener;
    }



    /**
     * 引导层点击事件监听器
     */
    public interface InnerOnGuidClickListener{
        /**
         * 引导层销毁回调
         */
        void destroyed();

        /**
         * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         * @return
         */
        boolean emptyErrorClicked();

        /**
         * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调
         * @param tag
         */
        void clipClicked(String tag);

        /**
         * 引导介绍区域点击回调
         * @param tag
         */
        void introClicked(String tag);
    }
}
