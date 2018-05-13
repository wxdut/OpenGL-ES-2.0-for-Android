package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Photo;
import wxplus.opengles2forandroid.programs.TextureBaseShaderProgram;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_02_Texture extends BaseActivity {

    protected float[] mProjectionMatrix = new float[16];

    protected TextureBaseShaderProgram mTextureProgramBottom;
    protected TextureBaseShaderProgram mTextureProgramTop;

    protected Photo mBottomPhoto;
    protected Photo mTopPhoto;

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
            mTextureProgramBottom = new TextureBaseShaderProgram(mActivity, R.drawable.gl_02_texture_texture1);
            mTextureProgramTop = new TextureBaseShaderProgram(mActivity, R.drawable.gl_02_texture_texture2);
            mBottomPhoto = new Photo(2, 2, false);
            mTopPhoto = new Photo(0.5f, 0.5f, true);
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
                mTextureProgramBottom.bindData(mProjectionMatrix, mBottomPhoto);
                mBottomPhoto.draw();
            }
            if (mShowTopTextureView.isChecked()) {
                if (mRotateTopTextureView.isChecked()) {
                    mTopPhoto.rotate(2, 0, 0, 1);
                }
                mTextureProgramTop.bindData(mProjectionMatrix, mTopPhoto);
                mTopPhoto.draw();
            }
        }
    }


    protected CheckBox mShowBottomTextureView;
    protected CheckBox mShowTopTextureView;
    protected CheckBox mRotateTopTextureView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShowBottomTextureView = findViewById(R.id.cb1);
        mShowTopTextureView = findViewById(R.id.cb2);
        mRotateTopTextureView = findViewById(R.id.cb3);
        mShowBottomTextureView.setChecked(true);
        mShowTopTextureView.setChecked(true);
        mRotateTopTextureView.setChecked(true);
    }
}
