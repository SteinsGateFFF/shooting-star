package hacking.to.the.gate.Hittables.bullet;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import hacking.to.the.gate.Position;

/**
 * Created by yihuaqi on 2015/9/10.
 */
public class BulletAnimation {
    private static Bitmap mBulletBitmap1;

    public static final int TYPE_SELF_BULLET = 0;
    public static final int TYPE_BULLET_1 = 1;
    public static final int TYPE_BULLET_2 = 2;
    public static final int TYPE_BULLET_3 = 3;
    public static final int TYPE_BULLET_4 = 4;
    public static final int TYPE_BULLET_5 = 5;
    public static final int TYPE_BULLET_6 = 6;
    public static final int TYPE_BULLET_7 = 7;
    public static final int TYPE_BULLET_8 = 8;
    public static final int TYPE_BULLET_9 = 9;
    public static final int TYPE_BULLET_10 = 10;

    private static final List<Rect> SELF_BULLET_RECTS = new ArrayList<>();
    private static final int SELF_BULLET_WIDTH = 16;
    private static final int SELF_BULLET_HEIGHT = 16;

    private static final List<Rect> BULLET_1_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_2_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_3_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_4_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_5_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_6_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_7_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_8_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_9_RECTS = new ArrayList<>();
    private static final List<Rect> BULLET_10_RECTS = new ArrayList<>();
    private static final int BULLET_WIDTH_0 = 16;
    private static final int BULLET_HEIGHT_0 = 16;

    public static void init(Context context) throws IOException {
        for(int i = 0; i < 16; i++) {
            // For different colors.
            // TODO: How can this become consistent with JetAnimation.
            SELF_BULLET_RECTS.add(new Rect(i*SELF_BULLET_WIDTH,16, (i+1)*SELF_BULLET_WIDTH,16+SELF_BULLET_HEIGHT));
            BULLET_1_RECTS.add(new Rect(i*BULLET_WIDTH_0, 32, (i+1)*BULLET_WIDTH_0, 32 + BULLET_HEIGHT_0));
            BULLET_2_RECTS.add(new Rect(i*BULLET_WIDTH_0, 48, (i+1)*BULLET_WIDTH_0, 48 + BULLET_HEIGHT_0));
            BULLET_3_RECTS.add(new Rect(i*BULLET_WIDTH_0, 64, (i+1)*BULLET_WIDTH_0, 64 + BULLET_HEIGHT_0));
            BULLET_4_RECTS.add(new Rect(i*BULLET_WIDTH_0, 80, (i+1)*BULLET_WIDTH_0, 80 + BULLET_HEIGHT_0));
            BULLET_5_RECTS.add(new Rect(i*BULLET_WIDTH_0, 96, (i+1)*BULLET_WIDTH_0, 96 + BULLET_HEIGHT_0));
            BULLET_6_RECTS.add(new Rect(i*BULLET_WIDTH_0, 112, (i+1)*BULLET_WIDTH_0, 112 + BULLET_HEIGHT_0));
            BULLET_7_RECTS.add(new Rect(i*BULLET_WIDTH_0, 128, (i+1)*BULLET_WIDTH_0, 128 + BULLET_HEIGHT_0));
            BULLET_8_RECTS.add(new Rect(i*BULLET_WIDTH_0, 144, (i+1)*BULLET_WIDTH_0, 144 + BULLET_HEIGHT_0));
            BULLET_9_RECTS.add(new Rect(i*BULLET_WIDTH_0, 160, (i+1)*BULLET_WIDTH_0, 160 + BULLET_HEIGHT_0));
            BULLET_10_RECTS.add(new Rect(i*BULLET_WIDTH_0, 176, (i+1)*BULLET_WIDTH_0, 176 + BULLET_HEIGHT_0));





        }



        mBulletBitmap1 = getBitmapFromAssets(context, "data/bullet/bullet1.png");
    }


    private static Bitmap getBitmapFromAssets(Context context, String filePath) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(filePath);
        return BitmapFactory.decodeStream(is);
    }

    public static BulletAnimation getInstance(int type){
        switch (type){
            case TYPE_SELF_BULLET:
                return new BulletAnimation(mBulletBitmap1, SELF_BULLET_RECTS);
            case TYPE_BULLET_1:
                return new BulletAnimation(mBulletBitmap1, BULLET_1_RECTS);
            case TYPE_BULLET_2:
                return new BulletAnimation(mBulletBitmap1, BULLET_2_RECTS);
            case TYPE_BULLET_3:
                return new BulletAnimation(mBulletBitmap1, BULLET_3_RECTS);
            case TYPE_BULLET_4:
                return new BulletAnimation(mBulletBitmap1, BULLET_4_RECTS);
            case TYPE_BULLET_5:
                return new BulletAnimation(mBulletBitmap1,BULLET_5_RECTS);
            case TYPE_BULLET_6:
                return new BulletAnimation(mBulletBitmap1,BULLET_6_RECTS);
            case TYPE_BULLET_7:
                return new BulletAnimation(mBulletBitmap1,BULLET_7_RECTS);
            case TYPE_BULLET_8:
                return new BulletAnimation(mBulletBitmap1,BULLET_8_RECTS);
            case TYPE_BULLET_9:
                return new BulletAnimation(mBulletBitmap1,BULLET_9_RECTS);
            case TYPE_BULLET_10:
                return new BulletAnimation(mBulletBitmap1,BULLET_10_RECTS);

            default: throw new IllegalArgumentException("Invalid Jet Animation Type");
        }
    }

    private Bitmap mBitmap;

    private List<Rect> mRect;

    private long mTick;

    public BulletAnimation(Bitmap b, List<Rect> rect){
        mBitmap = b;
        mRect = rect;
    }

    public void draw(Canvas c, Position p){
        tick();
        drawBullet(c,p);
    }

    private void drawBullet(Canvas c, Position p) {
        // TODO: Need to implement more powerful control on index.
        int index = (int) (mTick%mRect.size());
        drawBullet(c,p,mRect.get(index));
    }

    private void drawBullet(Canvas c, Position p, Rect src){
        c.drawBitmap(mBitmap,src, getDestRectF(src,p),null);
    }

    private RectF getDestRectF(Rect src, Position p) {
        return new RectF(
                p.getPositionX()-src.width()*2,
                p.getPositionY()-src.height()*2,
                p.getPositionX()+src.width()*2,
                p.getPositionY()+src.height()*2
                );
    }

    private void tick(){
        mTick++;
    }
}
