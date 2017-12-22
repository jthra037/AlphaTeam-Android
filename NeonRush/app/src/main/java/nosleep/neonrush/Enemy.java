package nosleep.neonrush;

import java.util.Random;

import nosleep.game.framework.FTuple;

/**
 * Created by John on 2017-10-19.
 */

public class Enemy extends Ball {
    private Player player;
    private float speed = 350;
    private float F = 60;

    public Enemy(World world, int radius, FTuple position) {
        super(world, radius);
        Random r = new Random();
        this.color = world.Palette[r.nextInt(world.Palette.length)];
        this.position = position;
        this.player = world.getPlayer();
        tag = "Enemy";
    }

    public Enemy(World world, int radius) {
        super(world, radius);
        Random r = new Random();
        this.color = world.Palette[r.nextInt(world.Palette.length)];
        this.position = new FTuple(0, 0);
        this.player = world.getPlayer();
        tag = "Enemy";
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        FTuple playerPos = getWorld().getPlayer().getWorldCoord();
        FTuple Fa = playerPos.Add(position.Mul(-1)).Normalized().Mul(F);
        if ( Math.abs(playerPos.x - position.x) > getWorld().getWidth()/2 )
        {
            Fa.x *= -1;
        }
        if ( Math.abs(playerPos.y - position.y) > getWorld().getHeight()/2 )
        {
            Fa.y *= -1;
        }
        AddForce(Fa);

        if(velocity.LengthS() > speed * speed)
        {
            velocity = velocity.Normalized().Mul(speed);
        }
    }


}
