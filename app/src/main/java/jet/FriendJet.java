package jet;

import android.graphics.Paint;

import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Velocity;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class FriendJet extends Jet implements Hittable{

    public static int ATTACK_DAMAGE = 45;
    public FriendJet(float x, float y, float r, Paint p,int animationType){
        super(x,y,r,p,animationType);
        mVelocity = new Velocity(0,-10);

    }

    public void onCollision(Hittable h){
        float curHealth = getHealth();
        if(h instanceof Bullet){
            curHealth -= ((Bullet) h).getDamage();
        }
        setHealth(curHealth);
        if(curHealth < 0) {
            setDead(true);
        }
        if(h instanceof EnemyJet){
            setDead(true);
        }

    }

    @Override
    public void tick(){

        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        getCollider().setPosition(mSelfPos);

    }
}
