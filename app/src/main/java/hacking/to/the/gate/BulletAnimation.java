package hacking.to.the.gate;

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

/**
 * Created by yihuaqi on 2015/9/10.
 */
public class BulletAnimation {
    private static Bitmap mBulletBitmap1;

    public static final int TYPE_SELF_BULLET = 0;

    private static final List<List<Rect>> SELF_BULLET_RECTS = new ArrayList<>();
    private static final int SELF_BULLET_WIDTH = 16;
    private static final int SELF_BULLET_HEIGHT = 16;

    public static void init(Context context) throws IOException {
        for(int i = 0; i < 16; i++) {
            // For different colors.
            // TODO: How can this become consistent with JetAnimation.
            List<Rect> rects = new ArrayList<>();
            rects.add(new Rect(i*SELF_BULLET_WIDTH,16, (i+1)*SELF_BULLET_WIDTH,16+SELF_BULLET_HEIGHT));
            SELF_BULLET_RECTS.add(rects);
        }

        mBulletBitmap1 = getBitmapFromAssets(context, "data/bullet/bullet1.png");
    }


    private static Bitmap getBitmapFromAssets(Context context, String filePath) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(filePath);
        return BitmapFactory.decodeStream(is);
    }

    public static BulletAnimation getInstance(int type, int color){
        switch (type){
            case TYPE_SELF_BULLET:
                return new BulletAnimation(mBulletBitmap1, SELF_BULLET_RECTS.get(color));
            //TODO: How to deal with color out of boundary?
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
        int index = (int) Math.min(mRect.size()-1,mTick);
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
