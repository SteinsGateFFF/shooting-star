package hacking.to.the.gate.ScriptParser;

import hacking.to.the.gate.Position;

/**
 * Created by yihuaqi on 2015/10/10.
 */
public interface ICollider {
    void setPosition(Position mSelfPos);
    boolean isOutOfScreen();
}
