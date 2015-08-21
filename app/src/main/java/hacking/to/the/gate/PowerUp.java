package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;

import java.util.MissingFormatArgumentException;

/**
 * Created by Ruiqian on 8/20/2015.
 */
public class PowerUp {

    private Position mCurPosition;
    private boolean mIsVisible;
    private Velocity mVelocity;
    private static final int mLifeCycle = 15000;
    private int mTimer;
    private boolean mIsStatic;
    private Paint mPaint;
    private float mRadius;
    public PowerUp(boolean isStatic,Position pos, float vx, float vy,Paint paint, float r){
        mIsStatic = isStatic;
        mCurPosition = pos;
        mPaint = paint;
        mVelocity = new Velocity(vx, vy);
        mTimer = 0;
        mIsVisible = true;

    }
    public Velocity getVelocity(){
        return mVelocity;
    }
    public void draw(Canvas canvas){
        canvas.drawCircle(mCurPosition.getPositionX(),mCurPosition.getPositionY(),mRadius,mPaint);
    }

    public void destroy(){
        mIsVisible = false;
    }

    public boolean isVisible(){
        return mIsVisible || mCurPosition.isOutOfScreen((int) mRadius);
    }

    public boolean checkHitBox(Bullet b){
        return mIsVisible
                &&(Math.pow(
                mCurPosition.getPositionX()-b.getSelfPos().getPositionX(),
                2)
                +Math.pow(
                mCurPosition.getPositionY() - b.getSelfPos().getPositionY(),
                2)
                <Math.pow(mRadius+b.getBulletR(),2));

    }
    public void tick(){
        if(mIsStatic){
            mTimer++;
            if(mTimer>mLifeCycle){
                mIsVisible = false;
            }
        }
        else{
            mCurPosition = new Position(mCurPosition.getPositionX()+mVelocity.getVelocityX(),mCurPosition.getPositionY()+mVelocity.getVelocityY());

        }

    }
}
