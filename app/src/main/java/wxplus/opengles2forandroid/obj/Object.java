package wxplus.opengles2forandroid.obj;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import wxplus.opengles2forandroid.obj.geometry.Circle;
import wxplus.opengles2forandroid.obj.geometry.Cylinder;
import wxplus.opengles2forandroid.obj.geometry.Square;
import wxplus.opengles2forandroid.utils.GlobalConfig;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_FLOAT;
import static wxplus.opengles2forandroid.utils.Constants.BYTES_PER_SHORT;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_TEXTURE_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.VERTEX_COUNT_SQUARE;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 图形的基类，提供共有方法
 */

public abstract class Object {
    public static final String TAG = Object.class.getSimpleName();

    protected static final int CIRCLE_VERTICES_COUNT = 100; // 圆形周边默认的顶点数
    protected static final int DEFAULT_VERTICES_COUNT = 4; // Object默认的顶点数量

    protected float[] mModelMatrix = new float[16]; // Model变换矩阵

    protected FloatBuffer mVertexBuffer;
    protected float[] mVertexData;
    protected int offsetVertexData = 0;
    protected final float[] DEFAULT_CUBE_VERTEX_DATA = new float[]{
            -1, 1, 1,     // (0) Top-left near
            1, 1, 1,     // (1) Top-right near
            -1, -1, 1,     // (2) Bottom-left near
            1, -1, 1,     // (3) Bottom-right near
            -1, 1, -1,     // (4) Top-left far
            1, 1, -1,     // (5) Top-right far
            -1, -1, -1,     // (6) Bottom-left far
            1, -1, -1      // (7) Bottom-right far
    };

    protected ShortBuffer mIndexBuffer;
    protected short[] mIndexData;
    public static final short[] DEFAULT_CUBE_INDEX_DATA = new short[]{
            // Front
            1, 3, 0,
            0, 3, 2,
            // Back
            4, 6, 5,
            5, 6, 7,
            // Left
            0, 2, 4,
            4, 2, 6,
            // Right
            5, 7, 1,
            1, 7, 3,
            // Top
            5, 1, 4,
            4, 1, 0,
            // Bottom
            6, 2, 7,
            7, 2, 3
    };

    protected FloatBuffer mNormalBuffer;
    protected float[] mNormalData;
    protected int offsetNormalData = 0;
    protected final float[] DEFAULT_CUBE_NORMAL_DATA = new float[] {
            // Front
            0, 0, 1,    0, 0, 1,    0, 0, 1,
            0, 0, 1,    0, 0, 1,    0, 0, 1,
            // Back
            0, 0, -1,   0, 0, -1,   0, 0, -1,
            0, 0, -1,   0, 0, -1,   0, 0, -1,
            // Left
            -1, 0, 0,   -1, 0, 0,   -1, 0, 0,
            -1, 0, 0,   -1, 0, 0,   -1, 0, 0,
            // Right
            1, 0, 0,    1, 0, 0,    1, 0, 0,
            1, 0, 0,    1, 0, 0,    1, 0, 0,
            // Top
            0, 1, 0,    0, 1, 0,    0, 1, 0,
            0, 1, 0,    0, 1, 0,    0, 1, 0,
            // Bottom
            0, -1, 0,   0, -1, 0,   0, -1, 0,
            0, -1, 0,   0, -1, 0,   0, -1, 0
    };

    protected FloatBuffer mTextureBuffer;
    protected float[] mTextureData;
    protected int offsetTextureData = 0;
    protected static final float[] DEFAULT_TEXTURE_DATA = new float[]{
            0.5f, 0.5f,
            0f, 0f,
            1f, 0f,
            1f, 1f,
            0f, 1f,
            0f, 0f
    };

