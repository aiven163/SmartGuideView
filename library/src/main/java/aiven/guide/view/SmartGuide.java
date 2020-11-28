package aiven.guide.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import aiven.guide.view.clip.BaseClipPosition;
import aiven.guide.view.face.IntroPanel;
import aiven.guide.view.layer.GuidView;
import aiven.guide.view.layer.LayerCreator;
import aiven.guide.view.util.SmartUtils;

public class SmartGuide {

    @NonNull
    private GuidView mView;
    @Nullable
    private Activity mActivity;
    @Nullable
    private Fragment mFragment;

    private SmartGuide(Context context){
        mView = new GuidView(context);
    }

    public static SmartGuide newGuide(Activity activity){
        SmartGuide guide =  new SmartGuide(activity);
        guide.mActivity = activity;
        return guide;
    }

    public static SmartGuide newGuide(Fragment fragment){
        SmartGuide guide = new SmartGuide(fragment.requireActivity());
        guide.mFragment = fragment;
        return guide;
    }


    /**
     * layer层基础颜色
     */
    public SmartGuide initBaseColor(int color){
        mView.setBackgroundColor(color);
        return this;
    }


    /**
     * 新建一个引导
     * @return
     */
    public LayerCreator newLayer(){
        LayerCreator creator = LayerCreator.newCreator(mView,this,createTag());
        return creator;
    }

    /**
     * 新建一个引导
     * @return
     */
    public LayerCreator newLayer(@NonNull String tag){
        if(SmartUtils.strIsEmpty(tag)){
            tag = createTag();
        }
        LayerCreator creator = LayerCreator.newCreator(mView,this,tag);
        return creator;
    }



    public void show(){
        if(mActivity != null) {
            mView.build(mActivity);
        }else if(mFragment != null){
            mView.build(mFragment);
        }
    }



    public void clearLayers(){
        if(mView != null){
            mView.clearLayers();
        }
    }

    public void removeLayerByTag(String tag){
        if(mView != null){
            mView.removeLayerByTag(tag);
        }
    }


    public void dismiss(){
        mView.dismiss();
    }




    private String createTag(){
        StringBuilder builder = new StringBuilder();
        builder.append("layer_")
                .append(System.currentTimeMillis())
                .append((int)(Math.random()* 99999))
                .append("_")
                .append((int)(Math.random()* 99999));
        return builder.toString();
    }


    public interface ClipPositionBuilder<T extends BaseClipPosition>{
        T buildTarget();
    }

    public interface IntroPanelBuilder {
        IntroPanel buildFacePanel();
    }


    /**
     * 引导层点击事件监听器
     */
    public static abstract class OnGuidClickListener{
        /**
         * 引导层销毁回调
         */
        public void destroyed(){};

        /**
         * 点击蒙层非裁剪和信息区域回调，返回true，直接退出引导，返回false则不退出
         * @return
         */
        public abstract boolean emptyErrorClicked();

        /**
         * 引导镂空区域点击回调，如果镂空区域设置了事件透传，则不回调
         * @param guide
         * @param tag
         */
        public abstract void clipClicked(SmartGuide guide,GuidView view,String tag);

        /**
         * 引导介绍区域点击回调
         * @param guide
         * @param tag
         */
        public abstract void introClicked(SmartGuide guide,GuidView view,String tag);
    }


    public enum AlignY{
        ALIGN_TOP(0),
        ALIGN_BOTTOM(1);


        int align;

        AlignY(int align) {
            this.align = align;
        }
    }

    public enum AlignX{
        ALIGN_LEFT(0),
        ALIGN_RIGHT(1);


        int align;

        AlignX(int align) {
            this.align = align;
        }
    }
}
