package aiven.guide.view.layer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import aiven.guide.view.SmartGuide;
import aiven.guide.view.clip.CustomClip;
import aiven.guide.view.clip.ViewRectClip;

public class LayerCreator implements GuidView.InnerOnGuidClickListener {

    @NonNull
    private GuidView mView;

    @NonNull
    private SmartGuide smartGuide;

    private Layer currentLayer;

    @Nullable
    private SmartGuide.OnGuidClickListener mListener;



    private LayerCreator(GuidView view,SmartGuide guide,String tag){
        this.mView = view;
        this.mView.setOnInnerOnGuidClickListener(this);
        this.smartGuide = guide;
        currentLayer = new Layer(tag);
    }

    public static LayerCreator newCreator(GuidView guidView,SmartGuide guide,String tag){
        LayerCreator creator = new LayerCreator(guidView,guide,tag);
        return creator;
    }



    /**
     * 根据VIEW 所在区域定位裁剪区域位置
     * @param clipBuilder
     * @return
     */
    public LayerCreator buildViewRectClip(SmartGuide.ClipPositionBuilder<ViewRectClip> clipBuilder){
        buildDstTarget(clipBuilder);
        return this;
    }


    /**
     * 自定义裁剪区域位置（全屏定位）
     * @param clipBuilder
     * @return
     */
    public LayerCreator buildCustomClip(SmartGuide.ClipPositionBuilder<CustomClip> clipBuilder){
        buildDstTarget(clipBuilder);
        return this;
    }


    /**
     * 设置一个引导说明图形
     * @param builder
     * @return
     */
    public LayerCreator buildIntroPanel(SmartGuide.IntroPanelBuilder builder){
        if(currentLayer == null){
            throw new RuntimeException("newLayer first,please!");
        }
        currentLayer.setFacePanel(builder.buildFacePanel());
        return this;
    }


    /**
     * 添加点击事件
     * @param listener
     * @return
     */
    public LayerCreator setOnGuidClickListener(SmartGuide.OnGuidClickListener listener){
        this.mListener = listener;
        return this;
    }



    public void show(){
        over();
        if(smartGuide != null) {
            smartGuide.show();
        }
    }



    private void buildDstTarget(SmartGuide.ClipPositionBuilder targetBuilder){
        if(currentLayer == null){
            throw new RuntimeException("newLayer first,please!");
        }
        if(targetBuilder!=null){
            currentLayer.setClipTarget(targetBuilder.buildTarget());
        }
    }

    public SmartGuide over(){
         mView.addLayer(currentLayer);
         return smartGuide;
    }



    @Override
    public void destroyed() {
        if(mListener != null){
            mListener.destroyed();
        }
    }

    @Override
    public boolean emptyErrorClicked() {
        if(mListener != null){
            return mListener.emptyErrorClicked();
        }
        return false;
    }

    @Override
    public void clipClicked(String tag) {
        if(mListener != null){
            mListener.clipClicked(smartGuide,mView,tag);
        }
    }

    @Override
    public void introClicked(String tag) {
        if(mListener != null){
            mListener.introClicked(smartGuide,mView,tag);
        }
    }
}
