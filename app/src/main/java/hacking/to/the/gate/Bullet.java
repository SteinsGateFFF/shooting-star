package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by yihuaqi on 2015/8/15.
 */
public class Bullet {
    private float mBulletX;
    private float mBulletY;
    private float mBulletR;
    private float mVelocityX;
    private float mVelocityY;
    private Paint mPaint;
    public Bullet(float x, float y, float r, Paint p, float vx, float vy){
        mBulletR = r;
        mBulletX = x;
        mBulletY = y;
        mPaint = p;
        mVelocityX = vx;
        mVelocityY = vy;
    }

    public float getBulletX() {
        return mBulletX;
    }

    public float getBulletY() {
        return mBulletY;
    }

    public float getBulletR() {
        return mBulletR;
    }

    public void onDraw(Canvas canvas){
        canvas.drawCircle(mBulletX,mBulletY,mBulletR,mPaint);
        mBulletX += mVelocityX;
        mBulletY += mVelocityY;


    }
}
