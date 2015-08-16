package hacking.to.the.gate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.widget.TextView;

/**
 * Created by yihuaqi on 2015/8/15.
 */
public class GameView extends SurfaceView {
    private SurfaceHolder holder;


    private GameLoopThread gameLoopThread;
    private Jet mSelfJet;
    private List<Jet> mEnemyJets;
    private List<Bullet> mSelfBullets = new ArrayList<>();
    private List<Bullet> mEnemyBullets = new ArrayList<>();



    public GameView(Context context) {
        super(context);
        setup();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mSelfJet.setDestination(event.getX(),event.getY(),true);
                break;
            case MotionEvent.ACTION_UP:
                mSelfJet.setDestination(event.getX(),event.getY(),false);
                break;
        }


        return true;
    }



    private void setup(){
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();



            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while(retry){
                    try{
                        gameLoopThread.join();
                        retry = false;
                    }catch(InterruptedException e){

                    }
                }

            }
        });

        gameLoopThread = new GameLoopThread(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();

    }

    private void init(){
        Paint p = new Paint();
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p.setColor(Color.WHITE);
        Log.d(TAG,"new Self Jet");
        mSelfJet = new Jet(getWidth()/2,getHeight()-50,50,p,0,0);
        Log.d(TAG,"SelfJet: "+mSelfJet.getJetX()+" "+mSelfJet.getJetY());
        mEnemyJets = new ArrayList<>();
        for(int i =0; i<5;i++){
            //mEnemyJets.add(new Jet((i+1)*getWidth()/6,0, 50, p2,0, 1));

        }
    }

    private final String TAG = "GameView";
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        if(!mSelfJet.isDead()) {
            mSelfJet.checkCollision(mEnemyBullets);
            mSelfJet.onDraw(canvas);

            mSelfBullets.add(new Bullet(mSelfJet.getJetX(), mSelfJet.getJetY(), 10, mSelfJet.getPaint(), 0, -20));
        }

        Log.d(TAG,mSelfJet.getJetX()+" "+mSelfJet.getJetY());
        for(Jet jet:mEnemyJets){
            if(!jet.isDead()) {
                jet.checkCollision(mSelfBullets);
                jet.onDraw(canvas);
                mEnemyBullets.add(new Bullet(jet.getJetX(), jet.getJetY(), 10, jet.getPaint(), 0, 20));
            }
        }
        for(Bullet b: mEnemyBullets){
            b.onDraw(canvas);
        }
        for(Bullet b: mSelfBullets){
            b.onDraw(canvas);
        }


    }



}
