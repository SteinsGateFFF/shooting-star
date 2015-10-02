package hacking.to.the.gate;

/**
 * Created by yihuaqi on 2015/9/1.
 */
public abstract class VelocityPattern {
    public static final int WORM = 0;
    public static final int SPIRAL = 1;
    public abstract Velocity nextVelocity(Velocity v);
}