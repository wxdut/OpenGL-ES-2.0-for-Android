package wxplus.opengles2forandroid.obj;

import wxplus.opengles2forandroid.obj.geometry.Circle;
import wxplus.opengles2forandroid.obj.geometry.Point;
import wxplus.opengles2forandroid.obj.geometry.Square;

/**
 * @author WangXiaoPlus
 * @date 2018/5/1
 */

public class Photo extends Object {
    protected Point center;
    protected float width;
    protected float height;

    public Photo(float width, float height, boolean isCircle) {
        this.center = new Point(0, 0, 0);
        this.width = width;
        this.height = height;

        if (isCircle && width == height) {
            addCircle(new Circle(center, width / 2), CIRCLE_VERTICES_COUNT);
        } else {
            addSquare(new Square(center, width, height));
        }
        rotate(180, 0, 0, 1); // 图片默认是倒着的，这里翻过来。
    }

    @Override
    public int verticesCount() {
        return sizeOfCircleInVertex(CIRCLE_VERTICES_COUNT);
    }
}
