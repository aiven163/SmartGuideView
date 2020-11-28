package aiven.guide.view.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author Aiven (aiven163@aliyun.com)
 * @desc <p>日志输出工具</p>
 */
public class MLog {

    public static boolean IS_DEBUG = true;
    public static String DEFAULT_TAG = "SmartGuide>>>";

    public static void I(String msg){
        I(DEFAULT_TAG,msg);
    }

    public static void I(String tag,String msg){
        if(IS_DEBUG && !SmartUtils.strIsEmpty(msg)){
            if(SmartUtils.strIsEmpty(tag)){
                tag = DEFAULT_TAG;
            }
            Log.i(tag,msg);
        }
    }

    public static void V(String msg){
        V(DEFAULT_TAG,msg);
    }

    public static void V(String tag,String msg){
        if(IS_DEBUG && !SmartUtils.strIsEmpty(msg)){
            if(SmartUtils.strIsEmpty(tag)){
                tag = DEFAULT_TAG;
            }
            Log.v(tag,msg);
        }
    }

    public static void E(String tag,String msg){
        if(IS_DEBUG && !SmartUtils.strIsEmpty(msg)){
            if(SmartUtils.strIsEmpty(tag)){
                tag = DEFAULT_TAG;
            }
            Log.e(tag,msg);
        }
    }

    public static void E(Throwable throwable){
        if(throwable != null && IS_DEBUG){
            E(DEFAULT_TAG,saveException2String(throwable));
        }
    }


    private static String saveException2String(Throwable var0) {
        StringBuilder var1 = new StringBuilder("\n");
        try {
            var1.append("异常原因:").append(var0.toString()).append("\n");
            var1.append("具体如下:\n=======================================================\n");
            StringWriter var2 = new StringWriter();
            PrintWriter var3 = new PrintWriter(var2);
            var0.printStackTrace(var3);

            for (var0 = var0.getCause(); var0 != null; var0 = var0.getCause()) {
                var0.printStackTrace(var3);
            }
            var3.close();
            String var5 = var2.toString();
            var1.append(var5);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return var1.toString();
    }
}
