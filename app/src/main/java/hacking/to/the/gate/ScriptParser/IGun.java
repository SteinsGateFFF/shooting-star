package hacking.to.the.gate.ScriptParser;

import java.util.ArrayList;

import hacking.to.the.gate.Collider;
import hacking.to.the.gate.Position;

/**
 * Created by yihuaqi on 2015/10/10.
 */
public interface IGun {
    ArrayList<Integer> getBulletStyles();

    float getBulletDamage();

    float getBulletMaxSpeed();

    Position getBulletPosition();

    Collider getBulletCollider();
}
