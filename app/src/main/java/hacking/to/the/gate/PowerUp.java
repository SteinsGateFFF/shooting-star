package hacking.to.the.gate;

import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Ruiqian on 8/24/2015.
 */
public class PowerUp {

    //declaration of variables

    //current position of the power up
    private Position mCurPosition;

    //the power up is visible or not
    private boolean mIsVisible;

    //velocity of the power up
    private Velocity mVelocity;

    //the life cycle a static power up can stay in Gameview
    private static final int mLifeCycle = 15000;

    //a timer used to count how long the power up has been existed
    private int mTimer;

    //whether or not a power up is static
    private boolean mIsStatic;

    //painter for the power up
    private Paint mPaint;

    //radius of the power up
    private float mRadius;

    //constructor
    public PowerUp(boolean isStatic,Position pos, float vx, float vy,Paint paint, float r){
        mIsStatic = isStatic;
        mCurPosition = pos;
        mPaint = paint;
        mVelocity = new Velocity(vx, vy);
        mTimer = 0;
        mIsVisible = true;
        mRadius = r;
    }
/*
 * draw power up on canvas
 *
 */
    public void draw(Canvas canvas){
        canvas.drawCircle(mCurPosition.getPositionX(),mCurPosition.getPositionY(),mRadius,mPaint);
    }
/*
 * check if the power up is visible
 */
    public boolean isVisible(){
        return mIsVisible || mCurPosition.isOutOfScreen((int) mRadius);
    }



}
