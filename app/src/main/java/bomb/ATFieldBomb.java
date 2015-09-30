package bomb;

import android.graphics.Paint;

import java.util.List;

import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;


/**
 * Created by Ruiqian on 9/20/2015.
 */
public class ATFieldBomb extends Bomb implements Hittable {

    private CircleCollider mCollider;
    public ATFieldBomb(float x, float y, float r, Paint p){
        super(x,y,r,p);
        mCollider = new CircleCollider(r, new Position(x, y));
        setBombTime(600);

    }

    public void onCollision(Hittable h){}
    public List<Hittable> getHittableChildren(){
        return null;
    }

    public CircleCollider getCollider(){
        return mCollider;
    }

    @Override
    public void tick(){

        super.tick();
        try{
            setSelfPos(GameManager.getInstance().getSelfJetPosition());
        }catch(Exception e){
            setDead(true);
        }
    }

    @Override
    public void setSelfPos(Position pos){
        super.setSelfPos(pos);
        mCollider.setPosition(mSelfPos);
    }
}
