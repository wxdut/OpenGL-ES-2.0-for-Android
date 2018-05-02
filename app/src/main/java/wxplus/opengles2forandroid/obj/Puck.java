package wxplus.opengles2forandroid.obj;

import wxplus.opengles2forandroid.obj.base.Circle;
import wxplus.opengles2forandroid.obj.base.Cylinder;
import wxplus.opengles2forandroid.obj.base.Point;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 冰球
 */

public class Puck extends Object {

    public float radius; // 半径
    public float height; // 高度
    public Point center; // 中心点

    public Puck(float radius, float height) {
        this.radius = radius;
        this.height = height;
        this.center = new Point(0, 0, 0);

        addCircle(new Circle(new Point(center.x, center.y, center.z + height / 2), radius), CIRCLE_VERTICES_COUNT)
                .addOpenCylinder(new Cylinder(center, radius, height), CIRCLE_VERTICES_COUNT);

    }

    @Override
    public int verticesCount() {
        return sizeOfCircleInVertex(CIRCLE_VERTICES_COUNT) + sizeOfCylinderInVertex(CIRCLE_VERTICES_COUNT);
    }
}
