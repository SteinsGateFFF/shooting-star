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

    public float getRadius() {
        return mRadius;
    }

    /**
     * Draw the bullet on the given canvas.
     *
     * TODO: Currently the moving of the bullet and drawing are in the same method. Should separate them into tick() and draw()
     * @param canvas
     */
    public void onDraw(Canvas canvas){
        if(mVelocityPattern!=null) {
            mVelocity = mVelocityPattern.nextVelocity(mVelocity);
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        canvas.drawCircle(mSelfPos.getPositionX(),mSelfPos.getPositionY(), mRadius,mPaint);
    }

    /**
     * Set the destination for the bullet
     * @param pos
     * @param hasDestination move towards the destination if it is true, otherwise stay.
     */
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
