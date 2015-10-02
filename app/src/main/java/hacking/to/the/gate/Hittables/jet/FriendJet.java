package hacking.to.the.gate.Hittables.jet;

import java.util.List;

import hacking.to.the.gate.Hittables.bullet.Bullet;
import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Velocity;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class FriendJet extends Jet{

    public List<Hittable> getHittableChildren(){
        return null;
    }

    public static int ATTACK_DAMAGE = 15;
    public static class Builder extends Jet.Builder<Builder> {
        public Builder() {
            super();
            mAnimation = JetAnimation.getInstance(JetAnimation.TYPE_SELF_JET);
        }


        public FriendJet build(){
            return new FriendJet(this);
        }
    }
    private FriendJet(Builder builder){
        super(builder);
        mVelocity = new Velocity(0,-10);

    }

    public void onCollision(Hittable h){
        if(!isDead()){
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

    }

    @Override
    public void tick(){

        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        getCollider().setPosition(mSelfPos);

    }
}
