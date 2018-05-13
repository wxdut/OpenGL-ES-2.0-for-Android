package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Puck;
import wxplus.opengles2forandroid.obj.Table;
import wxplus.opengles2forandroid.obj.Mallet;
import wxplus.opengles2forandroid.programs.ColorShaderProgram;
import wxplus.opengles2forandroid.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_03_Hockey extends BaseActivity {


    protected static final int sFovy = 90; // 透视投影的视角，90度
    protected static final float sZ = 2;

    protected float[] mViewMatrix = new float[16];
    protected float[] mProjectionMatrix = new float[16];
    protected float[] mProjectionViewMatrix = new float[16];

    protected TextureShaderProgram mTextureProgram;
    protected ColorShaderProgram mColorProgram;

    protected Table mTable;
    protected Puck mPuck;
    protected Mallet mTopMallet;
    protected Mallet mBottomMallet;

    @Override
    public int layoutResId() {
        return R.layout.activity_03_hockey;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new CusRenderer();
    }

    public class CusRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mTextureProgram = new TextureShaderProgram(mActivity, R.drawable.air_hockey_surface);
            mColorProgram = new ColorShaderProgram(mActivity);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // Set the OpenGL viewport to fill the entire surface.
            glViewport(0, 0, width, height);
            float focalLength = (float) (1 / Math.tan(Math.toRadians(sFovy / 2)));
            float screenAspect = width * 1.0f / height;
            Matrix.perspectiveM(mProjectionMatrix, 0, sFovy, screenAspect, 1f, 10f);
            setIdentityM(mViewMatrix, 0);
            translateM(mViewMatrix, 0, 0, 0, -sZ);
            rotateM(mViewMatrix, 0, -60, 1f, 0f, 0f);
            // Multiply the view and projection matrices together.
            multiplyMM(mProjectionViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
            // 初始化Objects
            mTable = new Table(sZ * screenAspect / focalLength, sZ / focalLength);
            mPuck = new Puck(0.1f, 0.1f);
            mTopMallet = new Mallet(0.05f, 0.1f, 0.1f, 0.05f);
            mBottomMallet = new Mallet(0.05f, 0.1f, 0.1f, 0.05f);

            mPuck.translate(0, 0, 0.05f);
            mTopMallet.translate(0, 0.5f, 0.05f);
            mBottomMallet.translate(0, -0.5f, 0.05f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            // 绘制Table
            mTextureProgram.bindData(mProjectionViewMatrix, mTable);
            mTable.draw();
            // 绘制Puck
            mColorProgram.bindData(mProjectionViewMatrix, mPuck, 1f, 0f, 0f, 1f);
            mPuck.draw();
            // top mallet
            mColorProgram.bindData(mProjectionViewMatrix, mTopMallet, 0, 1f, 0, 1f);
            mTopMallet.draw();
            // bottom mallet
            mColorProgram.bindData(mProjectionViewMatrix, mBottomMallet, 0, 0, 1f, 1f);
            mBottomMallet.draw();
        }
    }

}
