package hacking.to.the.gate.Hittables.jet;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import hacking.to.the.gate.Hittables.bullet.Bullet;
import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.Velocity;

/**
 * Created by Ruiqian on 9/8/2015.
 */
public abstract class Jet implements Hittable{

    public static class Builder<T extends Builder>{
        //Required parameters

        //optional parameters
        protected float mRadius;
        protected Position mSelfPos;
        protected JetAnimation mAnimation;
        protected CircleCollider collider;

        protected float mHealth = 100;
        protected float mMaxSpeed = 20;
        protected boolean mHasDestination = false;
        protected int mGunType = Gun.GUN_TYPE_DEFAULT;
        protected int mBulletStyle = Bullet.BULLET_STYLE_DEFAULT;


        public Builder(){
            mRadius = 50;
            mSelfPos = new Position(0,0);
            mAnimation = JetAnimation.getInstance(JetAnimation.TYPE_ENEMY_JET_0);
            collider = new CircleCollider(mRadius, mSelfPos);
        }

        public T radius(int val){
            mRadius = val;
            return (T) this;
        }

        public T selfPosition(float xPosition,float yPosition){
            mSelfPos = new Position(xPosition,yPosition);
            return (T) this;
        }

        public T animationType(int val)
        {
            if(val != JetAnimation.TYPE_ENEMY_JET_0 && val!= JetAnimation.TYPE_SELF_JET){
                throw new IllegalArgumentException("Type provided is not valid");
            }
            else{
                mAnimation = JetAnimation.getInstance(val);
            }
            return (T) this;
        }

        public T health(int val){
            mHealth = val;
            return (T) this;
        }

        public T maxSpeed(int val){
            mMaxSpeed = val;
            return (T)this;
        }

        public T hasDestination(boolean val){
            mHasDestination = val;
            return (T)this;
        }

        public T setGunType(int val){
            mGunType = val;
            return (T)this;
        }

        public T setBulletStyle(int val){
            mBulletStyle = val;
            return (T)this;
        }
    }
    public CircleCollider getCollider(){
        return collider;
    }

    private CircleCollider collider;
    /**
     * Position of the center of the circle that represents this hacking.to.the.gate.Hittables.Hittable.jet.
     */
    protected Position mSelfPos;
    /**
     * Position of the destination position that the hacking.to.the.gate.Hittables.Hittable.jet is heading to.
     */
    private Position mDestPos;
    private float mRadius;
    /**
     * The current velocity of the hacking.to.the.gate.Hittables.Hittable.jet.
     */
    protected Velocity mVelocity;
    /**
     * Health of the hacking.to.the.gate.Hittables.Hittable.jet. Jet is destroyed if Heath < 0
     */
    private float mHealth;
    private final String TAG = "Jet";
    /**
     * The speed limit that the hacking.to.the.gate.Hittables.Hittable.jet can move.
     */
    private float mMaxSpeed;
    /**
     * If the hacking.to.the.gate.Hittables.Hittable.jet has a destination.
     */
    private boolean mHasDestination;
    /**
     * List of Bullets that are shot by this hacking.to.the.gate.Hittables.Hittable.jet.
     */
    protected List<Bullet> mBullets;

    protected List<Gun>  mGuns;

    protected ArrayList<Integer> mBulletStyles;

    private int mGunType;

    private int mBulletStyle;

    /**
     * Create a Jet Object
     * @param x x coordinate of the center of the hacking.to.the.gate.Hittables.Hittable.jet
     * @param y y coordinate of the center of the hacking.to.the.gate.Hittables.Hittable.jet
     * @param r radius of the hacking.to.the.gate.Hittables.Hittable.jet
     * @param p paint that paints the hacking.to.the.gate.Hittables.Hittable.jet and bullet
     *
     */

    private JetAnimation mAnimation;
    protected boolean mIsDead = false;
    protected Jet(Builder builder){

        collider = builder.collider;
        mSelfPos = builder.mSelfPos;
        mRadius = builder.mRadius;
        mHealth = builder.mHealth;
        mMaxSpeed = builder.mMaxSpeed;
        mHasDestination = builder.mHasDestination;
        mAnimation = builder.mAnimation;
        mGunType = builder.mGunType;
        mBulletStyle = builder.mBulletStyle;
        mGuns = new LinkedList<>();
        mBulletStyles = new ArrayList<>();
        mBullets = new LinkedList<>();
        mBulletStyles.add(mBulletStyle);
        mGuns.add(Gun.getGun(mGunType,mBulletStyles));

    }

    /**
     * Set the gun of this hacking.to.the.gate.Hittables.Hittable.jet to the given type
     * @param gunType
     */
    public void setGunType(int index,int gunType, ArrayList<Integer> bulletStyles){

        if(index < mGuns.size()){
            mGuns.set(index, Gun.getGun(gunType, bulletStyles));
        } else {
            throw new IndexOutOfBoundsException("Index: "+index+" is equal or larger than Guns size: "+mGuns.size());
        }
    }

    /**
     * This should only be called after guns are set.
     * @param animationType
     */
    public void setBulletAnimation(int animationType){
        for(Gun gun:mGuns){
            gun.setBulletAnimation(animationType);
        }
    }

    public int getNumOfGuns(){
        return mGuns.size();
    }


    /**
     * Draw the hacking.to.the.gate.Hittables.Hittable.jet to the given canvas.
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
     * Set the destination of the hacking.to.the.gate.Hittables.Hittable.jet.
     * @param x
     * @param y
     * @param hasDestination true then hacking.to.the.gate.Hittables.Hittable.jet will move towards the destination, otherwise will stay.
     */
    public void setDestination(float x, float y, boolean hasDestination ){
        mDestPos = new Position(x,y);
        mHasDestination = hasDestination;
        Log.d("destination", "Set self destination: " + mDestPos);
    }

    /**
     *
     * @return a list of bullets that are shot by this hacking.to.the.gate.Hittables.Hittable.jet.
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
     * Change the hacking.to.the.gate.Hittables.Hittable.jet state to next tick.
     */
    public void tick(){

        if(mHasDestination) {
            // Fixed a bug that the ject have weird movement if destination is at the center of the hacking.to.the.gate.Hittables.Hittable.jet.
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
