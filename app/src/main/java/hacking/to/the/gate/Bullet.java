package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet {
    private Position mSelfPos;


    private float mRadius;
    private Velocity mVelocity;
    private Paint mPaint;
    private Velocity.VelocityPattern mVelocityPattern;
    /**
     * TODO: MaxSpeed actually does not guarantee the max speed.
     */
    private float mMaxSpeed;
    private boolean shouldRecycle = false;
    /**
     * Damage that should be dealt to jet when is collided.
     */
    private float mDamage;



    public Bullet(Position pos, float r, Paint paint, Velocity v, float damage){
        mRadius = r;
        mSelfPos = pos;
        mPaint = paint;
        mVelocity = v;
        mMaxSpeed = 20;
        mDamage = damage;
    }

    public float getDamage(){
        return mDamage;
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
    public void draw(Canvas canvas){

        canvas.drawCircle(mSelfPos.getPositionX(),mSelfPos.getPositionY(), mRadius,mPaint);
    }

    public void tick(){
        if(mVelocityPattern!=null) {
            mVelocity = mVelocityPattern.nextVelocity(mVelocity);
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
    }

    /**
     * Set the destination for the bullet
     * @param pos
     */
    public void setDestination(Position pos){

        mVelocity = Velocity.getDestinationVelocity(mSelfPos,pos,mMaxSpeed);
    }


    public void recycle(){
        shouldRecycle = true;
    }

    public boolean shouldRecycle(){
        return shouldRecycle || mSelfPos.isOutOfScreen((int) mRadius);
    }
}
