package hacking.to.the.gate.Hittables;

import java.util.ArrayList;
import java.util.List;

import hacking.to.the.gate.Collider;

/**
 * Created by Ruiqian on 10/1/2015.
 */
public class SuperHittable implements Hittable {
    private List<Hittable> mChildren;

    public SuperHittable(){
        mChildren = new ArrayList<>();

    }
    public void addAll(List<?extends Hittable> children){
        mChildren.addAll(children);

    }

    public void add(Hittable child){
        mChildren.add(child);
    }

    @Override
    public void onCollision(Hittable h) {

    }

    @Override
    public List<Hittable> getHittableChildren() {
        return mChildren;
    }

    @Override
    public Collider getCollider() {
        return null;
    }
}
