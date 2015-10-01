package hacking.to.the.gate.Hittables;

import java.util.List;

import hacking.to.the.gate.Collider;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public interface Hittable {
    Collider getCollider();
    List<? extends Hittable> getHittableChildren();
    void onCollision(Hittable h);
}
