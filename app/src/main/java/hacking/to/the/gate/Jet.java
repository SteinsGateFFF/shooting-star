package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/15.
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
    public Jet(float x, float y, float r, Paint p){
        mSelfPos = new Position(x,y);
        mRadius = r;
        mPaint = p;
        mHealth = 100;
        mMaxSpeed = 20;
        mHasDestination = false;
    }

    public void onDraw(Canvas canvas){
        if(!mIsDead) {

            if(mHasDestination) {
                mVelocity = Velocity.getDestinationVelocity(mSelfPos, mDestPos, mMaxSpeed);
                Log.d(TAG,"HasDestination: "+mVelocity );
            } else {
                mVelocity = new Velocity(0,0);
                Log.d(TAG,"No Destination: "+mVelocity);
            }
            mSelfPos = mSelfPos.applyVelocity(mVelocity);
            canvas.drawCircle(mSelfPos.getPositionX(), mSelfPos.getPositionY(), mRadius, mPaint);
        }
        canvas.drawText("Health: "+mHealth,10,10,mPaint);
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


                it.remove();

            }

        }

    }
    public void setDestination(float x, float y, boolean hasDestination ){
        mDestPos = new Position(x,y);
        mHasDestination = hasDestination;

    }
}
