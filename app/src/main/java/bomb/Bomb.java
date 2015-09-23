package bomb;

import android.graphics.Canvas;
import android.graphics.Paint;

import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/10/2015.
 */
public class Bomb {

    protected Position mSelfPos;
    private float mRadius;
    private Paint mPaint;
    protected boolean mShouldRecycle = false;

    private int mBombTime ;
    protected int mCounter;

    public Bomb(float x, float y, float r, Paint p){
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mCounter = 0;
        mBombTime = 200;
    }

    public void draw(Canvas canvas){
        if(!mShouldRecycle) {
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        }
    }
    public Paint getPaint(){
        return mPaint;
    }

    public boolean shouldRecycle(){
        return mShouldRecycle;
    }

    public void setDead(boolean b){
        mShouldRecycle = b;
    }

    public void setBombTime(int time){
        mBombTime = time;
    }

    public void setSelfPos(Position pos){
        mSelfPos = pos;
    }

    public void tick(){
        mCounter++;
        if(mCounter>mBombTime){
            setDead(true);
        }
    }
}
