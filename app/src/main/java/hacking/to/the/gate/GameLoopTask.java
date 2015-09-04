package hacking.to.the.gate;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Objects;

/**
 * Thread for rendering the canvas and doing game logic.
 *
 * TODO: Should sperate the rendering from game logic.
 * Created by Jelly and Huaqi on 2015/8/15.
 */
public class GameLoopTask extends AsyncTask<Void,Void,Void>{
    private final String TAG = "GameLoopThread";
    private GameView view;
    private boolean mIsPaused = true;
    private Object mLock = new Object();
    public GameLoopTask(GameView view){
        this.view = view;
    }

    public void toggle(){
        Log.d(TAG,"Toggled!");
        if(mIsPaused){
            resume();
        } else {
            pause();
        }
    }

    public void resume(){
        if(mIsPaused) {
            synchronized (mLock) {
                mIsPaused = false;
                mLock.notify();
            }
        }

    }

    public void pause(){
        if(!mIsPaused) {
            mIsPaused = true;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(true) {

            if(mIsPaused){
                synchronized (mLock) {
                    try {
                        mLock.wait();
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Interrupted!");
                    }
                }
            }

            Canvas c = null;
            try{
                c = view.getHolder().lockCanvas();
                if(c==null) continue;
                synchronized (view.getHolder()){
                    GameManager.getInstance().tick();
                    GameManager.getInstance().draw(c);
                    GameManager.getInstance().measureFrameRate(c);
                }
            }finally {
                if(c!= null){
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}

