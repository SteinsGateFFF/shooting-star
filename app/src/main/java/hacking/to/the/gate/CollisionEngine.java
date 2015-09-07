package hacking.to.the.gate;

import android.util.Log;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public class CollisionEngine {

    private List<Jet> mEnemies;
    private Jet mPlayer;
    private List<PowerUp> mPowerups;


    public CollisionEngine(List<Jet> jets, List<PowerUp> powerups, Jet player){
        mEnemies = jets;
        mPowerups = powerups;
        mPlayer = player;
    }

    public void setPlayer(Jet newPlayer){
        mPlayer = newPlayer;
    }
    private boolean detectCollision(Hittable t1, Hittable t2){
        return detectCollision(t1.getCollider(),t2.getCollider());
    }

    private boolean detectCollision(CircleCollider c1, CircleCollider c2){

        return Math.pow(
                c1.getPosition().getPositionX()-c2.getPosition().getPositionX(),
                2)
                +Math.pow(
                c1.getPosition().getPositionY()-c2.getPosition().getPositionY(),
                2)
                < Math.pow(c1.getRadius()+c2.getRadius(),2);

    }

    private void doCollision(Hittable h1, Hittable h2){
        h1.onCollision(h2);
        h2.onCollision(h1);
    }

    public void tick(){
        for(Iterator<PowerUp> i = mPowerups.iterator();i.hasNext();) {
            PowerUp p = i.next();
            if(!mPlayer.isDead()&&p.isVisible()&& detectCollision(mPlayer,p)){
                doCollision(mPlayer,p);
            }
        }

        for(Jet jet:mEnemies){
            for (Iterator<Bullet> it = jet.getBullets().iterator(); it.hasNext(); ) {
                Bullet b = it.next();
                if (!mPlayer.isDead() && detectCollision(mPlayer, b)) {
                    Log.d("collision", "hit by enemy's bullet");
                    doCollision(mPlayer,b);
                }
            }
            for(Iterator<Bullet> it = mPlayer.getBullets().iterator(); it.hasNext();){
                Bullet b = it.next();
                if(!jet.isDead()&& detectCollision(jet,b)) {
                    Log.d("collision","hit by player's bullet");
                    doCollision(jet,b);
                }
            }
        }

    }

}
