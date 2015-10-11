package hacking.to.the.gate;

import hacking.to.the.gate.ScriptParser.ICollider;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class Collider implements ICollider{
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

    //TODO:
    public boolean isOutOfScreen(){return false;};
}
