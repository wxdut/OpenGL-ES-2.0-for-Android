/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package wxplus.opengles2forandroid.programs;

import android.content.Context;
import android.opengl.Matrix;

import wxplus.opengles2forandroid.R;
import wxplus.opengles2forandroid.obj.Object;
import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_TEXTURE_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

public class TextureBaseShaderProgram extends BaseShaderProgram {

    public final int textureUnit;

    // Uniform locations
    protected final int uMatrixHandle;
    protected final int uTextureUnitHandle;

    // Attribute locations
    protected final int aPositionHandle;
    protected final int aTextureCoordinatesHandle;

    public TextureBaseShaderProgram(Context context, int resId) {
        super(context, R.raw.texture_vertex_shader,
            R.raw.texture_fragment_shader);

        textureUnit = TextureUtils.loadTexture(context, resId);

        // Retrieve uniform locations for the shader program.
        uMatrixHandle = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitHandle = glGetUniformLocation(program,
            U_TEXTURE_UNIT);
        
        // Retrieve attribute locations for the shader program.
        aPositionHandle = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesHandle = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void bindData(float[] matrix, Object obj) {
        // use Program
        glUseProgram(program);
        // Pass the matrix into the shader program.
        float[] mvpMatrix = new float[16];
        Matrix.multiplyMM(mvpMatrix, 0, matrix, 0, obj.getModelMatrix(), 0);
        glUniformMatrix4fv(uMatrixHandle, 1, false, mvpMatrix, 0);
        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0 + textureUnit);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureUnit);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitHandle, textureUnit);
        // 设置顶点数据
        glVertexAttribPointer(aPositionHandle, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionHandle);
        // 设置Texture数据
        glVertexAttribPointer(aTextureCoordinatesHandle, FLOATS_PER_TEXTURE_VERTEX, GL_FLOAT, false, 0, obj.getTextureBuffer());
        glEnableVertexAttribArray(aTextureCoordinatesHandle);
    }
}