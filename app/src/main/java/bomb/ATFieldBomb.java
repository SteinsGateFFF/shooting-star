package bomb;

import android.graphics.Paint;
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

    }

    public void onCollision(Hittable h){}

    public CircleCollider getCollider(){
        return mCollider;
    }

    @Override
    public void tick(){
        setBombTime(600);
        super.tick();
        try{
            setSelfPos(GameManager.getInstance().getSelfJetPosition());
        }catch(Exception e){
        }
       mCollider.setPosition(mSelfPos);

    }
}
