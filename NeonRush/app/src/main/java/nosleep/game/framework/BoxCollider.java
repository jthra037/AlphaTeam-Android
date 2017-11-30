package nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public class BoxCollider extends Collider {
    private ITuple size;

    public BoxCollider(ITuple size)
    {
        this.size = size;
    }

    public BoxCollider(int x, int y)
    {
        this.size = new ITuple(x, y);
    }

    public ITuple getSize() {
        return size;
    }

    public void setSize(ITuple size) {
        this.size = size;
    }

    @Override
    public boolean OnOverlap(Object other, ITuple pos) {
        return false;
    }

    @Override
    public boolean OnOverlap(Object other, FTuple pos) {
        return false;
    }

    @Override
    public Hit OnCollision(Object other, ITuple pos) {
        return null;
    }
}
