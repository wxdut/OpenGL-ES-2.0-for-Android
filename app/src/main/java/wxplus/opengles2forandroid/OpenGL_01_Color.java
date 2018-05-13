package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Photo;
import wxplus.opengles2forandroid.programs.ColorBaseShaderProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_01_Color extends BaseActivity {

    protected float[] mProjectionMatrix = new float[16];

    protected ColorBaseShaderProgram mColorShaderProgram;
    protected Photo mColorPhoto;

    @Override
    public int layoutResId() {
        return R.layout.activity_01_color;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new CusRenderer();
    }

    public class CusRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mColorShaderProgram = new ColorBaseShaderProgram(mActivity);
            mColorPhoto = new Photo(1, 1, false);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            float rate = height * 1.0f / width;
            Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -rate, rate, -1, 1); // 正交变换，防止界面拉伸
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(1, 1, 1, 1);
            float rate = (++time) % 100 / 100.0f;
            mColorShaderProgram.bindData(mProjectionMatrix, mColorPhoto, 1 - rate, rate, rate * rate, rate); // 随便搞个效果
            mColorPhoto.draw();
        }

        int time;
    }

}
