package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import java.util.Random;
import org.apache.http.impl.conn.tsccm.PoolEntryRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/17.
 */
public class GameManager {
    private static GameManager instance;
    private GameManager(){

    };
    public static GameManager getInstance(){
        if(instance==null){
            instance = new GameManager();
        }
        return instance;
    }


    private Jet mSelfJet;
    private List<Jet> mEnemyJets;
    private List<PowerUp> mPowerUps;
    private float mScreenWidth;
    private float mScreenHeight;
    private Rect mScreenRect;

    public void init(float screenWidht, float screenHeight){
        mScreenWidth = screenWidht;
        mScreenHeight = screenHeight;
        mScreenRect = new Rect(0,0,(int)mScreenWidth,(int)mScreenHeight);
        Paint p = new Paint();
        Paint p2 = new Paint();

        p2.setColor(Color.RED);
        p.setColor(Color.WHITE);

        mFPSPaint = new Paint();
        mFPSPaint.setColor(Color.WHITE);

        mSelfJet = new Jet(mScreenWidth/2,mScreenHeight-50,50,p);

        mEnemyJets = new LinkedList<>();

        for(int i =0; i<5;i++){
            mEnemyJets.add(new Jet((i+1)*mScreenWidth/6,0, 50, p2));

        }
        mPowerUps = new LinkedList<>();
    }
    public void setSelfJetDest(MotionEvent event){
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
    }

    public void tick(){

        if(!mSelfJet.isDead()) {
            mSelfJet.tick(null);
        }


        for(Jet jet:mEnemyJets){
            if(!jet.isDead()) {

                mSelfJet.checkCollision(jet.getBullets());
                if(jet.checkCollision(mSelfJet.getBullets()))
                {
                    Paint powerUpPaint = new Paint();
                    powerUpPaint.setColor(Color.GREEN);
                    Position pos = new Position(jet.getSelfPosition().getPositionX()+10,jet.getSelfPosition().getPositionY()+10);
                    PowerUp powerUp = new PowerUp(true,pos,0,0, powerUpPaint,5);
                    mPowerUps.add(powerUp);
                }
                Bullet b = new Bullet(jet.getSelfPosition(), 10, jet.getPaint(), 0, 20);
                b.setDestination(mSelfJet.getSelfPosition(),true);
                jet.tick(b);
            }
        }

        for(PowerUp p:mPowerUps){
            if(p.isVisible()){
                for(Bullet b:mSelfJet.getBullets()) {
                    if(p.checkHitBox(b)){
                        p.destroy();
                        mSelfJet.setHealth(mSelfJet.getHealth()+10);
                    }
                }
                p.tick();
            }
        }

        if(mSelfJet.getHealth()<30 && mPowerUps.size()<4){
            Paint powerUpPaint = new Paint();
            powerUpPaint.setColor(Color.GREEN);
            Random rand = new Random();
            int value = rand.nextInt(50)+1;
            Position pos = new Position(20*value,10*value);
            PowerUp powerUp = new PowerUp(false,pos,10,20, powerUpPaint,5);
            mPowerUps.add(powerUp);
        }

        recycle();
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.BLACK);
        if(!mSelfJet.isDead()) {
            mSelfJet.draw(canvas);
        }

        for(Jet jet:mEnemyJets){
            if(!jet.isDead()) {
                jet.draw(canvas);
            }
        }
        for(PowerUp p:mPowerUps){
            if(p.isVisible()){
                p.draw(canvas);
            }
        }
    }

    public Rect getScreenRect(){
        return mScreenRect;

    }

    public void recycle(){
        if(mSelfJet.shouldRecycle()){

        } else {
            mSelfJet.recycle();
        }
        Log.d("Jet","EnemyJets: "+mEnemyJets.size());
        for(Iterator<Jet> it = mEnemyJets.iterator(); it.hasNext();){
            Jet jet = it.next();
            if(jet.shouldRecycle()){
                Log.d("Jet","EnemyJet should recycle.");
                it.remove();
            } else {
                jet.recycle();
            }
        }

        for(Iterator<PowerUp> i = mPowerUps.iterator();i.hasNext();){
            PowerUp p = i.next();
            if(!p.isVisible()){
                i.remove();
            }
        }
    }

    private long lastTimestamp = 0;
    private Paint mFPSPaint;
    public void measureFrameRate(Canvas c) {
        if(lastTimestamp != 0) {
            double frameRate = 1000/(System.currentTimeMillis() - lastTimestamp);
            c.drawText("FPS: "+frameRate,10,10,mFPSPaint);
        }
        lastTimestamp = System.currentTimeMillis();
    }
}
