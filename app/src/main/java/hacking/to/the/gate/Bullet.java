package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet {
    private Position mSelfPos;
    private Position mDestPos;

    private float mRadius;
    private Velocity mVelocity;
    private Paint mPaint;
    private Velocity.VelocityPattern mVelocityPattern;
    private float mMaxSpeed;
    private boolean mHasDestination;
    private boolean shouldRecycle = false;



    public Bullet(Position pos, float r, Paint paint, float vx, float vy){
        mRadius = r;
        mSelfPos = pos;
        mPaint = paint;
        mVelocity = new Velocity(vx,vy);
        mMaxSpeed = 20;
        mHasDestination = false;
    }

    public void setVelocityPattern(Velocity.VelocityPattern pattern){
        mVelocityPattern = pattern;
    }


    public Position getSelfPos(){return mSelfPos;};

    public float getBulletR() {
        return mRadius;
    }

    public void onDraw(Canvas canvas){
        if(mVelocityPattern!=null) {
            mVelocity = mVelocityPattern.nextVelocity(mVelocity);
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        canvas.drawCircle(mSelfPos.getPositionX(),mSelfPos.getPositionY(), mRadius,mPaint);
    }

    public void setDestination(Position pos, boolean hasDestination ){
        mDestPos = pos;
        mHasDestination = hasDestination;
        mVelocity = Velocity.getDestinationVelocity(mSelfPos,mDestPos,mMaxSpeed);
    }


    public void recycle(){
        shouldRecycle = true;
    }

    public boolean shouldRecycle(){
        return shouldRecycle || mSelfPos.isOutOfScreen((int) mRadius);
    }
}
