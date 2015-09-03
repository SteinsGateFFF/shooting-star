package hacking.to.the.gate;

import android.graphics.Color;
import android.graphics.Paint;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private float mBulletDamage = 10;
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

    private int mBulletStyle = 0;

    /**
     * Construct a Gun instance.
     * @param shootingInterval
     * @param gunStyle
     * @param bulletStyle
     * @param bulletDamage
     * @param paint
     */
    private Gun(int shootingInterval, int gunStyle, int bulletStyle, float bulletDamage, Paint paint){
        mShootingInterval = shootingInterval;
        mGunStyle = getGunStyle(gunStyle);
        mBulletDamage = bulletDamage;
        mPaint = paint;
        mBulletStyle = bulletStyle;
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
     * Need a target
     */
    public final static int GUN_TYPE_SELF_TARGETING_ODD = 1;

    /**
     * //TODO: Self targeting gun types share certain common things. Sould make a sub class for handle general case.
     *
     */
    public final static int GUN_TYPE_SELF_TARGETING_EVEN = 2;

    /**
     * Burst a few bullets at random angle.
     * Need a target.
     */
    public final static int GUN_TYPE_RANDOM_SPLIT = 3;

    public final static int GUN_TYPE_WORM = 4;

    /**
     * Return an instance of gun of given type.
     * @param type
     * @return
     */
    public static Gun getGun(int type, int bulletStyle){
        // TODO: Need a way to easily distinguish enemy and self.
        Paint p = new Paint();
        switch (type) {
            case GUN_TYPE_DEFAULT:
                p.setColor(Color.WHITE);
                return new Gun(12, GUN_STYLE_TYPE_NORMAL, bulletStyle, 34, p);

            case GUN_TYPE_SELF_TARGETING_ODD:
                p.setColor(Color.RED);
                return new Gun(12, GUN_STYLE_TYPE_SELF_TARGETING_ODD, bulletStyle, 10,p);

            case GUN_TYPE_SELF_TARGETING_EVEN:
                p.setColor(Color.RED);
                return new Gun(12, GUN_STYLE_TYPE_SELF_TARGETING_EVEN, bulletStyle, 10,p);

            case GUN_TYPE_RANDOM_SPLIT:
                p.setColor(Color.YELLOW);
                return new Gun(60, GUN_STYLE_TYPE_RANDOM_SPLIT,bulletStyle, 10, p);

            default:
                throw new IllegalArgumentException("Invalid Gun Type");
        }

    }

    /**
     * Straight Bullet.
     */
    private final static int GUN_STYLE_TYPE_NORMAL = 0;
    /**
     * Keeps tracking the target
     */
    private final static int GUN_STYLE_TYPE_HOMING = 1;
    /**
     * Has an initial velocity toward self jet.
     * Can only be used for enemy jet.
     */
    private final static int GUN_STYLE_TYPE_SELF_TARGETING_ODD = 2;

    /**
     * Has an initial velocity toward but a few angle off the self jet
     * Can only be used for enemy jet.
     */
    private final static int GUN_STYLE_TYPE_SELF_TARGETING_EVEN = 3;

    /**
     * Burst a few bullets at random angle.
     * Should only be used for enemy jet.
     */
    private final static int GUN_STYLE_TYPE_RANDOM_SPLIT = 4;

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
     * //TODO: Bullet max speed should not be determined here.
     * @param type Possible types are {@link #GUN_STYLE_TYPE_SELF_TARGETING_ODD} {@link #GUN_STYLE_TYPE_NORMAL} and {@link #GUN_STYLE_TYPE_HOMING}
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
                            result.add(new Bullet(self,
                                    10,
                                    mPaint,
                                    new Velocity(0,-20),
                                    mBulletDamage,
                                    mBulletStyle));

                        } else {
                            // Indicate this is enemy jet
                            // TODO: Need more explicit indication.
                            result.add(new Bullet(self,
                                    10,
                                    mPaint,
                                    new Velocity(0,20),
                                    mBulletDamage,
                                    mBulletStyle));
                        }
                        return result;

                    }
                };
            case GUN_STYLE_TYPE_SELF_TARGETING_ODD:
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
                            Bullet b = new Bullet(self,
                                    10,
                                    mPaint,
                                    // TODO: Should get max speed from Bullet or Gun.
                                    Velocity.getDestinationVelocity(self, target, 10),
                                    mBulletDamage,
                                    mBulletStyle);

                            result.add(b);
                        }
                        return result;
                    }
                };

            case GUN_STYLE_TYPE_SELF_TARGETING_EVEN:
                return new GunStyle() {
                    @Override
                    public List<Bullet> generateBullets(Position self, Position target) {
                        List<Bullet> result = new ArrayList<>();
                        if(target==null){
                            // Self targeting must have a target
                            throw new IllegalArgumentException("Self targeting must have a target");
                        } else {
                            // Indicate this is enemy jet
                            // TODO: Need more explicit indication.
                            Bullet b1 = new Bullet(self,
                                    10,
                                    mPaint,
                                    // TODO: Should get max speed from Bullet or Gun.
                                    Velocity.getDestinationVelocity(self, target, 10).rotate(5),
                                    mBulletDamage,
                                    mBulletStyle);

                            Bullet b2 = new Bullet(self,
                                    10,
                                    mPaint,
                                    // TODO: Should get max speed from Bullet or Gun.
                                    Velocity.getDestinationVelocity(self, target, 20).rotate(-5),
                                    mBulletDamage,
                                    mBulletStyle);
                            result.add(b1);
                            result.add(b2);

                        }
                        return result;
                    }
                };

            case GUN_STYLE_TYPE_RANDOM_SPLIT:
                return new GunStyle() {
                    @Override
                    public List<Bullet> generateBullets(Position self, Position target) {
                        List<Bullet> result = new ArrayList<>();
                        if(target==null){
                            // Self targeting must have a target
                            throw new IllegalArgumentException("Self targeting must have a target");
                        } else {
                            // Indicate this is enemy jet
                            // TODO: Need more explicit indication.
                            Random random = new Random();
                            for (int i = 0; i < 5; i++){
                                int angle = random.nextInt(120);
                                Bullet b = new Bullet(self,
                                        10,
                                        mPaint,
                                        // TODO: Should get max speed from Bullet or Gun.
                                        Velocity.getDestinationVelocity(self, target, 15).rotate(angle - 60),
                                        mBulletDamage,
                                        mBulletStyle);
                                result.add(b);
                            }

                        }
                        return result;
                    }
                };

        }

        throw new IllegalArgumentException("Invalid Gun Style Type");
    }
}
