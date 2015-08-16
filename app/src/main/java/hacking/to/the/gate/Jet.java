package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Iterator;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/15.
 */
public class Jet {
    private float mJetX;
    private float mJetY;
    private float mRadius;
    private Paint mPaint;
    private float mVelocityX;
    private float mVelocityY;
    private float mHealth;
    public Jet(float x, float y, float r, Paint p, float vx, float vy){
        mJetX = x;
        mJetY = y;
        mRadius = r;
        mPaint = p;
        mVelocityX = vx;
        mVelocityY = vy;
        mHealth = 100;
    }
    public void onDraw(Canvas canvas){
        if(!mIsDead) {
            canvas.drawCircle(mJetX, mJetY, mRadius, mPaint);
            mJetX += mVelocityX;
            mJetY += mVelocityY;

        }
        canvas.drawText("Health: "+mHealth,10,10,mPaint);
    }
    public void setJetX(float x){
        mJetX = x;

    }
    public void setJetY(float y){
        mJetY = y;
    }
    public float getJetX(){
        return mJetX;
    }
    public float getJetY(){
        return mJetY;
    }
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
        return !mIsDead && Math.pow((mJetX-b.getBulletX()),2)+Math.pow((mJetY-b.getBulletY()),2)<Math.pow(mRadius+b.getBulletR(),2);

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
}
