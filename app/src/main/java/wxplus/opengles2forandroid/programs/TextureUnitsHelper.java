package wxplus.opengles2forandroid.programs;

import java.util.Stack;

import wxplus.opengles2forandroid.utils.GLog;

/**
 * @author WangXiaoPlus
 * @date 2018/4/27
 */

public class TextureUnitsHelper {
    public static final String TAG = TextureUnitsHelper.class.getSimpleName();

    public static final int MIN_TEXTURE_UNITS_COUNT = 16;
    protected static Stack<Integer> sTextureUnits = new Stack<Integer>();

    static {
        for (int i = 1; i <= MIN_TEXTURE_UNITS_COUNT; i++) {
            sTextureUnits.push(Integer.valueOf(i));
        }
    }

    public static int obtain() {
        if (sTextureUnits.isEmpty()) {
            throw new RuntimeException(TAG + "obtain, no unit is idle...");
        }
        return sTextureUnits.pop();
    }

    public static boolean recycle(int texture) {
        if (texture <= 0 || texture > MIN_TEXTURE_UNITS_COUNT) {
            GLog.e(TAG, "recycle, texture is illegal, texture = " + texture);
            return false;
        }
        sTextureUnits.push(Integer.valueOf(texture));
        return true;
    }

}
