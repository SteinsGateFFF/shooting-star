package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihuaqi on 2015/8/17.
 */
public class GameManager {
    private static GameManager instance;
    private GameManager(){};
    public static GameManager getInstance(){
        if(instance==null){
            instance = new GameManager();
        }
        return instance;
    }


    private Jet mSelfJet;
    private List<Jet> mEnemyJets;
    private float mScreenWidth;
    private float mScreenHeight;

    public void init(float screenWidht, float screenHeight){
        mScreenWidth = screenWidht;
        mScreenHeight = screenHeight;
        Paint p = new Paint();
        Paint p2 = new Paint();
        p2.setColor(Color.RED);
        p.setColor(Color.WHITE);

        mSelfJet = new Jet(mScreenWidth/2,mScreenHeight-50,50,p);

        mEnemyJets = new ArrayList<>();
        for(int i =0; i<5;i++){
            mEnemyJets.add(new Jet((i+1)*mScreenWidth/6,0, 50, p2));

        }
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
                jet.checkCollision(mSelfJet.getBullets());
                Bullet b = new Bullet(jet.getSelfPosition(), 10, jet.getPaint(), 0, 20);
                b.setDestination(mSelfJet.getSelfPosition(),true);
                jet.tick(b);
            }
        }
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
    }
}
