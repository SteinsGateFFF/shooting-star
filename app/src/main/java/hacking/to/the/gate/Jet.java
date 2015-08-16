package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Paint;

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
    public Jet(float x, float y, float r, Paint p, float vx, float vy){
        mJetX = x;
        mJetY = y;
        mRadius = r;
        mPaint = p;
        mVelocityX = vx;
        mVelocityY = vy;
    }
    public void onDraw(Canvas canvas){
        canvas.drawCircle(mJetX,mJetY,mRadius,mPaint);
        mJetX+=mVelocityX;
        mJetY += mVelocityY;
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
}
