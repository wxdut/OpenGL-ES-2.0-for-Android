package wxplus.opengles2forandroid.obj;


import wxplus.opengles2forandroid.obj.base.Point;
import wxplus.opengles2forandroid.obj.base.Square;

import static wxplus.opengles2forandroid.utils.Constants.FLOATS_PER_VERTEX;
import static wxplus.opengles2forandroid.utils.Constants.VERTEX_COUNT_SQUARE;

/**
 * @author WangXiaoPlus
 * @date 2017/11/19
 */

public class Table extends Object {
    protected Point center;
    protected float width;
    protected float height;

    public Table(float width, float height) {
        this.center = new Point(0, 0, 0);
        this.width = width;
        this.height = height;
        addSquare(new Square(center, width, height));

    }

    @Override
    public int verticesCount() {
        return VERTEX_COUNT_SQUARE;
    }
}
