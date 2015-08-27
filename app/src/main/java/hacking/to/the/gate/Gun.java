package hacking.to.the.gate;

import android.graphics.Color;
import android.graphics.Paint;


import java.util.ArrayList;
import java.util.List;

/**
 * Gun can determines how the bullets are shot from jet.
 */
public class Gun {
    /**
     * Interval of ticks between two shots.
     */
    private int mShootingInterval = 1;
    /**
     * Damage every bullet can deal.
     * TODO: This has not been implemented yet.
     */
    private int mBulletDamage = 10;
    /**
     * Number of frames that has passed by.
     * TODO: Should count tick instead.
     */
    private long mFrameCount = 0;
    /**
     * {@link hacking.to.the.gate.Gun.GunStyle}
     */
    private GunStyle mGunStyle = null;
    /**
     * Paint of the bullet.
     */
    private Paint mPaint = null;

    /**
     * Construct a Gun instance.
     * @param shootingInterval
     * @param gunStyle
     * @param bulletStyle
     * @param bulletDamage
     * @param paint
     */
    private Gun(int shootingInterval, int gunStyle, int bulletStyle, int bulletDamage, Paint paint){
        mShootingInterval = shootingInterval;
        mGunStyle = getGunStyle(gunStyle);
        //TODO: Add BulletStyle
        mBulletDamage = bulletDamage;
        mPaint = paint;

    }


    /**
     * Checks the {@link #mFrameCount} to see if it should shoot.
     * @param self
     * @param target
     * @return a list of bullets that should be added to the game at current tick.
     */
    public List<Bullet> tick(Position self, Position target) {

        mFrameCount++;
        if(mFrameCount%mShootingInterval==0 && mGunStyle!=null){
            return mGunStyle.generateBullets(self,target);
        } else {
            return new ArrayList<>();
        }
    }


    /**
     * Straight Bullets
     */
    public final static int GUN_TYPE_DEFAULT = 0;
    /**
     * Bullets that has the initial velocity toward self jet.
     * Can only be used for enemy jet.
     */
    public final static int GUN_TYPE_SELF_TARGETING = 1;

    /**
     * Return an instance of gun of given type.
     * @param type
     * @return
     */
    public static Gun getGun(int type){
        Paint p = new Paint();
        switch (type) {
            case GUN_TYPE_DEFAULT:

                p.setColor(Color.WHITE);
                return new Gun(12, GUN_STYLE_TYPE_NORMAL, 0, 10, p);

            case GUN_TYPE_SELF_TARGETING:

                p.setColor(Color.RED);
                return new Gun(12, GUN_STYLE_TYPE_SELF_TARGETING, 0, 10,p);

            default:
                throw new IllegalArgumentException("Invalid Gun Type");
        }

    }

    /**
     * Straight Bullet.
     */
    public final static int GUN_STYLE_TYPE_NORMAL = 0;
    /**
     * Keeps tracking the target
     */
    public final static int GUN_STYLE_TYPE_HOMING = 1;
    /**
     * Has an initial velocity toward self jet.
     * Can only be used for enemy jet.
     */
    public final static int GUN_STYLE_TYPE_SELF_TARGETING = 2;

    /**
     * Determines how the bullets will get generated.
     */
    private interface GunStyle {
        /**
         * Generates a list of bullets.
         * @param self {@link hacking.to.the.gate.Position} of the jet that shoots.
         * @param target {@link hacking.to.the.gate.Position} of the jet that is the target. Can be null.
         * @return
         */
        List<Bullet> generateBullets(Position self, Position target);
    }

    /**
     *
     * @param type Possible types are {@link #GUN_STYLE_TYPE_SELF_TARGETING} {@link #GUN_STYLE_TYPE_NORMAL} and {@link #GUN_STYLE_TYPE_HOMING}
     * @return the given type of GunStyle
     */
    private GunStyle getGunStyle(int type) {
        switch (type){
            case GUN_STYLE_TYPE_NORMAL:
                return new GunStyle() {
                    @Override
                    public List<Bullet> generateBullets(Position self, Position target) {
                        List<Bullet> result = new ArrayList<>();
                        if(target == null) {
                            // Indicate this is self jet
                            // TODO: Need more explicit indication.
                            result.add(new Bullet(self,10,mPaint,0,-20));

                        } else {
                            // Indicate this is enemy jet
                            // TODO: Need more explicit indication.
                            result.add(new Bullet(self,10,mPaint,0,20));
                        }
                        return result;

                    }
                };
            case GUN_STYLE_TYPE_SELF_TARGETING:
                return new GunStyle() {
                    @Override
                    public List<Bullet> generateBullets(Position self, Position target) {
                        List<Bullet> result = new ArrayList<>();
                        if(target==null){
                            // Self targeting must have a target.
                            // TODO: Need more explicit indication.
                            throw new IllegalArgumentException("Self targeting must have a target");
                        } else {
                            // Indicate this is enemy jet
                            // TODO: Need more explicit indication.
                            Bullet b = new Bullet(self,10,mPaint,0,20);
                            b.setDestination(target,true);
                            result.add(b);
                        }
                        return result;
                    }
                };

        }

        throw new IllegalArgumentException("Invalid Gun Style Type");
    }
}
