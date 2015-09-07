package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet implements Hittable{
    private CircleCollider collider;
    private Position mSelfPos;


    private float mRadius;
    private Velocity mVelocity;
    private Paint mPaint;
    private VelocityPattern mVelocityPattern;
    /**
     * TODO: MaxSpeed actually does not guarantee the max speed.
     */
    private float mMaxSpeed;
    private boolean shouldRecycle = false;
    /**
     * Damage that should be dealt to jet when is collided.
     */
    private float mDamage;

    public static final int BULLET_STYLE_DEFAULT = 0;

    public static final int BULLET_STYLE_WORM = 1;

    public static final int BULLET_STYLE_SPIRAL = 2;
	
	public CircleCollider getCollider(){
        return collider;
    }
    public void onCollision(Hittable h){
        if(h instanceof Jet){
            recycle();
        }

    }
    public Bullet(Position pos, float r, Paint paint, Velocity v, float damage, int bulletStyle){
    
        collider = new CircleCollider(r,pos);
        mRadius = r;
        mSelfPos = pos;
        mPaint = paint;
        mVelocity = v;
        mMaxSpeed = 20;
        mDamage = damage;
        //TODO: Need to support multiple patterns.
        switch (bulletStyle){
            case BULLET_STYLE_WORM:
                mVelocityPattern = VelocityPatternFactory.produce(VelocityPattern.WORM, mVelocity);
                break;
            case BULLET_STYLE_SPIRAL:
                mVelocityPattern = VelocityPatternFactory.produce(VelocityPattern.SPIRAL, mVelocity);
                break;
        }
    }



    public float getDamage(){
        return mDamage;
    }

    public void setVelocityPattern(VelocityPattern pattern){
        mVelocityPattern = pattern;
    }


    public Position getSelfPos(){return mSelfPos;};

    public float getRadius() {
        return mRadius;
    }

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

    @Override
    public String toString() {
        return mSelfPos.toString();
    }
}
