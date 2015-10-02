package hacking.to.the.gate.Hittables.bullet;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Hittables.bomb.ATFieldBomb;
import hacking.to.the.gate.Hittables.bomb.AtomicBomb;
import hacking.to.the.gate.Hittables.jet.EnemyJet;
import hacking.to.the.gate.Hittables.jet.FriendJet;
import hacking.to.the.gate.Hittables.jet.SelfJet;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.Velocity;
import hacking.to.the.gate.VelocityPattern;
import hacking.to.the.gate.VelocityPatternFactory;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet implements Hittable {
    private CircleCollider collider;
    private Position mSelfPos;
    private float mRadius;
    private Velocity mVelocity;
    private Paint mPaint;
    private List<VelocityPattern> mVelocityPatterns;
    /**
     * TODO: MaxSpeed actually does not guarantee the max speed.
     */
    //propose to close
    private float mMaxSpeed;
    private boolean shouldRecycle = false;
    /**
     * Damage that should be dealt to hacking.to.the.gate.Hittables.Hittable.jet when is collided.
     */
    private float mDamage;

//index of current velocity pattern in velocityPattern set.
    private int mVelocityPatternCounter;

    public static final int BULLET_STYLE_DEFAULT = 0;

    public static final int BULLET_STYLE_WORM = 1;

    public static final int BULLET_STYLE_SPIRAL = 2;

    private BulletAnimation mAnimation;
    public List<Hittable> getHittableChildren(){
        return null;
    }
	public CircleCollider getCollider(){
        return collider;
    }
    public void onCollision(Hittable h){
        if(!shouldRecycle()){
            if(h instanceof EnemyJet || h instanceof SelfJet ||
                    h instanceof AtomicBomb|| h instanceof ATFieldBomb||
                    h instanceof FriendJet){
                recycle();
            }
        }
    }
    public Bullet(Position pos, float r, Paint paint, Velocity v, float damage, ArrayList<Integer> bulletStyles, float maxSpeed){
    
        collider = new CircleCollider(r,pos);
        mRadius = r;
        mSelfPos = pos;
        mPaint = paint;
        mMaxSpeed = maxSpeed;
        mDamage = damage;
        mVelocityPatterns = new ArrayList<>();
        mVelocity = v;
        //TODO: Need to support multiple patterns.
        //propose to close
        if(bulletStyles!=null) {
            for (int style : bulletStyles) {
                applyStyle(style);
            }
        }

    }

    public void setBulletAnimation(int type){
        mAnimation = BulletAnimation.getInstance(type);
    }

    private void applyStyle(int bulletStyle){

        if(mVelocity!=null&&!validateVelocity(mVelocity)){
            mVelocity = normalizeVelocity(mVelocity);
        }
        switch (bulletStyle){
            case BULLET_STYLE_WORM:
                mVelocityPatterns.add(VelocityPatternFactory.produce(VelocityPattern.WORM, mVelocity));
                break;
            case BULLET_STYLE_SPIRAL:
                mVelocityPatterns.add(VelocityPatternFactory.produce(VelocityPattern.SPIRAL, mVelocity));
                break;
        }
    }

    /**
     * check if the given velocity v is valid(not greater than maxspeed)
     * @param v
     * @return true if it's valid, otherwise false
     */
    private boolean validateVelocity(Velocity v){
        float speed = v.getSpeed();
        return speed > mMaxSpeed;

    }

    /**
     * give a non-zero velocity v
     *
     * @param v non-zero velocity
     * @return a velocity that equals to maxspeed.
     */
    private Velocity normalizeVelocity(Velocity v){

        float ratio = mMaxSpeed/v.getSpeed();

        return new Velocity(v.getVelocityX()*ratio,v.getVelocityY()*ratio);

    }


    public float getDamage(){
        return mDamage;
    }

    public Position getSelfPos(){return mSelfPos;};

    public float getRadius() {
        return mRadius;
    }

    public void draw(Canvas canvas){
        if(mAnimation==null) {
            // TODO: Should get rid of this condition.
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        } else {
            mAnimation.draw(canvas,mSelfPos);
        }
    }

    public void tick(){

        if(mVelocityPatternCounter>mVelocityPatterns.size()-1){
            mVelocityPatternCounter=0;
        }
        if(mVelocityPatterns!=null && mVelocityPatterns.size()!= 0) {
            if(mVelocityPatterns.get(mVelocityPatternCounter)!= null) {
                mVelocity = mVelocityPatterns.get(mVelocityPatternCounter).nextVelocity(mVelocity);
               // Log.d("Bullet-velocity: ",""+mVelocity+" speed"+mVelocity.getSpeed());
            }
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        collider.setPosition(mSelfPos);
        mVelocityPatternCounter++;

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
