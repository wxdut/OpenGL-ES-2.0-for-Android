package wxplus.opengles2forandroid.programs;

import java.util.LinkedList;
import java.util.List;

import wxplus.opengles2forandroid.utils.GLog;

import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;

/**
 * @author WangXiaoPlus
 * @date 2018/4/27
 */

public class TextureUnitsHelper {
    public static final String TAG = TextureUnitsHelper.class.getSimpleName();

    protected static List<Integer> sTextureUnits = new LinkedList<>();

    public static int obtain() {
        final int[] texture = new int[1];
        glGenTextures(1, texture, 0);
        if (texture[0] == 0) {
            GLog.e(TAG, "obtain, Could not generate a new OpenGL texture object.");
            return 0;
        }
        sTextureUnits.add(texture[0]);
        return texture[0];
    }

    public static void recycle(int texture) {
        glDeleteTextures(1, new int[texture], 0);
        sTextureUnits.remove(Integer.valueOf(texture));
    }

}
