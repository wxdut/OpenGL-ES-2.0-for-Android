package wxplus.opengles2forandroid.programs;

import android.content.Context;

import wxplus.opengles2forandroid.R;
import wxplus.opengles2forandroid.obj.Object;
import wxplus.opengles2forandroid.utils.ProjectionHelper;
import wxplus.opengles2forandroid.utils.TextureUtils;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

/**
 * @author WangXiaoPlus
 * @date 2018/5/3
 */
public class CubeShaderProgram extends ShaderProgram {

    protected final int uMatrixLocation;
    protected final int uTextureUnitLocation;
    protected final int aPositionLocation;

    public final int textureUnit;

    public CubeShaderProgram(Context context, int[] cubeImgList) {
        super(context, R.raw.cube_vertex_shader, R.raw.cube_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        textureUnit = TextureUtils.loadCubeMap(context, cubeImgList);
    }

    public void bindData(ProjectionHelper projectionHelper, Object obj) {
        glUseProgram(program);

        projectionHelper.modelMatrix = obj.getModelMatrix();
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionHelper.generateMvpMatrix(), 0);

        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureUnit);
        glUniform1i(uTextureUnitLocation, textureUnit);

        glVertexAttribPointer(aPositionLocation, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionLocation);
    }


}
