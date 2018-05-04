package wxplus.opengles2forandroid.obj;

/**
 * @author WangXiaoPlus
 * @date 2018/5/3
 */
public class Cube extends Object {

    protected float sideWidth;

    public Cube(float sideWidth) {
        this.sideWidth = sideWidth;

        addCube(sideWidth);
    }

    @Override
    public int verticesCount() {
        return 0;
    }
}
