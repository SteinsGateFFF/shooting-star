package jet;

import android.graphics.Paint;

import java.util.List;

import bomb.AtomicBomb;
import bomb.LighteningBomb;
import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public class EnemyJet extends Jet{

    public List<Bullet> getHittableChildren(){
        return mBullets;
    }
    public void onCollision(Hittable h){
        if(!isDead()){
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
    }

    private JetLifeCycle jetLifeCycle;
    public static class Builder extends Jet.Builder<Builder> {
        public Builder() {
            super();
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
