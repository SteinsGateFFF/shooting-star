package hacking.to.the.gate;

/**
 * Created by Ruiqian on 8/31/2015.
 */
public class CircleCollider extends Collider{

    private float mRadius;
    public CircleCollider(float r, Position pos){
        super(pos);
        mRadius = r;

    }
    public float getRadius(){
        return mRadius;
    }
}
