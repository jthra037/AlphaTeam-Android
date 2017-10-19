package alpha.nosleep.game.framework;

/**
 * Created by John on 2017-10-12.
 */

public interface IPhysics {
    float mass = 1;
    FTuple velocity = new FTuple(0, 0);

    void AddForce(float force, float deltaTime);
    void SetVelocity(FTuple velocity);
}
