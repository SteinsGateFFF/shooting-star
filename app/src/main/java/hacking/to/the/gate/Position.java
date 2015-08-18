package hacking.to.the.gate;

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
}
