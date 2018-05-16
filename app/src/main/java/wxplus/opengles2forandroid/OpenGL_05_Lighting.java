package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Cube;
import wxplus.opengles2forandroid.obj.Photo;
import wxplus.opengles2forandroid.programs.ColorShaderProgram;
import wxplus.opengles2forandroid.programs.LightingShaderProgram;
import wxplus.opengles2forandroid.utils.ProjectionHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;

public class OpenGL_05_Lighting extends BaseActivity {

    protected float mAmbientLightFactor = 0.2f; // 环境光
    protected float[] mLightPosition; // 光源的位置

    protected ProjectionHelper mProjectionHelper;

    protected ColorShaderProgram mLightProgram;
    protected LightingShaderProgram mObjectProgram;
    protected Photo mLightPhoto;
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
                mObjectProgram = new LightingShaderProgram(mActivity, new int[]{
                        R.drawable.cube_first, R.drawable.cube_second, R.drawable.cube_third,
                        R.drawable.cube_fourth, R.drawable.cube_fifth, R.drawable.cube_sixth
                });
                mProjectionHelper = new ProjectionHelper();
                mObjectCube = new Cube(1);
                // 光源
                mLightProgram = new ColorShaderProgram(mActivity);
                mLightPhoto = new Photo(1.5f, 1.5f, true);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                final int fov = 120;
                float screenAspect = width * 1.0f / height;
                Matrix.perspectiveM(mProjectionHelper.projectionMatrix, 0, fov, screenAspect, 1f, 20f);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                glClearColor(0, 0, 0, 0);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glEnable(GL_DEPTH_TEST);
                mObjectCube.resetModelMatrix();
                mObjectCube.translate(0, 0, -4);
                mObjectCube.rotate(modelRotateDegrees, 1, 1, 0);
                mObjectProgram.bindData(mProjectionHelper, mObjectCube);
                mObjectCube.draw();
                modelRotateDegrees++;
                onDrawTime++;
                // 光源
                mLightPhoto.resetModelMatrix();
                mLightPhoto.translate(2, 4, -2);
//                mLightProgram.bindData(mProjectionHelper.generateVpMatrix(), mLightPhoto, 1, 1, 1, 1);
                mLightProgram.bindData(mProjectionHelper, mLightPhoto, 1, 1, 1, 1);
                mLightPhoto.draw();
            }

            int modelRotateDegrees = 0;
            int onDrawTime = 0;
        };
    }
}
