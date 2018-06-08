package wxplus.opengles2forandroid;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Environment;

import java.io.File;

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
        File file = new File("/storage/emulated/0/AA_S9/opengl_3d_models/sculpt.obj");
        System.out.println("AA "+ file.canRead());
        file = new File("/storage/emulated/0/AA_S9/opengl_3d_models/tex_1.jpg");
        System.out.println("AA "+ file.canRead());
        if (!nativeInit(getAssets())) {
            throw new IllegalStateException(TAG + ", init, nativeInit() failed...");
        }
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
                nativeOnSurfaceCreated(config);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                final int fov = 120;
                float screenAspect = width * 1.0f / height;
                Matrix.perspectiveM(mProjectionHelper.projectionMatrix, 0, fov, screenAspect, 1f, 10f);
                nativeOnSurfaceChanged(width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                nativeOnDrawFrame();
            }
        };
    }

    public native boolean nativeInit(AssetManager assetManager);
    public native void nativeOnSurfaceCreated(EGLConfig config);
    public native void nativeOnSurfaceChanged(int width, int height);
    public native void nativeOnDrawFrame();
}
