package jet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import hacking.to.the.gate.Bullet;
import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.Velocity;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public class Jet{

    public CircleCollider getCollider(){
        return collider;
    }

    private CircleCollider collider;
    /**
     * Position of the center of the circle that represents this jet.
     */
    protected Position mSelfPos;
    /**
     * Position of the destination position that the jet is heading to.
     */
    private Position mDestPos;
    private float mRadius;
    /**
     * Paint for drawing the jet and bullets.
     */
    private Paint mPaint;
    /**
     * The current velocity of the jet.
     */
    protected Velocity mVelocity;
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
    protected List<Bullet> mBullets;

    protected List<Gun>  mGuns;

    protected ArrayList<Integer> mBulletStyles;

    /**
     * Create a Jet Object
     * @param x x coordinate of the center of the jet
     * @param y y coordinate of the center of the jet
     * @param r radius of the jet
     * @param p paint that paints the jet and bullet
     *
     */

    private JetAnimation mAnimation;
    protected boolean mIsDead = false;

public Jet(float x, float y, float r, Paint p, int animationType){
        collider = new CircleCollider(r, new Position(x, y));
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
        mBullets = new LinkedList<>();
        mAnimation = JetAnimation.getInstance(animationType);
        mGuns = new LinkedList<>();
        mBulletStyles = new ArrayList<>();
        mBulletStyles.add(Bullet.BULLET_STYLE_DEFAULT);
        mGuns.add(Gun.getGun(Gun.GUN_TYPE_DEFAULT,mBulletStyles));
    }

    /**
     * Set the gun of this jet to the given type
     * @param gunType
     */
    public void setGunType(int index,int gunType, ArrayList<Integer> bulletStyles){
        if(index < mGuns.size()){
            mGuns.set(index, Gun.getGun(gunType, bulletStyles));
        }

    }

    public int getNumOfGuns(){
        return mGuns.size();
    }


    /**
     * Draw the jet to the given canvas.
     * @param canvas
     */
    public void draw(Canvas canvas){
        if(!mIsDead) {
//            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
            mAnimation.draw(canvas, mSelfPos);
        }
        for(Bullet b:mBullets){
            b.draw(canvas);
        }

    }

    public Position getSelfPosition() {return mSelfPos;}
    public boolean isDead(){
        return mIsDead;
    }
    public void setHealth(float h){
        mHealth = h;
    }
    public float getHealth(){
        return mHealth;
    }

    public void setDead(boolean b){
        mIsDead = b;
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
        Log.d("destination", "Set self destination: " + mDestPos);
    }

    /**
     *
     * @return a list of bullets that are shot by this jet.
     */
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

    /**
     * Change the jet state to next tick.
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
        collider.setPosition(mSelfPos);

    }

    public boolean shouldRecycle(){
        boolean noBullets = mBullets.isEmpty();
        return noBullets&&(mIsDead || mSelfPos.isOutOfScreen((int) mRadius));
    }
}
