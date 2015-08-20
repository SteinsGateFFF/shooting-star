package hacking.to.the.gate;

import android.graphics.Rect;

/**
 * Created by Jelly and Huaqi on 2015/8/16.
 */
public class Position {
    private final float mPositionX;
    private final float mPositionY;
    public Position(float x, float y){
        mPositionX = x;
        mPositionY = y;
    }

    public float getPositionX() {
        return mPositionX;
    }

    public float getPositionY() {
        return mPositionY;
    }

    public Position applyVelocity(Velocity v){
        return new Position(mPositionX+v.getVelocityX(),mPositionY+v.getVelocityY());
    }

    public boolean isOutOfScreen(int radius) {
        Rect screenRect = GameManager.getInstance().getScreenRect();
        int width = screenRect.width();
        int height = screenRect.height();
        Rect rectWithRadius = new Rect(-radius,-radius,width+radius,height+radius);
        return !rectWithRadius.contains((int) mPositionX, (int) mPositionY);

    }

}
