package hacking.to.the.gate.ScriptParser;

import android.content.Context;

import java.util.HashMap;

import hacking.to.the.gate.Gun;
import hacking.to.the.gate.Hittables.bullet.Bullet;
import hacking.to.the.gate.Hittables.jet.EnemyJet;
import hacking.to.the.gate.Hittables.jet.Jet;
import hacking.to.the.gate.Position;

/**
 * Created by yihuaqi on 2015/10/10.
 */
public class ScriptParser {
    private HashMap<String, Integer> mBulletAnimationMap;
    private HashMap<String, Integer> mJetAnimationMap;
    private HashMap<String, IJet> mJetMap;
    private HashMap<String, IBullet> mBulletMap;
    private HashMap<String, IGun> mGunMap;
    private HashMap<String, IBomb> mBombMap;
    private HashMap<String, IEvent> mEventMap;
    private HashMap<String, IPowerup> mPowerupMap;
    private HashMap<String, ICollider> mColliderMap;
    private HashMap<String, Position> mPositionMap;
    private HashMap<String, Integer> mGunTypeMap;
    private HashMap<String, Integer> mBulletStyleMap;


    private static ScriptParser instance;
    private Context mContext;
    private ScriptParser(){};
    public static ScriptParser getInstance(){
        if(instance==null){
            instance = new ScriptParser();
        }
        return instance;
    }

    public void onCreate(Context context){
        mContext = context;
    };

    public void parseFromFile(String filename){
        //TODO:
    }

    public void parseFromString(String sentence){

    }

    private static final String DEFINE_BULLET =
            "define bullet1: Bullet(gun: Gun)" +
            "  BulletAnimation=bulletAnim1"+
            "  BulletStyles=gun.BulletStyles"+
            "  Damage=gun.BulletDamage"+
            "  MaxSpeed=gun.BulletMaxSpeed"+
            "  Position=gun.BulletPosition"+
            "  onCollision=bulletCollision1"+
            "  Collider=gun.BulletCollider";

    /**
     * Should do the same thing as parseFromString(DEFINE_BULLET);
     */
    private void testDefineBullet(IGun gun){
        IBullet bullet = new Bullet.Builder()
                .setAnimation(mBulletAnimationMap.get("bulletAnim1"))
                .setBulletStyles(gun.getBulletStyles())
                .setDamage(gun.getBulletDamage())
                .setMaxSpeed(gun.getBulletMaxSpeed())
                .setSelfPos(gun.getBulletPosition())
                .setOnCollision(mEventMap.get("bulletCollision1"))
                .setCollider(gun.getBulletCollider())
                .build();
        mBulletMap.put("bullet1",bullet);
    }

    private static final String DEFINE_JET =
            "define enemyJet1: EnemyJet"+
                    "  Collider=collider1"+
                    "  Position=position1"+
                    "  Animation=animation1"+
                    "  Health=100"+
                    "  MaxSpeed=50"+
                    "  GunType='SELF_TARGETING_EVEN'"+
                    "  BulletStyles='DEFAULT'";

    private void testDefineJet(){
        IJet jet = new EnemyJet.Builder()
                .setCollider(mColliderMap.get("collider1"))
                .setPosition(mPositionMap.get("position1"))
                .setAnimation(mJetAnimationMap.get("animation1"))
                .setHealth(100)
                .setMaxSpeed(50)
                //TODO: Should use enum
                .setGunType(mGunTypeMap.get("SELF_TARGETING_EVEN"))
                .setBulletStyle(mBulletStyleMap.get("DEFAULT"))
                .build();
    }
}
