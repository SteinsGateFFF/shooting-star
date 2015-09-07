package hacking.to.the.gate;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public interface Hittable {
    CircleCollider getCollider();
    void onCollision(Hittable h);
}
