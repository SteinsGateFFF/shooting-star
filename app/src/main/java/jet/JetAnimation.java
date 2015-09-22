package jet;

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
 * This is a class for drawing animation.
 *
 * Created by yihuaqi on 2015/9/7.
 */
public class JetAnimation {
    private static Bitmap mSelfJetBitmap;
    private static Bitmap mEnemyJetBitmap;

    public static final int TYPE_SELF_JET = 0;
    public static final int TYPE_ENEMY_JET_0 = 1;

    private static final List<Rect> SELF_JET_MOVE_LEFT_RECTS = new ArrayList<>();
    private static final List<Rect> SELF_JET_MOVE_RIGHT_RECTS = new ArrayList<>();
    private static final List<Rect> SELF_JET_STILL_RECTS = new ArrayList<>();
    private static final int SELF_JET_WIDTH = 32;
    private static final int SELF_JET_HEIGHT = 48;

    private static final List<Rect> ENEMY_JET_0_MOVE_LEFT_RECTS = new ArrayList<>();
    private static final List<Rect> ENEMY_JET_0_MOVE_RIGHT_RECTS = new ArrayList<>();
    private static final List<Rect> ENEMY_JET_0_STILL_RECTS = new ArrayList<>();
    private static final int ENEMY_JET_0_WIDTH = 48;
    private static final int ENEMY_JET_0_HEIGHT = 32;

    /**
     * Must be called before any operation.
     *
     * @param context
     * @throws IOException
     */
    public static void init(Context context) throws IOException {
        for (int i = 0; i < 8; i++) {
            SELF_JET_STILL_RECTS.add(new Rect(i * SELF_JET_WIDTH, 0, (i + 1) * SELF_JET_WIDTH, SELF_JET_HEIGHT));
            SELF_JET_MOVE_LEFT_RECTS.add(new Rect(i * SELF_JET_WIDTH, SELF_JET_HEIGHT, (i + 1) * SELF_JET_WIDTH, SELF_JET_HEIGHT*2));
            SELF_JET_MOVE_RIGHT_RECTS.add(new Rect(i * SELF_JET_WIDTH, SELF_JET_HEIGHT*2, (i + 1) * SELF_JET_WIDTH, SELF_JET_HEIGHT*3));
        }

        for (int i = 0; i < 8; i++) {
            ENEMY_JET_0_STILL_RECTS.add(new Rect((i % 4) * ENEMY_JET_0_WIDTH, 0, (i % 4 + 1) * ENEMY_JET_0_WIDTH, ENEMY_JET_0_HEIGHT));
        }
        for (int i = 0; i < 8; i++) {
            ENEMY_JET_0_MOVE_RIGHT_RECTS.add(new Rect((i % 4) * ENEMY_JET_0_WIDTH, (i / 4 + 1) * ENEMY_JET_0_HEIGHT, (i % 4 + 1) * ENEMY_JET_0_WIDTH, (i / 4 + 2) * ENEMY_JET_0_HEIGHT));
            // TODO: HOLY F**K. We don't have left rects.
            ENEMY_JET_0_MOVE_LEFT_RECTS.add(new Rect((i % 4 + 1) * ENEMY_JET_0_WIDTH, (i / 4 + 1) * ENEMY_JET_0_HEIGHT, (i % 4) * ENEMY_JET_0_WIDTH, (i / 4 + 2) * ENEMY_JET_0_HEIGHT));
        }

        mSelfJetBitmap = getBitmapFromAssets(context, "data/player/pl00/pl00.png");
        mEnemyJetBitmap = getBitmapFromAssets(context, "data/enemy/enemy.png");
    }

    private static Bitmap getBitmapFromAssets(Context context, String filePath) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(filePath);
        return BitmapFactory.decodeStream(is);

    }


    public static JetAnimation getInstance(int type){
        switch (type){
            case TYPE_SELF_JET:
                return new JetAnimation(mSelfJetBitmap, SELF_JET_MOVE_LEFT_RECTS,SELF_JET_STILL_RECTS,SELF_JET_MOVE_RIGHT_RECTS);
            case TYPE_ENEMY_JET_0:
                return new JetAnimation(mEnemyJetBitmap,ENEMY_JET_0_MOVE_LEFT_RECTS,ENEMY_JET_0_STILL_RECTS,ENEMY_JET_0_MOVE_RIGHT_RECTS);
            default: throw new IllegalArgumentException("Invalid Jet Animation Type");
        }
    }

    /**
     * The bitmap that contains the frames of animation.
     */
    private Bitmap mBitmap;
    /**
     * List of rects when jet is moving towards left.
     */
    private List<Rect> mLeft;
    /**
     * List of rects when jet is still in x.
     */
    private List<Rect> mStill;
    /**
     * List of rects when jet is moving towards right.
     */
    private List<Rect> mRight;
    /**
     * Position of the previous movement.
     */
    private Position mPrevPos = null;
    /**
     * Previous Move type.
     */
    private int mPrevMove = -1;

    private long mTick;

    private static final int MOVE_STILL = 0;
    private static final int MOVE_LEFT = 1;
    private static final int MOVE_RIGHT = 2;


    public JetAnimation(Bitmap b, List<Rect> left, List<Rect> still, List<Rect> right){
        mBitmap = b;
        mLeft = left;
        mStill = still;
        mRight = right;
    }

    public void draw(Canvas c, Position p){
        int move = checkMove(p);
        tick(move);
        switch (move){
            case MOVE_STILL:
                drawStill(c,p);
                break;
            case MOVE_LEFT:
                drawLeftMove(c,p);
                break;
            case MOVE_RIGHT:
                drawRightMove(c,p);
                break;
        }
    };

    /**
     * Checks if the current movement would be towards left or right or still.
     * TODO: Might make more sense if we pass the velocity here.
     * @param p
     * @return
     */
    private int checkMove(Position p){
        if(mPrevPos!=null) {
            if ((int) p.getPositionX() > (int) mPrevPos.getPositionX()) {
                return MOVE_RIGHT;
            } else if ((int) p.getPositionX() < (int) mPrevPos.getPositionX()) {
                return MOVE_LEFT;
            } else {
                return MOVE_STILL;
            }
        } else {
            return MOVE_STILL;
        }
    }

    /**
     * Update tick and prevmove.
     * @param move
     */
    private void tick(int move){
        if(mPrevMove == move){
            mTick++;
        } else {
            mTick = 0;
            mPrevMove = move;
        }
    }

    private void drawLeftMove(Canvas c, Position p){
        drawTick(c,p,mLeft);
    };

    private void drawStill(Canvas c, Position p){
        drawTick(c,p,mStill);
    };

    private void drawRightMove(Canvas c, Position p){
        drawTick(c,p,mRight);
    };

    private void drawTick(Canvas c, Position p, List<Rect> rects){
        long index = mTick/20;
        if(index<4){
            drawTick(c,p,rects.get((int) index));
        } else {
            drawTick(c,p,rects.get((int) (index%4+4)));
        }

    }

    private void drawTick(Canvas c, Position p, Rect src){
        c.drawBitmap(mBitmap,src, getDestRectF(src, p),null);
    }

    private RectF getDestRectF(Rect rect, Position p){
        return new RectF(p.getPositionX()-rect.width()*2,p.getPositionY()-rect.height()*2,
                p.getPositionX()+rect.width()*2,p.getPositionY()+rect.height()*2);
    }

}