    public Object() {
        int count = verticesCount();
        if (count <= 0) {
            count = DEFAULT_VERTICES_COUNT;
        }
        mVertexData = new float[floatSizeOfVertices(count)];
        mTextureData = new float[floatSizeOfTextureVertices(count)];
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    protected List<DrawTask> drawTaskList = new ArrayList<>();

    public Object addCube(float sideWidth) {
        // Create a unit cube.
        mVertexData = DEFAULT_CUBE_VERTEX_DATA;
        for (int i = 0; i < mVertexData.length; i++) {
            mVertexData[i] /= Math.abs(mVertexData[i]);
            mVertexData[i] *= sideWidth;
        }
        mNormalData = DEFAULT_CUBE_NORMAL_DATA;
        // 6 indices per cube side
        mIndexData = DEFAULT_CUBE_INDEX_DATA;
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, getIndexBuffer());
            }
        });
        return this;
    }

    public Object addSquare(Square square) {
        final int startVertex = offsetVertexData / FLOATS_PER_VERTEX;
        final int vertexCount = VERTEX_COUNT_SQUARE;
        if (mVertexData == null || mVertexData.length - offsetVertexData < vertexCount * FLOATS_PER_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addSquare, mVertexData is not big enough");
            }
            return this;
        }
        if (mTextureData == null || mTextureData.length - offsetTextureData < vertexCount * FLOATS_PER_TEXTURE_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addSquare, mTextureData is not big enough");
            }
            return this;
        }
        // 先确定正方形中心的坐标
        mVertexData[offsetVertexData++] = square.center.x;
        mVertexData[offsetVertexData++] = square.center.y;
        mVertexData[offsetVertexData++] = square.center.z;
        // 左下角
        mVertexData[offsetVertexData++] = square.center.x - square.width / 2;
        mVertexData[offsetVertexData++] = square.center.y - square.height / 2;
        mVertexData[offsetVertexData++] = square.center.z;
        // 右下角
        mVertexData[offsetVertexData++] = square.center.x + square.width / 2;
        mVertexData[offsetVertexData++] = square.center.y - square.height / 2;
        mVertexData[offsetVertexData++] = square.center.z;
        // 右上角
        mVertexData[offsetVertexData++] = square.center.x + square.width / 2;
        mVertexData[offsetVertexData++] = square.center.y + square.height / 2;
        mVertexData[offsetVertexData++] = square.center.z;
        // 左上角
        mVertexData[offsetVertexData++] = square.center.x - square.width / 2;
        mVertexData[offsetVertexData++] = square.center.y + square.height / 2;
        mVertexData[offsetVertexData++] = square.center.z;
        // 左下角(triangle fan)
        mVertexData[offsetVertexData++] = square.center.x - square.width / 2;
        mVertexData[offsetVertexData++] = square.center.y - square.height / 2;
        mVertexData[offsetVertexData++] = square.center.z;

        mTextureData = DEFAULT_TEXTURE_DATA;

        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, vertexCount);
            }
        });
        return this;
    }

    public Object addCircle(Circle circle, int pointCount) {
        final int startVertex = offsetVertexData / FLOATS_PER_VERTEX;
        final int vertexCount = sizeOfCircleInVertex(pointCount);
        if (mVertexData == null || mVertexData.length - offsetVertexData < vertexCount * FLOATS_PER_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addCircle, mVertexData is not big enough");
            }
            return this;
        }
        if (mTextureData == null || mTextureData.length - offsetTextureData < vertexCount * FLOATS_PER_TEXTURE_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addCircle, mTextureData is not big enough");
            }
            return this;
        }
        // 先确定圆心的坐标
        mVertexData[offsetVertexData++] = circle.center.x;
        mVertexData[offsetVertexData++] = circle.center.y;
        mVertexData[offsetVertexData++] = circle.center.z;
        mTextureData[offsetTextureData++] = 0.5f;
        mTextureData[offsetTextureData++] = 0.5f;
        // 循环赋值，确定圆上顶点的坐标(最后一个顶点赋值两次)
        final float radian = (float) (2 * Math.PI / pointCount); // 先计算出每一份的弧度值
        for (int i = 0; i <= pointCount; i++) {
            final float x = circle.center.x + circle.radius * (float) Math.cos(radian * i);
            final float y = circle.center.y + circle.radius * (float) Math.sin(radian * i);
            final float z = circle.center.z;
            // vertex 赋值
            mVertexData[offsetVertexData++] = x;
            mVertexData[offsetVertexData++] = y;
            mVertexData[offsetVertexData++] = z;
            mTextureData[offsetTextureData++] = (x + circle.radius) / (2 * circle.radius);
            mTextureData[offsetTextureData++] = (y + circle.radius) / (2 * circle.radius);
        }
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, vertexCount);
            }
        });
        return this;
    }

    public Object addOpenCylinder(Cylinder cylinder, int pointCount) {
        final int startVertex = offsetVertexData / FLOATS_PER_VERTEX;
        final int vertexCount = sizeOfCylinderInVertex(pointCount);
        if (mVertexData == null || mVertexData.length - offsetVertexData < vertexCount * FLOATS_PER_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addOpenCylinder, mVertexData is not big enough");
            }
            return this;
        }
        if (mTextureData == null || mTextureData.length - offsetTextureData < vertexCount * FLOATS_PER_TEXTURE_VERTEX) {
            if (GlobalConfig.DEBUG) {
                throw new IndexOutOfBoundsException(TAG + ", addOpenCylinder, mTextureData is not big enough");
            }
            return this;
        }
        float topZ = cylinder.center.z + cylinder.height / 2;
        float bottomZ = cylinder.center.z - cylinder.height / 2;
        float radian = (float) (2 * Math.PI / pointCount); // 先计算出每一份的弧度值
        // 依次赋值
        for (int i = 0; i <= pointCount; i++) {
            float x = cylinder.center.x + cylinder.radius * (float) Math.cos(i + radian);
            float y = cylinder.center.y + cylinder.radius * (float) Math.sin(i + radian);
            // top
            mVertexData[offsetVertexData++] = x;
            mVertexData[offsetVertexData++] = y;
            mVertexData[offsetVertexData++] = topZ;
            // bottom
            mVertexData[offsetVertexData++] = x;
            mVertexData[offsetVertexData++] = y;
            mVertexData[offsetVertexData++] = bottomZ;
        }
        drawTaskList.add(new DrawTask() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, vertexCount);
            }
        });
        return this;
    }

    public FloatBuffer getVertexBuffer() {
        if (mVertexBuffer == null) {
            mVertexBuffer = ByteBuffer
                    .allocateDirect(mVertexData.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mVertexData);
        }
        mVertexBuffer.position(0);
        return mVertexBuffer;
    }

    public FloatBuffer getTextureBuffer() {
        if (mTextureBuffer == null) {
            mTextureBuffer = ByteBuffer
                    .allocateDirect(mTextureData.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mTextureData);
        }
        mTextureBuffer.position(0);
        return mTextureBuffer;
    }

    public ShortBuffer getIndexBuffer() {
        if (mIndexBuffer == null) {
            mIndexBuffer = ByteBuffer
                    .allocateDirect(mIndexData.length * BYTES_PER_SHORT)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .put(mIndexData);
        }
        mIndexBuffer.position(0);
        return mIndexBuffer;
    }

    public FloatBuffer getNormalBuffer() {
        if (mNormalBuffer == null) {
            mNormalBuffer = ByteBuffer
                    .allocateDirect(mNormalData.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mNormalData);
        }
        mNormalBuffer.position(0);
        return mNormalBuffer;
    }

    public void draw() {
        for (int i = 0; i < drawTaskList.size(); i++) {
            drawTaskList.get(i).draw();
        }
    }

    public int sizeOfCircleInVertex(int pointCount) {
        return pointCount + 2;
    }

    public int sizeOfCylinderInVertex(int pointCount) {
        return (pointCount + 1) * 2;
    }

    public int floatSizeOfVertices(int vertexCount) {
        return FLOATS_PER_VERTEX * vertexCount;
    }

    public int floatSizeOfTextureVertices(int vertexCount) {
        return FLOATS_PER_TEXTURE_VERTEX * vertexCount;
    }

    public abstract int verticesCount();

    public interface DrawTask {
        void draw();
    }

    public float[] getModelMatrix() {
        return mModelMatrix;
    }

    public void resetModelMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    // 旋转、缩放、平移动画
    public void rotate(int degrees, float x, float y, float z) {
        Matrix.rotateM(mModelMatrix, 0, degrees, x, y, z);
    }

    public void translate(float x, float y, float z) {
        Matrix.translateM(mModelMatrix, 0, x, y, z);
    }

    public void scale(float x, float y, float z) {
        Matrix.scaleM(mModelMatrix, 0, x, y, z);
    }
}
