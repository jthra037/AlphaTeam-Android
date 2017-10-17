package alpha.nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public class BoxCollider implements Collider {
    private ITuple size;

    public BoxCollider(ITuple size)
    {
        this.size = size;
    }

    public BoxCollider(int x, int y)
    {
        this.size = new ITuple(x, y);
    }

    @Override
    public boolean OnOverlap() {
        return false;
    }

    @Override
    public Hit OnCollision() {
        return null;
    }

    public ITuple getSize() {
        return size;
    }

    public void setSize(ITuple size) {
        this.size = size;
    }
}
