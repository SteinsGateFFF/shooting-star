package hacking.to.the.gate;

import android.graphics.Rect;

/**
 * Created by Jelly and Huaqi on 2015/8/16.
 */
public class Position {
    private final double mPositionX;
    private final double mPositionY;
    public Position(double x, double y){
        mPositionX = x;
        mPositionY = y;
    }

    public float getPositionX() {
        return (float)mPositionX;
    }

    public float getPositionY() {

        return (float)mPositionY;

    }

    /**
     *
     * @param v
     * @return a new Position that has been applied by the given velocity
     */
    public Position applyVelocity(Velocity v){
        return new Position(mPositionX+v.getVelocityX(),mPositionY+v.getVelocityY());
    }

    /**
     *
     * @param radius
     * @return true if the circle with this position and given radius is totally out of the screen.
     */
    public boolean isOutOfScreen(int radius) {
        Rect screenRect = GameManager.getInstance().getScreenRect();
        int width = screenRect.width();
        int height = screenRect.height();
        Rect rectWithRadius = new Rect(-radius,-radius,width+radius,height+radius);
        return !rectWithRadius.contains((int) mPositionX, (int) mPositionY);

    }

    @Override
    public String toString() {
        return "<"+mPositionX+","+mPositionY+">";
    }
}
