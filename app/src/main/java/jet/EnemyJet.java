package jet;

import android.graphics.Paint;

import bomb.AtomicBomb;
import bomb.LighteningBomb;
import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.PowerUp;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public class EnemyJet extends Jet implements Hittable{

    public void onCollision(Hittable h){
        float curHealth = getHealth();
        if(h instanceof AtomicBomb){
            curHealth -= AtomicBomb.BOMB_DAMAGE;
        }
        else if(h instanceof Bullet){
            curHealth -= ((Bullet) h).getDamage();
        }
        else if(h instanceof LighteningBomb){
            curHealth -= LighteningBomb.BOMB_DAMAGE;
        }else if(h instanceof  FriendJet){
            curHealth -= FriendJet.ATTACK_DAMAGE;
        }
        setHealth(curHealth);
        if(curHealth < 0) {
            setDead(true);
        }
    }

    private JetLifeCycle jetLifeCycle;
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

        public EnemyJet build(){
            return new EnemyJet(this);
        }
    }
    private EnemyJet(Builder builder){
        super(builder);

    }

    @Override
    public void tick(){
        super.tick();
        try {
            Position selfJetPos = GameManager.getInstance().getSelfJetPosition();
            if(!mIsDead){
                for(Gun gun:mGuns){
                    mBullets.addAll(gun.tick(mSelfPos, selfJetPos));
                }

            }
        } catch (Exception e) {
        }
        for (Bullet b : mBullets){
            b.tick();
        }
    }
    public void setJetLifeCycleListener(JetLifeCycle e){
        jetLifeCycle = e;

    }
    @Override
    public void setDead(boolean b){
        super.setDead(b);
        jetLifeCycle.onDeath(this);
    }
}
