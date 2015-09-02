package hacking.to.the.gate;

/**
 *
 * Velocity is defined as the x and y displacement after one tick.
 *
 * Created by Jelly and Huaqi on 2015/8/16.
 */
public class Velocity {
    private final double mVelocityX;
    private final double mVelocityY;
    public Velocity(double x, double y){
        mVelocityX = x;
        mVelocityY = y;
    }

    public double getVelocityX() {
        return mVelocityX;
    }

    public double getVelocityY() {
        return mVelocityY;
    }

    public static Velocity getDestinationVelocity(Position self, Position dest, float maxSpeed) {
        if(dest.getPositionX() == self.getPositionX()){
            // Edge case fix
            return new Velocity(0,maxSpeed);
        }
        double ratio = (dest.getPositionY() - self.getPositionY())/(dest.getPositionX() - self.getPositionX());
        double vx = maxSpeed*Math.abs((float) Math.pow(1 / (1 + Math.pow(ratio, 2)), 0.5));
        if(dest.getPositionX()<self.getPositionX()){
            vx*=-1;
        }
        double vy = ratio * vx;

        return new Velocity(vx,vy);
    }

    /**
     *
     * @param degree
     * @return A new Velocity that rotate by given degree.
     */
    public Velocity rotate(double degree){
        double radian = Math.toRadians(degree);
        double vx = mVelocityX * Math.cos(radian) - mVelocityY * Math.sin(radian);
        double vy = mVelocityX * Math.sin(radian) + mVelocityY * Math.cos(radian);
        return new Velocity(vx,vy);
    }

    /**
     * Keeps the same velocity, but multiple the speed by rate.
     * @param rate
     * @return
     */
    public Velocity change(double rate){
        double vx = mVelocityX * rate;
        double vy = mVelocityY * rate;
        return new Velocity(vx,vy);
    }

    public float getSpeed() {
        return (float) Math.pow(Math.pow(mVelocityX,2)+Math.pow(mVelocityY,2),0.5);
    }

    public static Velocity getDisplacement(Position self, Position dest){
        return new Velocity(dest.getPositionX()-self.getPositionX(),
                dest.getPositionY()-self.getPositionY());
    }

    @Override
    public String toString() {
        return "Vx="+mVelocityX+" Vy="+mVelocityY;
    }



}
