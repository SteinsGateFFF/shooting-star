package hacking.to.the.gate;

import android.graphics.Color;
import android.graphics.Paint;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/25.
 */
public class Gun {

    private int mShootingInterval = 1;
    private int mBulletDamage = 10;
    private long mFrameCount = 0;
    private GunStyle mGunStyle = null;
    private Paint mPaint = null;
    private Gun(int shootingInterval, int gunStyle, int bulletStyle, int bulletDamage, Paint paint){
        mShootingInterval = shootingInterval;
        mGunStyle = getGunStyle(gunStyle);
        //TODO: Add BulletStyle
        mBulletDamage = bulletDamage;
        mPaint = paint;

    }



    public List<Bullet> shoot(Position self,Position target) {

        mFrameCount++;
        if(mFrameCount%mShootingInterval==0 && mGunStyle!=null){
            return mGunStyle.generateBullets(self,target);
        } else {
            return new ArrayList<>();
        }
    }



    public final static int GUN_TYPE_DEFAULT = 0;
    public final static int GUN_TYPE_SELF_TARGETING = 1;

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

    public final static int GUN_STYLE_TYPE_NORMAL = 0;
    public final static int GUN_STYLE_TYPE_HOMING = 1;
    public final static int GUN_STYLE_TYPE_SELF_TARGETING = 2;

    private interface GunStyle {
        List<Bullet> generateBullets(Position self, Position target);
    }

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
                            // Indicate this is self jet
                            // TODO: Need more explicit indication.
                            throw new UnsupportedOperationException("Self targeting GunStyle need a target Position");
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
