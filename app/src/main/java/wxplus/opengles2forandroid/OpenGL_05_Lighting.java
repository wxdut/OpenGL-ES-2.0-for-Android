package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Cube;
import wxplus.opengles2forandroid.programs.CubeShaderProgram;

public class OpenGL_05_Lighting extends BaseActivity {

    protected float mAmbientLightFactor = 0.2f; // 环境光
    protected float[] mLightPosition; // 光源的位置

    protected CubeShaderProgram mLightProgram;
    protected CubeShaderProgram mObjectProgram;
    protected Cube mLightCube;
    protected Cube mObjectCube;

    @Override
    public int layoutResId() {
        return R.layout.activity_05_lighting;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

            }

            @Override
            public void onDrawFrame(GL10 gl) {

            }
        };
    }
}
