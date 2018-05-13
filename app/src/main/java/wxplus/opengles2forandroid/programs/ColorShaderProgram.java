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

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

public class ColorShaderProgram extends BaseShaderProgram {

    private final int uColorHandle;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.color_vertex_shader,
                R.raw.color_fragment_shader);

        uColorHandle = glGetUniformLocation(program, U_COLOR);

    }


    public void bindData(float[] matrix, Object obj, float r, float g, float b, float a) {
        // 使用这个Program
        glUseProgram(program);
        // 矩阵变换
        float[] mvpMatrix = new float[16];
        Matrix.multiplyMM(mvpMatrix, 0, matrix, 0, obj.getModelMatrix(), 0);
        glUniformMatrix4fv(uMatrixHandle, 1, false, mvpMatrix, 0);
        // 设置颜色
        glUniform4f(uColorHandle, r, g, b, a);
        // 设置顶点数据
        glVertexAttribPointer(aPositionHandle, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionHandle);
    }
}
