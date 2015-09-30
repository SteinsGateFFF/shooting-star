package bomb;
import android.graphics.Paint;

import java.util.List;
import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/12/2015.
 */
public class AtomicBomb extends Bomb{

    private CircleCollider mCollider;
    public static int BOMB_DAMAGE = 30;
    public AtomicBomb(float x, float y, float r, Paint p){
        super(x,y,r,p);
        mCollider = new CircleCollider(r, new Position(x, y));
    }

    public void onCollision(Hittable h){

    }
    public List<Hittable> getHittableChildren(){
        return null;
    }

    public CircleCollider getCollider(){
        return mCollider;
    }
    @Override
    public void tick(){
        super.tick();

    }

}
