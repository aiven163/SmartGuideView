# 一个Android APP新手引导蒙层工具View

这里不做太多的描述，详细描述介绍到本人[简书文章](https://www.jianshu.com/p/61526f4e09d3) 查看，
附带几张截屏（GitHub不知道能显示出来不）

代码实例一用图</br>
<img src="https://upload-images.jianshu.io/upload_images/25308300-c19e4c084dcb101b.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp"  height="484" width="250">

代码实例二用图</br>
<img src="https://upload-images.jianshu.io/upload_images/25308300-9f08919576985b32.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp"  height="484" width="250">

代码实例三用图</br>
<img src="https://upload-images.jianshu.io/upload_images/25308300-97cec32c5f833595.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/1080/format/webp"  height="484" width="250">


当前版本：1.0.0，当前还处在最初版本，可能有bug

使用方式
    在需要引用的module 的gradle中
```    
      dependencies {
          implementation fileTree(include: ['*.jar'], dir: 'libs')
          implementation 'aiven.guide.view:library:1.0.0'
          //或者使用api
          // api 'aiven.guide.view:library:1.0.0'
      }
```

这个库不需要在Layout布局文件的xml中写代码，思路是直接通过当前activity的DecorView 添加和移除View的方式。当然已经分装了，用起来比较简单，只是有部分偏移需要根据自己的UI布局来计算一下。

* * *
### 具体代码用法 ###
直接通过上面三张图片，实现的代码进行注释说明吧，程序员的表达不好，谅解哈。

###### 实例一： 左上角用户个人中心引导  ######

要求：个人中心搬家到这里的指示引导图片可点击，点击后弹出一个Toast
        右上角个人中心图片原型镂空，切镂空区域点击事件不能拦截，点击后要直接进入个人中心，有个人中心入口图标响应事件
        这个实例通过根布局左上角绝对位置偏移实现，实例二中音乐图标会通过View自身相对位置实现。
 代码：
 ```
 SmartGuide.newGuide(this) //这里的this是activity 或者fragment。需要注意的是fragment必须要在显示出来后（attach之后）才能实例这个，因为归根接地是用的activity
                .initBaseColor(0X80000000)//设置引蒙层背景颜色，可以不设置，默认是50%的黑色半透明，当然也可以自己制定
                //新建一个引导
                .newLayer(TAG_USER_HEADER)  // TAG_USER_HEADER 是一个字符串，标识Layer的tag，后面在点击事件中用到，可以当做一个ID看待
                //创建一个镂空区域（也就是被裁剪挖空区域）
                .buildCustomClip(new SmartGuide.ClipPositionBuilder<CustomClip>() {
                    @Override
                    public CustomClip buildTarget() {//返回一个被挖空的镂空区域实例
                        //构建镂空区域图形，支持CustomClip 和 ViewRectClip，ViewRectClip 后面第二个实例紫色音乐图标实例说明
                        return CustomClip.newClipPos()
                                //设置异形图片(实现见第三个按钮)
                                //.asIrregularShape(getApplicationContext(),R.mipmap.test_img)   这里屏蔽了是因为在实例3中是实现，标识一个要挖空一个异形区域，由不规则图片区域决定
                                .setAlignX(SmartGuide.AlignX.ALIGN_RIGHT)//设置定位水平定位偏向（这里的偏向是屏幕根而言，类似于RelativeLayout的align,局屏幕顶部，左边、右边、下边 自己定义）
                                .setAlignY(SmartGuide.AlignY.ALIGN_TOP)//设置定位垂直定位偏向 （这里的偏向是屏幕根而言，类似于RelativeLayout的align）
                                .setEventPassThrough(true)//镂空区域是否事件穿透，上面说的需要个人中心图标自己响应事件，设置后后面的点击事件 clipClicked 将不再响应改Tag的事件
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),14))//根据水平定位偏向设置偏移，类似于margin，如果未ALIGN_LEFT,则是距离屏幕左侧偏移量，如果是ALIGN_RIGHT 则是距离屏幕右侧偏移量
                                .setOffsetY(SmartUtils.getStatusBarHeight(getApplicationContext())+SmartUtils.dip2px(getApplicationContext(),4)) //同offsetX基本相同的意思，只不过这个是垂直方向的
                                .setClipSize(SmartUtils.dip2px(getApplicationContext(),48),SmartUtils.dip2px(getApplicationContext(),48)) //设置镂空裁剪区域尺寸,这里一定要指定，否则没有
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),24));//设置镂空区域半径，默认是90度直角，也就是方形的，这里显示一个圆圈，所以半径是宽高的一半，也可以自己指定一个圆角值
                    }
                })
                //添加一个介绍图片(也就是图一中的个人中心搬家到这里的箭头白色文字图片)
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                .setIntroBmp(R.mipmap.test_face)//设置一个图片，这里就是刚说那张说明图片
                                //这里的Align 同CustomClip的Align有点点区别，这里的Align不再是界面根布局为标准，这里是clip的镂空区域，也就是局于镂空区域的，左边、上面、下面、右面，自己定义
                                //类似与RelativeLayout的相对布局toLeft、toRight、above、below
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_BOTTOM)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),151),SmartUtils.dip2px(getApplicationContext(),97)) //设置图片展示的尺寸
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-20),0);//设置水平和垂直方向的偏移值，这里要注意根据Align来计算，也相当于margin，不过是相对于clip镂空区域
                    }
                })
                //添加点击事件
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    @Override
                    public boolean emptyErrorClicked() {//点击蒙层中无裁剪和无镂空区域部分，部分需求是点击旁边无内容区域蒙层直接消失，所以这里增加了
                        return true;//返回true，引导消失，false不消失
                    }

                    @Override
                    public void clipClicked(SmartGuide guide, GuidView view, String tag) {
                        //由于设置了setEventPassThrough 为true，所以tag为TAG_USER_HEADER 的时候这里这个方法不会回调
                    }

                    @Override
                    public void introClicked(SmartGuide guide, GuidView view, String tag) {
                        //点击文字区域
                        Toast.makeText(getApplicationContext(), "点击了右上角裁剪区域的说明图片引导", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();  //最后不要忘记了调用show把蒙层显示出来；
 ```
 实例一的代码就完了，看上去代码有点多，但是都是流式的代码，看上去还是比较清晰，为了便于理解，对外的实例我基本上都直接采用流式API了。
 
 #####  我要显示多个怎么办？有没有退出函数？有没有蒙层消失回调？别急，后面都有说明  ####
 
 - - -
 ###### 实例二： 这个实例是显示多个Layer，同时介绍相对View本身的Clip  ######
 要求：可以显示多个Layer在蒙层上，音乐图标镂空区域根据图标自己定位和尺寸
 与上面带个Layer的不同点，主要注意有个over方法衔接两个Layer，over函数表示上一个Layer设置结束
 
 直接上代码
 ```
 SmartGuide.newGuide(this)  //这里的this是activity 或者fragment。需要注意的是fragment必须要在显示出来后（attach之后）才能实例这个，因为归根接地是用的activity
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_USER_HEADER)
                //创建一个镂空区域
                .buildCustomClip(new SmartGuide.ClipPositionBuilder<CustomClip>() {
                    @Override
                    public CustomClip buildTarget() {
                        //这里代码我省略了，为了篇幅，通上面实例1的代码一致，唯一不同是没有设置setEventPassThrough(true) 这句。
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                         //这里代码我省略了，为了篇幅，通上面实例1的代码一致
                    }
                })
                .over() //多个newLayer必须用over作为上一个newLayer的结束连接，用于衔接下面一个newLayer
                .newLayer(TAG_MUSIC_IMG)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() { //构建一个View自身挖空区域
                        return ViewRectClip.newClipPos()
                                .setDstView(R.id.text_pos) //指定一个目标View的Id,或者直接传一个view。当然这个view一定要是前面实例化是this 这个当前的activity或者fragment中的view
                                .setPadding(SmartUtils.dip2px(getApplicationContext(),5)) //由于由View自身决定尺寸和位置，所以可能刚好和view的边缘重叠，为了扩展大一点，所以设置一定d的padding的a
                                .clipRadius(SmartUtils.dip2px(getApplicationContext(),51));//设置挖空区域形状半径，默认为0，是一个矩形
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                        return IntroPanel.newIntroPanel(getApplicationContext())
                                //设置介绍图片与clipInfo的对齐信息，这里也不再过多解释，通上面的一个理解一样，只是参数不同
                                .setIntroBmp(R.mipmap.test_face_music)
                                .setAlign(SmartGuide.AlignX.ALIGN_LEFT,SmartGuide.AlignY.ALIGN_TOP)
                                .setSize(SmartUtils.dip2px(getApplicationContext(),120),SmartUtils.dip2px(getApplicationContext(),120))
                                .setOffset(SmartUtils.dip2px(getApplicationContext(),-100),0);
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
 ```
 实例二代码完了，主要注意一下over方法的使用地方和含义
 
 - - -
 ###### 实例三： 界面上有个异形的图片区域需要镂空  ######
 要求：根据界面上一个ImageView上展示的图片来显示挖空区域
 这个麻烦点在，是一个不规则的形状，之前都是一个矩形，或者矩形设置半径变为圆角矩形或者圆形。
 api上封装了，比较简单,主要是asIrregularShape 函数，上代码
 ```
 SmartGuide.newGuide(this)
                .initBaseColor(0X80000000)//设置引蒙层背景颜色
                //新建一个引导
                .newLayer(TAG_IGG_SHAPE)
                //创建一个镂空区域
                .buildViewRectClip(new SmartGuide.ClipPositionBuilder<ViewRectClip>() {
                    @Override
                    public ViewRectClip buildTarget() {
                        return ViewRectClip.newClipPos()
                                .setDstView(R.id.text_pos2) //这个和实例二一样的
                                //设置异形图片
                                .asIrregularShape(getApplicationContext(),R.mipmap.test_img) //重要方法，设置一个不规则图片，标识挖空区域，CustomClip 也支持的，这里就不多说了
                                .setPadding(SmartUtils.dip2px(getApplicationContext(),10))
                                .setOffsetX(SmartUtils.dip2px(getApplicationContext(),-5))
                                .setOffsetY(SmartUtils.dip2px(getApplicationContext(),-5));
                    }
                })
                .buildIntroPanel(new SmartGuide.IntroPanelBuilder() {
                    @Override
                    public IntroPanel buildFacePanel() {
                       //为了篇幅，这里代码省略了
                    }
                })
                .setOnGuidClickListener(new SmartGuide.OnGuidClickListener() {
                    //为了篇幅，这里代码省略了
                })
                .show();
 
 ```
 - - -
 三个实例都说完了，说几个没有介绍的方法：
 
 SmartGuide.clearLayers()  清空所有蒙层中的Layer，几乎不会用到
 SmartGuide.removeLayerByTag(String tag) 根据tag 移除某个Layer，当多个引导指示跳转的时候会用到
 SmartGuide.dismiss()  直接退出用户引导蒙层
 
 另外OnGuidClickListener 是抽象类，不是接口，里面可以复现方法，监听引导蒙层的退出 
 destroyed() 方法，当蒙层退出时会调用
 
 ## 完了，谢谢！ ##
 有bug或者有什么建议欢迎大家直接邮件我 aiven163@aliyun.com
 
 
