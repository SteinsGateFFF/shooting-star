package hacking.to.the.gate;

/**
 * Created by Ruiqian on 8/31/2015.
 */
public class CircleCollider {
    private Position mPos;
    private float mRadius;
    public CircleCollider(float r, Position pos){
        mPos = pos;
        mRadius = r;

    }
    public Position getPosition(){
        return mPos;
    }
    public float getRadius(){
        return mRadius;
    }
    public void setPosition(Position pos){
        mPos = pos;
    }
}
