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
import java.util.List;


/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

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
        GameManager.getInstance().setSelfJetDest(event);
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
        GameManager.getInstance().init(getWidth(),getHeight());

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        GameManager.getInstance().tick();
        GameManager.getInstance().draw(canvas);



    }



}
