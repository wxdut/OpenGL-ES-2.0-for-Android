package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Photo;
import wxplus.opengles2forandroid.programs.ColorShaderProgram;
import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_FLOAT;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_01_Color extends BaseActivity {

    protected float[] mProjectionMatrix = new float[16];

    protected ColorShaderProgram mColorShaderProgram;
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
            mColorShaderProgram = new ColorShaderProgram(mActivity);
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
            mColorShaderProgram.bindData(mProjectionMatrix, mColorPhoto, 0, 1, 0, 1);
            mColorPhoto.draw();
        }
    }

}
