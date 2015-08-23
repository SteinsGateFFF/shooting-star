package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Jet {
    /**
     * Position of the center of the circle that represents this jet.
     */
    private Position mSelfPos;
    /**
     * Position of the destination position that the jet is heading to.
     */
    private Position mDestPos;
    /**
     * Radius of the circle that represents this jet.
     *
     * TODO: Currently the radius represent both the hitbox and the actual drawing. Should seperate these two.
     */
    private float mRadius;
    /**
     * Paint for drawing the jet and bullets.
     *
     * TODO: Should also use different Pain for jet and bullets.
     */
    private Paint mPaint;
    /**
     * The current velocity of the jet.
     */
    private Velocity mVelocity;
    /**
     * Health of the jet. Jet is destroyed if Heath < 0
     */
    private float mHealth;
    private final String TAG = "Jet";
    /**
     * The speed limit that the jet can move.
     */
    private float mMaxSpeed;
    /**
     * If the jet has a destination.
     */
    private boolean mHasDestination;
    /**
     * List of Bullets that are shot by this jet.
     */
    private List<Bullet> mBullets;
    /**
     * Frames that need to wait between bullets.
     */
    private int mShootingInterval = 1;
    /**
     * Number of frames that has passed.
     *
     * TODO: Should not clear this number to zero so that we can define multiple rate instead of one.
     */
    private int mFrameCount = 0;

    /**
     * Create a Jet Object
     * @param x x coordinate of the center of the jet
     * @param y y coordinate of the center of the jet
     * @param r radius of the jet
     * @param p paint that paints the jet and bullet
     * @param shootingInterval number of ticks between each shooting
     */
    public Jet(float x, float y, float r, Paint p, int shootingInterval){
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
        mBullets = new LinkedList<>();
        mShootingInterval = shootingInterval;
    }

    /**
     * Draw the jet to the given canvas.
     * @param canvas
     */
    public void draw(Canvas canvas){
        if(!mIsDead) {
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        }
        for(Bullet b:mBullets){
            b.onDraw(canvas);
        }

    }

    /**
     * Change the jet state to next tick.
     * @param bullet
     */
    public void tick(Bullet bullet){
        Log.d(TAG,"Bullets: "+mBullets.size());
        if(mHasDestination) {
            mVelocity = Velocity.getDestinationVelocity(mSelfPos, mDestPos, mMaxSpeed);

        } else {
            mVelocity = new Velocity(0,0);

        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);


        if(mFrameCount >= mShootingInterval) {
            mFrameCount = 0;
            shoot(bullet);
        } else {
            mFrameCount++;
        }


    }

    public Position getSelfPosition() {return mSelfPos;};
    public Paint getPaint(){
        return mPaint;
    }
    public float getRadius(){
        return mRadius;
    }

    public boolean isDead(){
        return mIsDead;
    }
    public void setHealth(float h){
        mHealth = h;
    }
    public float getHealth(){
        return mHealth;
    }

    private boolean mIsDead = false;
    public void setDead(boolean b){
        mIsDead = b;
    }

    /**
     *
     * @param b
     * @return true if the bullet hit this jet, otherwise false
     */
    private boolean checkHitBox(Bullet b){
        return !mIsDead
                && (Math.pow(
                mSelfPos.getPositionX()-b.getSelfPos().getPositionX(),
                2)
                +Math.pow(
                mSelfPos.getPositionY()-b.getSelfPos().getPositionY(),
                2)
                < Math.pow(mRadius+b.getRadius(),2));

    }
<<<<<<< HEAD
    public boolean checkCollision(List<Bullet> bullets){
        boolean collision = false;
=======

    /**
     * Given a list of bullet, check if the bullet hit this jet, and perform corresponding
     * action, like decreasing Health.
     * @param bullets
     */
    public void checkCollision(List<Bullet> bullets){
>>>>>>> ec1a8b0ca2d65be4f68a73eb499a86eeec789a2b
        for(Iterator<Bullet> it = bullets.iterator(); it.hasNext();){
            Bullet b = it.next();
            if(checkHitBox(b)){

                float curHealth = 0;
                curHealth = getHealth();
                curHealth -= 10;
                if(curHealth <0) {
                    setDead(true);

                }
                setHealth(curHealth);
                b.recycle();
                collision = true;

            }

        }
        return collision;

    }

    /**
     * Set the destination of the jet.
     * @param x
     * @param y
     * @param hasDestination true then jet will move towards the destination, otherwise will stay.
     */
    public void setDestination(float x, float y, boolean hasDestination ){
        mDestPos = new Position(x,y);
        mHasDestination = hasDestination;

    }


    /**
     * Added a default Bullet to the list of bullets.
     *
     * TODO: This method need to be deprecated.
     */
    public void shoot(){
        mBullets.add(new Bullet(mSelfPos, 10, mPaint, 0, -20));
    }

    /**
     * Added the given bullet to the list of bullets.
     *
     * @param bullet
     */
    public void shoot(Bullet bullet){
        if(bullet==null){
            shoot();
        } else {
            mBullets.add(bullet);
        }
    }

    public List<Bullet> getBullets(){
        return mBullets;
    }


    /**
     * Recycle all the bullets that can be recycled.
     */
    public void recycle(){
        for(Iterator<Bullet> it = mBullets.iterator();it.hasNext();){
            if(it.next().shouldRecycle()){
                it.remove();
            }
        }
    }

    public boolean shouldRecycle(){
        return mIsDead || mSelfPos.isOutOfScreen((int) mRadius);
    }

}
