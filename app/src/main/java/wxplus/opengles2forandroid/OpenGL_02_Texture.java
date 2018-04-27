package wxplus.opengles2forandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLUtils.texImage2D;
import static android.opengl.Matrix.orthoM;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_FLOAT;

/**
 * Created by hi on 2017/10/29.
 */

public class OpenGL_02_Texture extends BaseActivity {

    protected float[] mVertexArray = new float[] { // OpenGL的坐标是[-1, 1]，这里的Vertex正好定义了一个居中的正方形
            // Triangle Fan x, y
            0f,    0f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f,  0.5f,
            -0.5f,  0.5f,
            -0.5f, -0.5f
    };
    protected float[] mTextureArray = new float[] {
            0.5f, 0.5f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f
    };
    protected FloatBuffer mVertexBuffer;
    protected FloatBuffer mTextureBuffer;
    protected float[] mProjectionMatrix = new float[16];

    protected int textureId1;
    protected int textureId2;

    protected int uMatrixLocation;
    protected int aPositionLocation;
    protected int aTextureCoordinatesLocation;
    protected int uTextureUnit1;
    protected int uTextureUnit2;

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
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            // 初始化顶点数据
            mVertexBuffer = ByteBuffer.allocateDirect(mVertexArray.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mVertexArray);
            mTextureBuffer = ByteBuffer.allocateDirect(mTextureArray.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mTextureArray);
            String vertexShaderStr = TextureUtils.readShaderCodeFromResource(mActivity, R.raw.texture_vertex_shader);
            String fragmentShaderStr = TextureUtils.readShaderCodeFromResource(mActivity, R.raw.texture_fragment_shader);
            // 创建Shader
            final int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
            final int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(vertexShaderId, vertexShaderStr);
            glShaderSource(fragmentShaderId, fragmentShaderStr);
            glCompileShader(vertexShaderId);
            glCompileShader(fragmentShaderId);
            final int[] compileStatus = new int[1];
            glGetShaderiv(fragmentShaderId, GL_COMPILE_STATUS,
                    compileStatus, 0);
            // 创建Program
            final int programId = glCreateProgram();
            glAttachShader(programId, vertexShaderId);
            glAttachShader(programId, fragmentShaderId);
            glLinkProgram(programId);
            // 启用这个Program
            glUseProgram(programId);
            // 找到需要赋值的变量
            uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
            aPositionLocation = glGetAttribLocation(programId, "a_Position");
            aTextureCoordinatesLocation = glGetAttribLocation(programId, "a_TextureCoordinates");
            uTextureUnit1 = glGetUniformLocation(programId, "u_TextureUnit1");
            uTextureUnit2 = glGetUniformLocation(programId, "u_TextureUnit2");

            // 初始化Texture
            int[] textureObjectIds = new int[2];
            glGenTextures(2, textureObjectIds, 0);
            textureId1 = textureObjectIds[0];
            textureId2 = textureObjectIds[1];
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeResource(
                    getResources(), R.drawable.gl_02_texture_texture1, options);
            glBindTexture(GL_TEXTURE_2D, textureId1);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
            glGenerateMipmap(GL_TEXTURE_2D);
            bitmap.recycle();
            // 使用该Texture
            glUniform1i(uTextureUnit1, 0);

            bitmap = BitmapFactory.decodeResource(
                    getResources(), R.drawable.gl_02_texture_texture2, options);
            glBindTexture(GL_TEXTURE_2D, textureId2);
            texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
            glGenerateMipmap(GL_TEXTURE_2D);
            bitmap.recycle();
            glUniform1i(uTextureUnit2, 1);

            // 填充数据
            mVertexBuffer.position(0);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, mVertexBuffer);
            glEnableVertexAttribArray(aPositionLocation);
            mTextureBuffer.position(0);
            glVertexAttribPointer(aTextureCoordinatesLocation, 2, GL_FLOAT, false, 0, mTextureBuffer);
            glEnableVertexAttribArray(aTextureCoordinatesLocation);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            // 正交投影
            float rate = (float) height / width;
            orthoM(mProjectionMatrix, 0, -1, 1, -rate, rate, -1, 1);
            // 赋值
            glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClearColor(1f, 1f, 1f, 1f);
            glClear(GL_COLOR_BUFFER_BIT);
            glBindTexture(GL_TEXTURE_2D, 0);
            if (mShowBottomTextureView.isChecked()) {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, textureId1);
            }
            if (mShowTopTextureView.isChecked()) {
                glActiveTexture(GL_TEXTURE1);
                glBindTexture(GL_TEXTURE_2D, textureId2);
            }
            glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
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
