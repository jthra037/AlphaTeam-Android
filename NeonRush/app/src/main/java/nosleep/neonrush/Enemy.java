package nosleep.neonrush;

import java.util.Random;

import nosleep.androidgames.framework.Graphics;
import nosleep.game.framework.FTuple;

/**
 * Created by John on 2017-10-19.
 * Trimmed by Mark on 2018-01-03.
 */

public class Enemy extends Ball
{
    private Player player;

    public Enemy(World world, int radius, FTuple pos)
    {
        super(world, radius);
        tag = "Enemy";
        player = world.getPlayer();

        //Assign a random color from the palette.
        Random r = new Random();
        colorIndex = r.nextInt(world.Palette.length);
        color = world.Palette[colorIndex];
        img = g.newPixmap(world.enemyPalette[colorIndex], Graphics.PixmapFormat.ARGB8888);
        backupImg = g.newPixmap(world.enemyPalette[colorIndex], Graphics.PixmapFormat.ARGB8888);
        int imgScalar = (int)(radius * 2.0f * 1.5f);    //Manually calculated to suit art assets.
        g.resizePixmap(img, imgScalar, imgScalar);

        position = pos;
    }

    @Override
    public void update(float deltaTime)
    {
        FTuple playerPos = player.getWorldCoord();
        float speed = 350;
        float F = 60;
        FTuple Fa = playerPos.Add(position.Mul(-1)).Normalized().Mul(F);

        //
        //
        //This is why enemies move weird. This worked when our world was the size of the screen. No longer the case.
        //Especially near wrap lines enemies won't chase the player, they'll chase a weird direction.
        //
        //

        if ( Math.abs(playerPos.x - position.x) > world.getWidth()/2 )
        {
            Fa.x *= -1;
        }
        if ( Math.abs(playerPos.y - position.y) > world.getHeight()/2 )
        {
            Fa.y *= -1;
        }

        AddForce(Fa);

        //Cap enemy speed.
        if(velocity.LengthS() > speed * speed)
        {
            velocity = velocity.Normalized().Mul(speed);
        }

        //Once velocity is properly determined, run it through Ball's movement functionality in update().
        super.update(deltaTime);
    }

    @Override
    public void present(float deltaTime)
    {
        super.present(deltaTime, localCoord);
    }
}
