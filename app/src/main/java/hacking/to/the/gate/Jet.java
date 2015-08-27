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

    private boolean mIsPlayer;

    private Gun mGun;

    /**
     * Create a Jet Object
     * @param x x coordinate of the center of the jet
     * @param y y coordinate of the center of the jet
     * @param r radius of the jet
     * @param p paint that paints the jet and bullet
     * @param shootingInterval number of ticks between each shooting
     */
    public Jet(float x, float y, float r, Paint p,boolean isPlayer){
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
        mIsPlayer = isPlayer;
        mBullets = new LinkedList<>();
        mGun = Gun.getGun(Gun.GUN_TYPE_DEFAULT);
    }

    public void setGunType(int gunType){
        mGun = Gun.getGun(gunType);
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
    public void tick(){

        if(mHasDestination) {
            // Fixed a bug that the ject have weird movement if destination is at the center of the jet.
            mVelocity = Velocity.getDisplacement(mSelfPos,mDestPos);
            if(mVelocity.getSpeed() > mMaxSpeed) {
                mVelocity = Velocity.getDestinationVelocity(mSelfPos, mDestPos, mMaxSpeed);
            }
            //Log.d("destination", "Set self velocity: "+mSelfPos+" "+mDestPos+" "+mVelocity);
        } else {
            mVelocity = new Velocity(0,0);

        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        if(!mIsPlayer) {

            Position selfJetPos = GameManager.getInstance().getSelfJetPosition();
            mBullets.addAll(mGun.tick(mSelfPos, selfJetPos));

        } else {

            // TODO: Later should pass enemy targets.
            mBullets.addAll(mGun.tick(mSelfPos, null));
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

    /**
     * Given a list of bullet, check if the bullet hit this jet, and perform corresponding
     * action, like decreasing Health.
     * @param bullets
     */
    public void checkCollision(List<Bullet> bullets){
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

            }

        }

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
        Log.d("destination", "Set self destination: "+mDestPos);
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
