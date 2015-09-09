package jet;

import android.graphics.Paint;
import android.util.Log;

import java.util.List;

import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.PowerUp;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public class SelfJet extends Jet implements Hittable{

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
    public SelfJet(float x, float y, float r, Paint p,int animationType) {
        super(x,y,r,p,animationType);
        mGuns.add(Gun.getGun(Gun.GUN_TYPE_NO_TARGET_UP, mBulletStyles));
    }

    @Override
    public void tick(){
        super.tick();
        if(!mIsDead) {
            int i = 0;
            for(Gun gun:mGuns){
                Position startPos = new Position(mSelfPos.getPositionX()+50*i,mSelfPos.getPositionY());
                List<Position> enemyPositions = GameManager.getInstance().getEnemyPositions();
                if(enemyPositions.isEmpty()){
                    mBullets.addAll(gun.tick(startPos, null));
                }
                else{
                    mBullets.addAll(gun.tick(startPos, getTargetPosition(mSelfPos,enemyPositions)));
                }
                i++;
            }
        }
        for (Bullet b : mBullets){
            b.tick();
        }
    }

    private static Position getTargetPosition(Position selfjetPos, List<Position> enemyPositions){
        Position targetPosition = enemyPositions.get(0);
        float min = getDistanceInY(selfjetPos,targetPosition);
        Log.d("target", "" + min);
        for(Position p : enemyPositions){
            Log.d("target",""+p);
            float diff = getDistanceInY(selfjetPos,p);
            Log.d("target",""+diff);
            if(diff<min){
                targetPosition = p;
                min = diff;
            }
        }
        Log.d("target","target: "+targetPosition);
        return targetPosition;

    }

    private static float getDistanceInY(Position pos1, Position pos2){
        return Math.abs(pos1.getPositionY() - pos2.getPositionY());
    }
}
