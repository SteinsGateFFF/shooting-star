package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet {
    private Position mSelfPos;
    private Position mDestPos;

    private float mBulletR;
    private Velocity mVelocity;
    private Paint mPaint;
    private Velocity.VelocityPattern mVelocityPattern;
    private float mMaxSpeed;
    private boolean mHasDestination;

    public Bullet(Position pos, float r, Paint paint, float vx, float vy){
        mBulletR = r;
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
        return mBulletR;
    }

    public void onDraw(Canvas canvas){
        if(mVelocityPattern!=null) {
            mVelocity = mVelocityPattern.nextVelocity(mVelocity);
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        canvas.drawCircle(mSelfPos.getPositionX(),mSelfPos.getPositionY(),mBulletR,mPaint);
    }

    public void setDestination(Position pos, boolean hasDestination ){
        mDestPos = pos;
        mHasDestination = hasDestination;
        mVelocity = Velocity.getDestinationVelocity(mSelfPos,mDestPos,mMaxSpeed);
    }
}
