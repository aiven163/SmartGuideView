package aiven.guide.view.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;


/**
 * @author Aiven (aiven163@aliyun.com)
 * <p>工具方法集合</p>
 */
public class SmartUtils {
    /**
     * 获取屏幕信息,包含状态栏
     * @param activity
     * @return
     */
    public static Point getScreenSize(@NonNull Activity activity) {
        if (activity != null) {
            DisplayMetrics dm = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            } else {
                dm = activity.getResources().getDisplayMetrics();
            }
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            return new Point(screenWidth, screenHeight);
        }
        return new Point(0, 0);
    }

    /**
     * 获取屏幕信息,包含状态栏
     * @param context
     * @return
     */
    public static int getScreenWidth(@NonNull Context context) {
        if (context != null) {
            DisplayMetrics dm = new DisplayMetrics();
            dm = context.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            return screenWidth;
        }
        return 0;
    }

    public static int getScreenHeight(@NonNull Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        try {
            // 获得状态栏高度
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            MLog.E(e);
            return dip2px(context, 24);
        }
    }




    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 检查字符串是否为空
     * @param value
     * @return
     */
    public static boolean strIsEmpty(String value){
        if(value != null && value.trim().length() > 0){
            return false;
        }
        return true;
    }
}
