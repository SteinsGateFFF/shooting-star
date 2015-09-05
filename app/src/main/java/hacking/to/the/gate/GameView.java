package hacking.to.the.gate;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class GameView extends SurfaceView {
    private SurfaceHolder holder;
    private GameLoopTask mGameLoopTask;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN || keyCode==KeyEvent.KEYCODE_VOLUME_UP){
//            mGameLoopTask.toggle();
            GameManager.getInstance().toggleState();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Bind the {@link GameLoopTask} with {@link android.view.SurfaceHolder.Callback}.
     * So that when the surface is created, the {@link GameLoopTask} is started,
     * and when the surface is destroyed, the {@link GameLoopTask} is stopped.
     *
     *TODO: Should change the control flow to support game pause. Also the join() method will cause unexpected crash.
     */
    private void setup(){
        holder = getHolder();
        mGameLoopTask = new GameLoopTask(this);
        mGameLoopTask.execute();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas();
                if(c==null) return;
                GameManager.getInstance().draw(c);
                holder.unlockCanvasAndPost(c);
                mGameLoopTask.resume();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                GameManager.getInstance().pause();
                mGameLoopTask.pause();

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Here is when the width and height of the view are determined.
        GameManager.getInstance().init(getWidth(),getHeight());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
