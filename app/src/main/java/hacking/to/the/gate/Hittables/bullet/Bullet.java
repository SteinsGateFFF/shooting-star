package hacking.to.the.gate.Hittables.bullet;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import hacking.to.the.gate.CircleCollider;
import hacking.to.the.gate.Collider;
import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Hittables.bomb.ATFieldBomb;
import hacking.to.the.gate.Hittables.bomb.AtomicBomb;
import hacking.to.the.gate.Hittables.jet.EnemyJet;
import hacking.to.the.gate.Hittables.jet.FriendJet;
import hacking.to.the.gate.Hittables.jet.SelfJet;
import hacking.to.the.gate.Position;
import hacking.to.the.gate.ScriptParser.IBullet;
import hacking.to.the.gate.ScriptParser.IEvent;
import hacking.to.the.gate.Velocity;
import hacking.to.the.gate.VelocityPattern;
import hacking.to.the.gate.VelocityPatternFactory;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Bullet implements Hittable, IBullet {
    private Collider mCollider;
    private Position mSelfPos;
    private Velocity mVelocity;

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
    //TODO:
    private IEvent onCollision;

    public List<Hittable> getHittableChildren(){
        return null;
    }
	public Collider getCollider(){
        return mCollider;
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

    public Bullet(Position pos, Collider c, Velocity v, float damage, ArrayList<Integer> bulletStyles, float maxSpeed){
    
        mCollider = c;

        mSelfPos = pos;

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



    public void draw(Canvas canvas){

        mAnimation.draw(canvas,mSelfPos);

    }

    public void tick(){

        if(mVelocityPatternCounter>mVelocityPatterns.size()-1){
            mVelocityPatternCounter=0;
        }
        if(mVelocityPatterns!=null && mVelocityPatterns.size()!= 0) {
            if(mVelocityPatterns.get(mVelocityPatternCounter)!= null) {
                mVelocity = mVelocityPatterns.get(mVelocityPatternCounter).nextVelocity(mVelocity);
               // Log.d("Bullet-velocity: ",""+velocity+" speed"+velocity.getSpeed());
            }
        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        mCollider.setPosition(mSelfPos);
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
        return shouldRecycle
                || (mCollider instanceof CircleCollider)?
                mSelfPos.isOutOfScreen((int) ((CircleCollider) mCollider).getRadius()):
                false;
    }

    @Override
    public String toString() {
        return mSelfPos.toString();
    }

    public void setOnCollision(IEvent onCollision) {
        this.onCollision = onCollision;
    }

    public IEvent getOnCollision() {
        return onCollision;
    }

    public static class Builder<T extends Builder>{
        protected Collider collider;
        protected Position selfpos;
        protected Velocity velocity;
        protected ArrayList<Integer> velocityPatterns;
        protected float maxSpeed;
        protected float damage;
        protected int animation;
        protected IEvent onCollisionEvent;

        public Builder(){

        }

        public T setCollider(Collider c){
            collider = c;
            return (T) this;
        }

        public T setSelfPos(Position p){
            selfpos = p;
            return (T) this;
        }

        public T setVelocity(Velocity v){
            velocity = v;
            return (T) this;
        }

        public T setBulletStyles(ArrayList<Integer> list){
            velocityPatterns = list;
            return (T) this;
        }

        public T setMaxSpeed(float maxSpeed){
            this.maxSpeed = maxSpeed;
            return (T) this;
        }

        public T setDamage(float damage){
            this.damage = damage;
            return (T) this;
        }

        public T setAnimation(int type){
            this.animation = type;
            return (T) this;
        }

        public T setOnCollision(IEvent event){
            this.onCollisionEvent = event;
            return (T) this;
        }

        public Bullet build() {
            Bullet b = new Bullet(selfpos, collider, velocity, damage, velocityPatterns, maxSpeed);
            b.setBulletAnimation(animation);
            b.setOnCollision(onCollisionEvent);
            return b;
        }
    }
}
