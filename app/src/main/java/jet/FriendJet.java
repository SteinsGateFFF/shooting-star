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
    public static class Builder extends Jet.Builder {
        public Builder(float x, float y, float r,int animationType) {
            super(x,y,r,animationType);
        }
        public Builder health(int val){
            mHealth = val;
            return this;
        }

        public Builder maxSpeed(int val){
            mMaxSpeed = val;
            return this;
        }

        public Builder hasDestination(boolean val){
            mHasDestination = val;
            return this;
        }

        public Builder setGunType(int val){
            mGunType = val;
            return this;
        }

        public Builder setBulletStyle(int val){
            mBulletStyle = val;
            return this;
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
