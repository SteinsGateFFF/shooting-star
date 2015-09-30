package bomb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import hacking.to.the.gate.BoxCollider;
import hacking.to.the.gate.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class LighteningBomb extends Bomb{

    public static int BOMB_DAMAGE = 80;
    private float mWidth;
    private float mHeight;
    private int mFlashInterval = 20;
    private boolean mIsFlashing = false;
    private BoxCollider mCollider;
    public LighteningBomb(float x, float y,float w, float h, Paint p){
        super(x,y,0,p);
        mWidth = w;
        mHeight = h;
        mCollider = new BoxCollider(w,h,new Position(x,y));

    }
    public void onCollision(Hittable h){}
    public List<Hittable> getHittableChildren(){
        return null;
    }

    public BoxCollider getCollider(){
        return mCollider;
    }

    @Override
    public void draw(Canvas canvas){
        if(mIsFlashing){
            canvas.drawColor(Color.WHITE);
        }
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
        if(mCounter%mFlashInterval == 0)
        {
            mIsFlashing = !mIsFlashing;
        }

    }
}
