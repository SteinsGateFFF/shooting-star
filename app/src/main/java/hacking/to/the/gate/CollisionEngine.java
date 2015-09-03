package hacking.to.the.gate;

import android.util.Log;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public class CollisionEngine {

    public static boolean detectCollision(Hittable t1, Hittable t2){
        return detectCollision(t1.getCollider(),t2.getCollider());
    }

    private static boolean detectCollision(CircleCollider c1, CircleCollider c2){

        return Math.pow(
                c1.getPosition().getPositionX()-c2.getPosition().getPositionX(),
                2)
                +Math.pow(
                c1.getPosition().getPositionY()-c2.getPosition().getPositionY(),
                2)
                < Math.pow(c1.getRadius()+c2.getRadius(),2);

    }
}
