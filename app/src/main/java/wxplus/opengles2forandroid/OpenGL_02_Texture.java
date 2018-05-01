package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Table;
import wxplus.opengles2forandroid.obj.base.Point;
import wxplus.opengles2forandroid.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_02_Texture extends BaseActivity {

    protected float[] mProjectionMatrix = new float[16];

    protected TextureShaderProgram mTextureProgramBottom;
    protected TextureShaderProgram mTextureProgramTop;

    protected Table mBottomTable;
    protected Table mTopTable;

    @Override
    public int layoutResId() {
        return R.layout.activity_02_texture;
    }

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new CusRenderer();
    }

    public class  CusRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mTextureProgramBottom = new TextureShaderProgram(mActivity, R.drawable.gl_02_texture_texture1);
            mTextureProgramTop = new TextureShaderProgram(mActivity, R.drawable.gl_02_texture_texture2);
            mBottomTable = new Table(new Point(0, 0, 0), 1, 1);
            mTopTable = new Table(new Point(0, 0, 0), 0.5f, 0.5f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            // 正交投影
            float rate = (float) height / width;
            orthoM(mProjectionMatrix, 0, -1, 1, -rate, rate, -1, 1);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClearColor(1f, 1f, 1f, 1f);
            glClear(GL_COLOR_BUFFER_BIT);
            if (mShowBottomTextureView.isChecked()) {
                glBindTexture(GL_TEXTURE_2D, mTextureProgramBottom.textureUnit);
                mTextureProgramBottom.bindData(mProjectionMatrix, mBottomTable);
                mBottomTable.draw();
            }
            if (mShowTopTextureView.isChecked()) {
                glBindTexture(GL_TEXTURE_2D, mTextureProgramTop.textureUnit);
                mTextureProgramTop.bindData(mProjectionMatrix, mTopTable);
                mTopTable.draw();
            }
        }
    }


    protected CheckBox mShowBottomTextureView;
    protected CheckBox mShowTopTextureView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShowBottomTextureView = findViewById(R.id.cb1);
        mShowTopTextureView = findViewById(R.id.cb2);
        mShowBottomTextureView.setChecked(true);
    }
}
