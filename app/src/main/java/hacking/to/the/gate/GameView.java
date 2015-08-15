package hacking.to.the.gate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yihuaqi on 2015/8/15.
 */
public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private Paint mypaint;
    private GameLoopThread gameLoopThread;
    private int bulletY = 0;
    private int bulletX = 0;


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
        mypaint = new Paint();
        mypaint.setColor(Color.WHITE);
        gameLoopThread = new GameLoopThread(this);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(100,100,50,mypaint);
        canvas.drawCircle(bulletX,bulletY,10,mypaint);
        bulletX++;
        bulletY++;
    }
}
