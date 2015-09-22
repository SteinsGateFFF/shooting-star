package bomb;

import android.content.Context;
import android.graphics.Paint;
import android.os.Vibrator;

import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.GameManager;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/12/2015.
 */
public class AtomicBomb extends Bomb implements Hittable{

    private CircleCollider mCollider;
    public static int BOMB_DAMAGE = 30;

    private Context mContext;
    public AtomicBomb(float x, float y, float r, Paint p){
        super(x,y,r,p);
        mCollider = new CircleCollider(r, new Position(x, y));
        mContext = GameManager.getInstance().getContext();
    }

    public void onCollision(Hittable h){
        if(h instanceof Bullet){
            Vibrator v = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }

    public CircleCollider getCollider(){
        return mCollider;
    }
    @Override
    public void tick(){
        super.tick();
        mCollider.setPosition(mSelfPos);

    }

}
