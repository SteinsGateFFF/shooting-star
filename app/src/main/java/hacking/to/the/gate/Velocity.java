package hacking.to.the.gate;

/**
 *
 * Velocity is defined as the x and y displacement after one tick.
 *
 * Created by Jelly and Huaqi on 2015/8/16.
 */
public class Velocity {
    private final float mVelocityX;
    private final float mVelocityY;
    public Velocity(float x, float y){
        mVelocityX = x;
        mVelocityY = y;
    }

    public float getVelocityX() {
        return mVelocityX;
    }

    public float getVelocityY() {
        return mVelocityY;
    }

    public static Velocity getDestinationVelocity(Position self, Position dest, float maxSpeed) {
        if(dest.getPositionX() == self.getPositionX()){
            // Edge case fix
            return new Velocity(0,maxSpeed);
        }
        float ratio = (dest.getPositionY() - self.getPositionY())/(dest.getPositionX() - self.getPositionX());
        float vx = maxSpeed*Math.abs((float) Math.pow(1 / (1 + Math.pow(ratio, 2)), 0.5));
        if(dest.getPositionX()<self.getPositionX()){
            vx*=-1;
        }
        float vy = ratio * vx;

        return new Velocity(vx,vy);
    }


    public static Velocity getDisplacement(Position self, Position dest){
        return new Velocity(dest.getPositionX()-self.getPositionX(),
                dest.getPositionY()-self.getPositionY());
    }

    @Override
    public String toString() {
        return "Vx="+mVelocityX+" Vy="+mVelocityY;
    }

    public static interface VelocityPattern {
        Velocity nextVelocity(Velocity v);
    }

}
