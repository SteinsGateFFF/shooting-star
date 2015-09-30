package hacking.to.the.gate;

import java.util.List;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public interface Hittable {
    Collider getCollider();
    List<? extends Hittable> getHittableChildren();
    void onCollision(Hittable h);
}
