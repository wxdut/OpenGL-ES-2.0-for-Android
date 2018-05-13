package wxplus.opengles2forandroid.obj;

import wxplus.opengles2forandroid.obj.geometry.Circle;
import wxplus.opengles2forandroid.obj.geometry.Cylinder;
import wxplus.opengles2forandroid.obj.geometry.Point;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 * <p>
 * 球棍
 */

public class Mallet extends Object {

    public Point center; // 中心
    public float radiusTop; // 上面的圆柱体的半径
    public float heightTop; // 上面的圆柱体的高度
    public float radiusBottom; // 上面的圆柱体的半径
    public float heightBottom; // 上面的圆柱体的高度

    public Mallet(float radiusTop, float radiusBottom, float heightTop, float heightBottom) {
        this.radiusTop = radiusTop;
        this.radiusBottom = radiusBottom;
        this.heightTop = heightTop;
        this.heightBottom = heightBottom;
        this.center = new Point(0, 0, 0);

        addCircle(new Circle(new Point(center.x, center.y, center.z + heightTop), radiusTop), CIRCLE_VERTICES_COUNT)
                .addCircle(new Circle(new Point(center.x, center.y, center.z), radiusBottom), CIRCLE_VERTICES_COUNT)
                .addOpenCylinder(new Cylinder(new Point(center.x, center.y, center.z + heightTop / 2), radiusTop, heightTop), CIRCLE_VERTICES_COUNT)
                .addOpenCylinder(new Cylinder(new Point(center.x, center.y, center.z - heightBottom / 2), radiusBottom, heightBottom), CIRCLE_VERTICES_COUNT);

    }

    @Override
    public int verticesCount() {
        return 2 * sizeOfCircleInVertex(CIRCLE_VERTICES_COUNT) + 2 * sizeOfCylinderInVertex(CIRCLE_VERTICES_COUNT);
    }
}
