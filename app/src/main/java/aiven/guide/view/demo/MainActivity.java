package aiven.guide.view.demo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import aiven.guide.view.SmartGuide;
import aiven.guide.view.clip.CustomClip;
import aiven.guide.view.clip.ViewRectClip;
import aiven.guide.view.face.IntroPanel;
import aiven.guide.view.layer.GuidView;
import aiven.guide.view.util.MLog;
import aiven.guide.view.util.SmartUtils;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG_USER_HEADER = "userHeadImg";
    protected static final String TAG_MUSIC_IMG = "music_img";
    protected static final String TAG_IGG_SHAPE = "igg_shape";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.app_name);

        int[] pos = new int[2];
        findViewById(R.id.root).getLocationOnScreen(pos);
        MLog.E(null,"rootWindow:"+pos[1]);
    }

    public void userHeadClick(View view){
        Toast.makeText(getApplicationContext(),"进入个人中心",Toast.LENGTH_SHORT).show();
    }


    /**
     * 通过绝对定位添加
     * @param view
     */
    public void showAbsPosLayer(View view){
        //构建引导
        SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_USER_HEADER)
                //创建一个镂空区域
                .buildCustomClip(new SmartGuide.ClipPositionBuilder<CustomClip>() {
                    @Override
                    public CustomClip buildTarget() {
                        //构建镂空区域图形，支持CustomClip 和 ViewRectClip
                        return CustomClip.newClipPos()
                                //设置异形图片(实现见第三个按钮)
//                                .asIrregularShape(getApplicationContext(),R.mipmap.test_img)
                                .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT)//设置定位水平定位偏向
                                .setAlignY(SmartGuide.AlignY.ALIGN_TOP)//设置定位垂直定位偏向
                                .setEventPassThrough(true)//镂空区域是否事件穿透
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),14))//根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                                .setOffsetY(SmartUtils.getStatusBarHeight(getApplicationContext())+SmartUtils.dip2px(getApplicationContext(),4))
                                //设置镂空裁剪区域尺寸
                                .setClipSize(SmartUtils.dip2px(getApplicationContext(),48),SmartUtils.dip2px(getApplicationContext(),48))
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),24));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {//点击蒙层空白区域
                        return true;//返回true，引导消失，false不消失
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        //由于设置了setEventPassThrough 为true，所以这里这个方法不会回调
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        //点击文字区域
                        Toast.makeText(getApplicationContext(), "点击了右上角裁剪区域的说明图片引导", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    /**
     * 根据View 自身位置定位
     * @param view
     */
    public void showViewPosLayer(View view){
        //构建引导
        SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_MUSIC_IMG)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() {
                        return ViewRectClip.newClipPos()
                        .setDstView(R.id.text_pos)
                        .setPadding(SmartUtils.dip2px(getApplicationContext(),5))
                        .clipRadius(SmartUtils.dip2px(getApplicationContext(),51));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {
                        return true;
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了紫色音乐图标介绍图片区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    /**
     *
     * 显示不规则异形镂空引导
     * @param view
     */
    public void showIgg(View view){
        SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_IGG_SHAPE)
//                .buildCustomClip(xxx)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() {
                        return ViewRectClip.newClipPos()
                                .setDstView(R.id.text_pos2)
                                //设置异形图片
                                .asIrregularShape(getApplicationContext(),R.mipmap.test_img)
                                .setPadding(SmartUtils.dip2px(getApplicationContext(),10))
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),-5))
                                .setOffsetY(SmartUtils.dip2px(getApplicationContext(),-5));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {
                        return true;
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了不规则图形镂空区域", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了不规则图形图片说明区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    /**
     * 单屏显示多个layer
     * @param view
     */
    public void showMultLayer(View view){
        SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_USER_HEADER)
                //创建一个镂空区域
                .buildCustomClip(new SmartGuide.ClipPositionBuilder<CustomClip>() {
                    @Override
                    public CustomClip buildTarget() {
                        //构建镂空区域图形，支持CustomClip 和 ViewRectClip
                        return CustomClip.newClipPos()
                                .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT)//设置定位水平定位偏向
                                .setAlignY(SmartGuide.AlignY.ALIGN_TOP)//设置定位垂直定位偏向
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),14))//根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                                .setOffsetY(SmartUtils.getStatusBarHeight(getApplicationContext())+SmartUtils.dip2px(getApplicationContext(),4))
                                //设置镂空裁剪区域尺寸
                                .setClipSize(SmartUtils.dip2px(getApplicationContext(),48),SmartUtils.dip2px(getApplicationContext(),48))
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),24));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .over() //多个newLayer必须用over作为上一个newLayer的结束连接
                .newLayer(TAG_MUSIC_IMG)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() {
                        return ViewRectClip.newClipPos()
                                .setDstView(R.id.text_pos)
                                .setPadding(SmartUtils.dip2px(getApplicationContext(),5))
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),51));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {
                        return true;
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        if (TAG_USER_HEADER.equals(tag)) {
                            Toast.makeText(getApplicationContext(), "点击了左上角头像裁剪区域", Toast.LENGTH_SHORT).show();
                        }else if(TAG_MUSIC_IMG.equals(tag)){
                            Toast.makeText(getApplicationContext(), "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        if (TAG_USER_HEADER.equals(tag)) {
                            Toast.makeText(getApplicationContext(), "点击了左上角头像图片介绍区域", Toast.LENGTH_SHORT).show();
                        }else if(TAG_MUSIC_IMG.equals(tag)){
                            Toast.makeText(getApplicationContext(), "点击了紫色音乐图片介绍区域", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }


    /**
     * 多步骤切换展示
     * @param view
     */
    public void releaseTest(View view){
        SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_USER_HEADER)
                //创建一个镂空区域
                .buildCustomClip(new SmartGuide.ClipPositionBuilder<CustomClip>() {
                    @Override
                    public CustomClip buildTarget() {
                        //构建镂空区域图形，支持CustomClip 和 ViewRectClip
                        return CustomClip.newClipPos()
                                //设置异形图片(实现见第三个按钮)
//                                .asIrregularShape(getApplicationContext(),R.mipmap.test_img)
                                .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT)//设置定位水平定位偏向
                                .setAlignY(SmartGuide.AlignY.ALIGN_TOP)//设置定位垂直定位偏向
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),14))//根据水平定位偏向设置偏移，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                                .setOffsetY(SmartUtils.getStatusBarHeight(getApplicationContext())+SmartUtils.dip2px(getApplicationContext(),4))
                                //设置镂空裁剪区域尺寸
                                .setClipSize(SmartUtils.dip2px(getApplicationContext(),48),SmartUtils.dip2px(getApplicationContext(),48))
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),24));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {//点击蒙层空白区域
                        return false;//返回true，引导消失，false不消失
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        //由于设置了setEventPassThrough 为true，所以这里这个方法不会回调
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        showStep2(guide);
                    }
                })
                .show();
    }




    private void showStep2(SmartGuide guide){
        guide.clearLayers();
        guide.newLayer(TAG_MUSIC_IMG)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() {
                        return ViewRectClip.newClipPos()
                                .setDstView(R.id.text_pos)
                                .setPadding(SmartUtils.dip2px(getApplicationContext(),5))
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),51));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息
                                .setIntroBmp(R.mipmap.test_face)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {
                        return true;
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了紫色音乐图标裁剪区域", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        Toast.makeText(getApplicationContext(), "点击了紫色音乐图标介绍图片区域", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }



}