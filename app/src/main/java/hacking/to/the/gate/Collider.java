package hacking.to.the.gate;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class Collider {
    private Position mPos;
    public Collider(Position pos){
        mPos = pos;
    }

    public Position getPosition(){
        return mPos;
    }
    public void setPosition(Position pos){
        mPos = pos;
    }
}
