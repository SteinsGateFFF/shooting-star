package hacking.to.the.gate;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class BoxCollider extends Collider{
    private float mWidth;
    private float mHeight;
    public BoxCollider(float width, float height,Position pos){
        super(pos);
        mWidth = width;
        mHeight = height;

    }

    public float getWidth(){
        return mWidth;
    }
    public float getHeight(){return mHeight;}
}
