package hacking.to.the.gate.Hittables.bomb;

import android.graphics.Canvas;
import android.graphics.Paint;

import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Position;

/**
 * Created by Ruiqian on 9/10/2015.
 */
public abstract class Bomb implements Hittable{

    protected BombFactory.BombLifeCycle bombLifeCycle;
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

    public void setBombCycleListener(BombFactory.BombLifeCycle e){
        bombLifeCycle = e;
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
        if(!mShouldRecycle){
            mCounter++;
            if(mCounter>mBombTime){
                timeIsUp();
            }
        }
    }
    public void timeIsUp(){
        setDead(true);

    }
}
