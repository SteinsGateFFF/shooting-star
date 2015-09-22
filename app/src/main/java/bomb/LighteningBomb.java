package bomb;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import hacking.to.the.gate.BoxCollider;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class LighteningBomb extends Bomb implements Hittable {

    public static int BOMB_DAMAGE = 80;
    private float mWidth;
    private float mHeight;
    private BoxCollider mCollider;
    public LighteningBomb(float x, float y,float w, float h, Paint p){
        super(x,y,0,p);
        mWidth = w;
        mHeight = h;
        mCollider = new BoxCollider(w,h,new Position(x,y));

    }
    public void onCollision(Hittable h){

    }

    public BoxCollider getCollider(){
        return mCollider;
    }

    @Override
    public void draw(Canvas canvas){
        if(!mShouldRecycle) {
            Rect rect = new Rect((int)(mSelfPos.getPositionX()-mWidth/2),
                    (int)(mSelfPos.getPositionY()-mHeight/2),
                    (int)(mSelfPos.getPositionX()+mWidth/2),
                    (int)(mSelfPos.getPositionY()+mHeight/2));
            canvas.drawRect(rect, getPaint());

        }
    }
    @Override
    public void tick(){
        super.tick();

        mCollider.setPosition(mSelfPos);
    }
}
