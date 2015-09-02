package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet implements Hittable{
    private CircleCollider collider;
    private Position mSelfPos;
    private Position mDestPos;

    private float mRadius;
    private Velocity mVelocity;
    private Paint mPaint;
    private Velocity.VelocityPattern mVelocityPattern;
    private float mMaxSpeed;
    private boolean mHasDestination;
    private boolean shouldRecycle = false;
    /**
     * Damage that should be dealt to jet when is collided.
     */
    private float mDamage;

    public CircleCollider getCollider(){
        return collider;
    }



    public Bullet(Position pos, float r, Paint paint, float vx, float vy, float damage){
        collider = new CircleCollider(r,pos);
        mRadius = r;
        mSelfPos = pos;
        mPaint = paint;
        mVelocity = new Velocity(vx,vy);
        mMaxSpeed = 20;
        mHasDestination = false;
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
        collider.setPosition(mSelfPos);
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
