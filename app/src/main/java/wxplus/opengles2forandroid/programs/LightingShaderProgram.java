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
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;

/**
 * @author WangXiaoPlus
 * @date 2018/5/13
 */
public class LightingShaderProgram extends BaseShaderProgram {

    public final int textureUnit;

    public LightingShaderProgram(Context context, int[] cubeImgList) {
        super(context, R.raw.lighting_vertex_shader, R.raw.lighting_fragment_shader);

        textureUnit = TextureUtils.loadCubeMap(context, cubeImgList);
    }

    public void bindData(ProjectionHelper projectionHelper, Object obj) {
        glUseProgram(program);

        projectionHelper.modelMatrix = obj.getModelMatrix();
        glUniformMatrix4fv(uMatrixHandle, 1, false, projectionHelper.generateMvpMatrix(), 0);

        glActiveTexture(GL_TEXTURE0 + textureUnit);
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureUnit);
        glUniform1i(uTextureUnitHandle, textureUnit);

        glUniform3fv(uLightColorHandle, 1, new float[]{1, 1, 1}, 0);
        glUniform3fv(uLightPositionHandle, 1, new float[]{2, 4, -2}, 0);
        glUniform3fv(uViewPositionHandle, 1, new float[]{0, 0, 0}, 0);

        glVertexAttribPointer(aPositionHandle, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getVertexBuffer());
        glEnableVertexAttribArray(aPositionHandle);

        glVertexAttribPointer(aNormalHandle, FLOATS_PER_VERTEX, GL_FLOAT, false, 0, obj.getNormalBuffer());
        glEnableVertexAttribArray(aNormalHandle);

    }

}
