package wxplus.opengles2forandroid;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.utils.ProjectionHelper;

/**
 * @author WangXiaoPlus
 * @date 2018/5/18
 */
public class OpenGL_06_Model extends BaseActivity {
    protected static final String TAG = "OpenGL_06_Model";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    protected ProjectionHelper mProjectionHelper;


    @Override
    public void init() {
        mProjectionHelper = new ProjectionHelper();
//        if (!nativeInit(getAssets())) {
//            throw new IllegalStateException(TAG + ", init, nativeInit() failed...");
//        }
    }

    @Override
    public int layoutResId() {
        return R.layout.activity_06_model;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                nativeInit(getAssets());
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                nativeOnSurfaceChanged(width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
//                nativeOnDrawFrame();
            }
        };
    }

    public native static boolean nativeInit(AssetManager assetManager);
    public native static void nativeOnSurfaceCreated(EGLConfig config);
    public native static void nativeOnSurfaceChanged(int width, int height);
    public native static void nativeOnDrawFrame();
}
