package jet;

import android.graphics.Paint;

import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.JetLifeCycle;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.PowerUp;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public class EnemyJet extends Jet implements Hittable{

    public void onCollision(Hittable h){
        float curHealth = getHealth();
        if(h instanceof PowerUp){
            curHealth += PowerUp.POWERUP_HEAL;
        }
        else if(h instanceof Bullet){
            curHealth -= ((Bullet) h).getDamage();
        }
        setHealth(curHealth);
        if(curHealth < 0) {
            setDead(true);
        }
    }

    private JetLifeCycle jetLifeCycle;
    public EnemyJet(float x, float y, float r, Paint p) {
        super(x,y,r,p);
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
