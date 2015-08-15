package hacking.to.the.gate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/15.
 */
public class GameView extends SurfaceView {
    private SurfaceHolder holder;

    private GameLoopThread gameLoopThread;
    private Jet mSelfJet;
    private List<Jet> mEnemyJets;



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
        mSelfJet.setJetX(event.getX());
        mSelfJet.setJetY(event.getY());

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Paint p = new Paint();
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p.setColor(Color.WHITE);
        mSelfJet = new Jet(getWidth()/2,getHeight()-50,50,p,0,0);
        mEnemyJets = new ArrayList<>();
        for(int i =0; i<5;i++){
            mEnemyJets.add(new Jet((i+1)*getWidth()/6,0, 50, p2,0, 1));

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        mSelfJet.onDraw(canvas);
        for(Jet jet:mEnemyJets){
            jet.onDraw(canvas);
        }

    }
}
