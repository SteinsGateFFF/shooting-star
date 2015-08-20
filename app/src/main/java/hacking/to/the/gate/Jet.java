package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class Jet {
    private Position mSelfPos;
    private Position mDestPos;
    private float mRadius;
    private Paint mPaint;
    private Velocity mVelocity;
    private float mHealth;
    private final String TAG = "Jet";
    private float mMaxSpeed;
    private boolean mHasDestination;
    private List<Bullet> mBullets;
    private int SHOOTING_RATE = 1;
    private int frameCount = 0;

    public Jet(float x, float y, float r, Paint p){
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
        mBullets = new LinkedList<>();
    }

    public void draw(Canvas canvas){
        if(!mIsDead) {
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        }
        for(Bullet b:mBullets){
            b.onDraw(canvas);
        }

    }

    public void tick(Bullet bullet){
        Log.d(TAG,"Bullets: "+mBullets.size());
        if(mHasDestination) {
            mVelocity = Velocity.getDestinationVelocity(mSelfPos, mDestPos, mMaxSpeed);

        } else {
            mVelocity = new Velocity(0,0);

        }
        mSelfPos = mSelfPos.applyVelocity(mVelocity);
        shoot(bullet);
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
    private boolean checkHitBox(Bullet b){
        return !mIsDead
                && (Math.pow(
                mSelfPos.getPositionX()-b.getSelfPos().getPositionX(),
                2)
                +Math.pow(
                mSelfPos.getPositionY()-b.getSelfPos().getPositionY(),
                2)
                < Math.pow(mRadius+b.getBulletR(),2));

    }
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
    public void setDestination(float x, float y, boolean hasDestination ){
        mDestPos = new Position(x,y);
        mHasDestination = hasDestination;

    }

    public void shoot(){
        mBullets.add(new Bullet(mSelfPos, 10, mPaint, 0, -20));
    }

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
