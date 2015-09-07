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
public class Jet implements Hittable {

    private JetLifeCycle jetLifeCycle;
    public CircleCollider getCollider(){
        return collider;
    }

    public void onCollision(Hittable h){
        float curHealth = getHealth();
        if(h instanceof PowerUp){
            curHealth += PowerUp.POWERUP_HEAL;
        }
        else if(h instanceof Bullet){
            curHealth -= ((Bullet) h).getDamage();
        }
        setHealth(curHealth);
        if(curHealth < 0) {
            setDead(true);
            if(!mIsPlayer) {
                jetLifeCycle.onDeath(this);
            }
        }
    }

    public void setJetLifeCycleListener(JetLifeCycle e){
        jetLifeCycle = e;

    }
    private CircleCollider collider;
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
     * If true then this is self jet, otherwise false.
     */
    private boolean mIsPlayer;

    /**
     * {@link hacking.to.the.gate.Gun} of the jet.
     *
     * TODO: might need to support more than one gun.
     */
    private List<Gun>  mGuns;

    /**
     * Create a Jet Object
     * @param x x coordinate of the center of the jet
     * @param y y coordinate of the center of the jet
     * @param r radius of the jet
     * @param p paint that paints the jet and bullet
     *
     */
    public Jet(float x, float y, float r, Paint p,boolean isPlayer){
        collider = new CircleCollider(r, new Position(x, y));
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
        mIsPlayer = isPlayer;
        mBullets = new LinkedList<>();
        mGuns = new LinkedList<>();
        mGuns.add(Gun.getGun(Gun.GUN_TYPE_DEFAULT, Bullet.BULLET_STYLE_DEFAULT));
        if(isPlayer)
        {
            mGuns.add(Gun.getGun(Gun.GUN_TYPE_DEFAULT,Bullet.BULLET_STYLE_DEFAULT));
        }
    }


    /**
     * Set the gun of this jet to the given type
     * @param gunType
     */
    public void setGunType(int index,int gunType, int bulletStyle){
        if(index < mGuns.size()){
            mGuns.set(index, Gun.getGun(gunType, bulletStyle));
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
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        }
        for(Bullet b:mBullets){
            b.draw(canvas);
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

        // Shoot bullets.
        if(!mIsPlayer) {
            //Enemy jets shoot logic.
            // Default target jet of enemy jets is self jet.

            try {
                Position selfJetPos = GameManager.getInstance().getSelfJetPosition();
                if(!mIsDead){
                    for(Gun gun:mGuns){
                        mBullets.addAll(gun.tick(mSelfPos, selfJetPos));
                    }

                }
            } catch (Exception e) {
            }
        } else {
            //Self jet shoot logic.
            // TODO: Later should pass enemy targets.
            if(!mIsDead) {
                int i = 0;
                for(Gun gun:mGuns){
                    Position startPos = new Position(mSelfPos.getPositionX()+50*i,mSelfPos.getPositionY());

                    mBullets.addAll(gun.tick(startPos, null));
                    i++;
                }
            }
        }

        for (Bullet b : mBullets){
            b.tick();
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

    private boolean mIsDead = false;
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
        Log.d("destination", "Set self destination: "+mDestPos);
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

    public boolean shouldRecycle(){
        boolean noBullets = mBullets.isEmpty();
        if(mIsPlayer){
            Log.d(TAG, "Bullets: "+mBullets.size());
            for(Bullet b : mBullets){
                Log.d(TAG, "Should Recycle: "+b.shouldRecycle());
                Log.d(TAG, b.toString());
            }
            Log.d(TAG, "mIsDead: "+mIsDead);
            Log.d(TAG, "mIsOutOfScreen: "+mSelfPos.isOutOfScreen((int) mRadius));
        }
        return noBullets&&(mIsDead || mSelfPos.isOutOfScreen((int) mRadius));
    }

}
