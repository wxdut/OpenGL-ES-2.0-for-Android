package wxplus.opengles2forandroid;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.obj.Cube;
import wxplus.opengles2forandroid.programs.CubeShaderProgram;
import wxplus.opengles2forandroid.utils.ProjectionHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;

public class OpenGL_04_Cube extends BaseActivity {

    protected SeekBar mSeekBar;

    @Override
    public int layoutResId() {
        return R.layout.activity_04_cube;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mSeekBar.setProgress(50);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float rate = (float) (progress / 50.0);
                    mCube.setScale(rate, rate, rate);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected CubeShaderProgram mCubeShaderProgram;
    protected ProjectionHelper mProjectionHelper;
    protected Cube mCube;

    @Override
    public GLSurfaceView.Renderer createGlViewRenderer() {
        return new GLSurfaceView.Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                mCubeShaderProgram = new CubeShaderProgram(mActivity, new int[]{
                        R.drawable.cube_first, R.drawable.cube_second, R.drawable.cube_third,
                        R.drawable.cube_fourth, R.drawable.cube_fifth, R.drawable.cube_sixth
                });
                mProjectionHelper = new ProjectionHelper();
                mCube = new Cube(1);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                glViewport(0, 0, width, height);
                final int fov = 90;
                float screenAspect = width * 1.0f / height;
                Matrix.perspectiveM(mProjectionHelper.projectionMatrix, 0, fov, screenAspect, 1f, 10f);
                Matrix.translateM(mProjectionHelper.viewMatrix, 0, 0, 0, -5);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                glClearColor(1f, 1f, 1f, 1f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                glEnable(GL_DEPTH_TEST);
                mCube.rotate(1, 1, 0, 1);
                mCubeShaderProgram.bindData(mProjectionHelper, mCube);
                mCube.draw();
            }
        };
    }
}
