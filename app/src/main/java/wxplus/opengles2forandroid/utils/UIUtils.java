package wxplus.opengles2forandroid.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * @author WangXiaoPlus
 * @date 2018/5/1
 */
public class UIUtils {
    /**
     * 获得屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕高度（像素）
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获得屏幕像素密度  DENSITY_DEFAULT = DENSITY_MEDIUM
     * DENSITY_LOW = 120
     * DENSITY_MEDIUM = 160
     * DENSITY_TV = 213
     * DENSITY_HIGH = 240
     * DENSITY_XHIGH = 320
     * DENSITY_400 = 400
     * DENSITY_XXHIGH = 480
     * DENSITY_XXXHIGH = 640
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获得屏幕分辨率
     */
    public static float getScreenDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float px) {
        return Math.round(px * getScreenDensity(context));
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, int px) {
        return px / getScreenDensity(context);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch(Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }
}
