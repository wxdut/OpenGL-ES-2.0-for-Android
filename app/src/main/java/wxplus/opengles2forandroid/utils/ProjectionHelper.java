package wxplus.opengles2forandroid.utils;

import android.opengl.Matrix;

public class ProjectionHelper {

    public static final int PROJECTION_MATRIX_LENGTH = 16;

    public float[] modelMatrix;
    public float[] viewMatrix;
    public float[] projectionMatrix;

    public ProjectionHelper() {
        Matrix.setIdentityM(modelMatrix = new float[PROJECTION_MATRIX_LENGTH], 0);
        Matrix.setIdentityM(viewMatrix = new float[PROJECTION_MATRIX_LENGTH], 0);
        Matrix.setIdentityM(projectionMatrix = new float[PROJECTION_MATRIX_LENGTH], 0);
    }

    public float[] generateMvpMatrix() {
        float[] vpMatrix = new float[PROJECTION_MATRIX_LENGTH];
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        float[] mvpMatrix = new float[PROJECTION_MATRIX_LENGTH];
        Matrix.multiplyMM(mvpMatrix, 0,vpMatrix, 0, modelMatrix, 0);
        return mvpMatrix;
    }

    public float[] generateVpMatrix() {
        float[] vpMatrix = new float[PROJECTION_MATRIX_LENGTH];
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        return vpMatrix;
    }

    public float[] generateNormalMatrix() {
        float[] inverseMatrix = new float[PROJECTION_MATRIX_LENGTH];
        Matrix.invertM(inverseMatrix, 0, modelMatrix, 0);
        float[] transposeMatrix = new float[PROJECTION_MATRIX_LENGTH];
        Matrix.transposeM(transposeMatrix, 0, inverseMatrix, 0);
        return transposeMatrix;
    }

}
