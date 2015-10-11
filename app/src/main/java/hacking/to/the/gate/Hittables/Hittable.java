package hacking.to.the.gate.Hittables;

import java.util.List;

import hacking.to.the.gate.Collider;
import hacking.to.the.gate.ScriptParser.ICollider;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public interface Hittable {
    ICollider getCollider();
    List<? extends Hittable> getHittableChildren();
    void onCollision(Hittable h);
}
