package hacking.to.the.gate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Manager class that manages the game logic, holds all underlying data objects, and renders the canvas.
 *
 * TODO: Should probably seperate the call {@link #tick()} and {@link #draw(android.graphics.Canvas)} into different threads.
 * TODO: Rendering should be using a defensive copy of the underlying data objects to avoid synchronization and performance problem.
 *
 * Created by yihuaqi on 2015/8/17.
 */
public class GameManager {
    private static GameManager instance;


    /**
     * Initial State.
     */
    public static final int STATE_NOT_INIT = -1;

    /**
     * State that the screen dimensions have been initialized.
     */
    public static final int STATE_INIT = 0;
    /**
     * State that all the game resources have been loaded.
     */
    public static final int STATE_CREATED = 1;
    /**
     * State that time flows.
     */
    public static final int STATE_STARTED = 2;
    /**
     * State that time stops.
     */
    public static final int STATE_PAUSED = 3;
    /**
     * State when all lives have been used up.
     */
    public static final int STATE_GAME_OVER = 4;
    /**
     * State when player wins the game.
     * TODO: Not implemented yet.
     */
    public static final int STATE_GAME_WIN = 5;
    /**
     * Describe the current state of the game.
     */
    private int mGameState = STATE_NOT_INIT;

    private String mGameStateText;
    private String mGameHintText;

    private long lastTimestamp = 0;
    private Paint mFPSPaint;
    private Paint mStatePaint;
    private Paint mHintPaint;
    private Paint mEnemyJetPaint;
    private Paint mSelfJetPaint;

    private int mRemainingLife;
    private static final int DEFAULT_REMAINING_LIVES = 3;

    private CollisionEngine mCollisionEngine;

    private GameManager(){
    };
    public static GameManager getInstance(){
        if(instance==null){
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Jet that controled by player.
     */
    private Jet mSelfJet;
    /**
     * List of enemy Jets.
     */
    private List<Jet> mEnemyJets;

    /*
        list of power ups
     */
    private List<PowerUp> mPowerUps;
    private float mScreenWidth;
    private float mScreenHeight;
    private Rect mScreenRect;

    /**
     * Init the {@link hacking.to.the.gate.GameManager} with the dimension of the {@link hacking.to.the.gate.GameView}.
     * Create SelfJet and EnemyJets.
     *
     * TODO: Creating SelfJet and EnemyJets should be in seperate methods.
     *
     * @param screenWidht
     * @param screenHeight
     */
    public void init(float screenWidht, float screenHeight){
        mScreenWidth = screenWidht;
        mScreenHeight = screenHeight;
        mScreenRect = new Rect(0,0,(int)mScreenWidth,(int)mScreenHeight);
        mEnemyJetPaint = new Paint();
        mSelfJetPaint = new Paint();
        mEnemyJetPaint.setColor(Color.RED);
        mSelfJetPaint.setColor(Color.WHITE);
        mFPSPaint = new Paint();
        mFPSPaint.setColor(Color.WHITE);

        mStatePaint = new Paint();
        mStatePaint.setColor(Color.WHITE);
        mStatePaint.setTextSize(80);
        mStatePaint.setTextAlign(Paint.Align.CENTER);

        mHintPaint = new Paint(mStatePaint);
        mHintPaint.setTextSize(50);

        onStateChange(STATE_INIT);
    }

    /**
     * Create self jet and enemy jets.
     * TODO: Should be able to load from a predefined game file.
     */
    public void createGame(){

        mEnemyJets = new LinkedList<>();
        // To avoid crash when start a new game.
        synchronized (mEnemyJets) {
            for (int i = 0; i < 5; i++) {
                Jet enemyJet = new Jet((i + 1) * mScreenWidth / 6, 100, 50, mEnemyJetPaint, false);
                enemyJet.setGunType(Gun.GUN_TYPE_SELF_TARGETING_EVEN, Bullet.BULLET_STYLE_WORM);
                mEnemyJets.add(enemyJet);
            }
        }
        mRemainingLife = DEFAULT_REMAINING_LIVES;
        mPowerUps = new LinkedList<>();
        mCollisionEngine = new CollisionEngine(mEnemyJets,mPowerUps,mSelfJet);
    }

    public void createSelfJet(float x, float y){
        mSelfJet = new Jet(x,y,50,mSelfJetPaint,true);
        mSelfJet.setGunType(Gun.GUN_TYPE_DEFAULT, Bullet.BULLET_STYLE_DEFAULT);
        mRemainingLife--;
        mCollisionEngine.setPlayer(mSelfJet);
        Log.d("Selfjet","New self jet is created");

    }

    /**
     * Set the destination of the SelfJet.
     * @param event MotionEvent of user touch.
     */
    private void setSelfJetDest(MotionEvent event){
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(mSelfJet!=null) {
                    mSelfJet.setDestination(event.getX(), event.getY(), true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mSelfJet!=null) {
                    mSelfJet.setDestination(event.getX(), event.getY(), false);
                }
                break;
        }
    }

    /**
    * Tick to the next move.
     */
    public void tick(){
        if(!shouldTick()) {
            return;
        }
        if(mSelfJet!=null) {
            mSelfJet.tick();
            for(Jet jet:mEnemyJets){
                jet.tick();
            }
            for(PowerUp powerup :mPowerUps){
                powerup.tick();
            }
            mCollisionEngine.tick();
        }


        recycle();
    }

    /**
     * generate a powerup
     * @param isStatic identify if the powerup is static
     * @param posX x-position of the center of the powerup
     * @param posY y-position of the center of the powerup
     */
    public void generatePowerups(boolean isStatic,int posX,int posY){
        PowerUp powerUp;
        Paint powerUpPaint = new Paint();
        powerUpPaint.setColor(Color.GREEN);
        Position pos = new Position(posX,posY);
        if(isStatic){
            powerUp = new PowerUp(isStatic,pos,0,0, powerUpPaint,23);

        }
        else {
            powerUp = new PowerUp(isStatic, pos, 1, 4, powerUpPaint, 23);
        }
        mPowerUps.add(powerUp);

    }

    /**
     *
     * @return true if it's time to generate powerups
     */
    public boolean shouldGeneratePowerUps(){
        return mPowerUps.size()< 4 && mSelfJet!=null && mSelfJet.getHealth()<80;
    }
    /**
     * Render to canvas
     * @param canvas
     */
    public void draw(Canvas canvas){
        canvas.drawColor(Color.BLACK);

        drawStates(canvas);

        if(!shouldDraw()) {
            return;
        }


        if(mSelfJet!=null) {
            mSelfJet.draw(canvas);
        }

        if(mEnemyJets!=null) {
            // Otherwise might cause crash to call createGame()
            synchronized (mEnemyJets) {
                for (Jet jet : mEnemyJets) {
                    jet.draw(canvas);
                }
            }
        }

        if(mPowerUps!=null) {
            // Otherwise might cause crash to call createGame()
            synchronized (mPowerUps) {
                for (PowerUp p : mPowerUps) {
                    if (p.isVisible()) {
                        p.draw(canvas);
                    }
                }
            }
        }




    }

    /**
     * Indicate whether the game should draw.
     * @return
     */
    private boolean shouldDraw(){
        return !(mGameState==STATE_INIT || mGameState==STATE_NOT_INIT);
    }

    /**
     * Indicate whether the game should tick.
     * @return
     */
    private boolean shouldTick(){
        return mGameState == STATE_STARTED || mGameState == STATE_GAME_OVER || mGameState == STATE_GAME_WIN;
    }

    private void drawStates(Canvas canvas) {
        if(mGameState==STATE_STARTED && mSelfJet!=null) {
            // Don't draw text when player is playing.
            return;
        }
        canvas.drawText(mGameStateText,mScreenWidth/2,mScreenHeight/2, mStatePaint);
        canvas.drawText(mGameHintText,mScreenWidth/2,80+mScreenHeight/2, mHintPaint);
    }



    public Rect getScreenRect(){
        return mScreenRect;

    }

    private void recycle(){
        if(mSelfJet!=null) {
            if (mSelfJet.shouldRecycle()) {
                mSelfJet = null;
                if(mRemainingLife==0){
                    onStateChange(STATE_GAME_OVER);
                }
            } else {
                mSelfJet.recycle();
            }
        }

        boolean win = true;
        for(Iterator<Jet> it = mEnemyJets.iterator(); it.hasNext();){
            Jet jet = it.next();

            if(jet.shouldRecycle()){
                it.remove();

            } else {
                jet.recycle();
            }
        }
        //TODO: Not a good indicator for winning.


        if(mEnemyJets.size()==0) {
            onStateChange(STATE_GAME_WIN);
        }

        for(Iterator<PowerUp> i = mPowerUps.iterator();i.hasNext();){
            PowerUp p = i.next();
            if(!p.isVisible()){
                i.remove();
                Log.d("PowerUp","removed");
            }
        }
    }



    /**
     * This will measure and display the FPS on the left top corner.
     * @param c
     */
    public void measureFrameRate(Canvas c) {
        if(lastTimestamp != 0) {
            double frameRate = 1000/(System.currentTimeMillis() - lastTimestamp);
            c.drawText("FPS: "+frameRate,10,10,mFPSPaint);
        }
        lastTimestamp = System.currentTimeMillis();
    }

    public Position getSelfJetPosition() throws Exception {
        if(mSelfJet==null) throw new Exception("No Self Jet in Game");
        return mSelfJet.getSelfPosition();
    }

    public void toggleState() {
        switch (mGameState){
            case STATE_INIT:
                onStateChange(STATE_CREATED);
                break;
            case STATE_CREATED:
                onStateChange(STATE_STARTED);
                break;
            case STATE_PAUSED:
                onStateChange(STATE_STARTED);
                break;
            case STATE_GAME_OVER:
                onStateChange(STATE_CREATED);
                break;
            case STATE_GAME_WIN:
                onStateChange(STATE_CREATED);
                break;
            case STATE_STARTED:
                onStateChange(STATE_PAUSED);
                break;
            default:
                throw new IllegalStateException("Undefined Current Game State: "+mGameState);
        }
    }

    public void pause(){
        if(mGameState == STATE_STARTED){
            toggleState();
        }
    }

    public void resume(){
        if(mGameState == STATE_PAUSED){
            toggleState();
        }
    }

    private void onStateChange(int nextState){
        mGameState = nextState;
        switch (mGameState){
            case STATE_GAME_WIN:
                mGameStateText = "You Win!!";
                mGameHintText = "How come...";
                break;
            case STATE_INIT:
                mGameStateText = "Init";
                mGameHintText = "Press Volumn Button to Create Game.";
                break;
            case STATE_STARTED:
                mGameStateText = "";
                mGameHintText = "Click on Screen to Revive";
                break;
            case STATE_CREATED:
                createGame();
                mGameStateText = "Created";
                mGameHintText = "Press Volumn Button to Start Game.";
                break;
            case STATE_GAME_OVER:
                mGameStateText = "Game Over";
                mGameHintText = "This game sucks...";
                break;
            case STATE_PAUSED:
                mGameStateText = "Paused";
                mGameHintText = "Press Volumn Button to Resume Game.";
                break;
            default:
                throw new IllegalStateException("Undefined Next Game State: "+nextState);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        if(mGameState==STATE_STARTED) {
            if (mSelfJet == null) {
                createSelfJet(event.getX(), event.getY());
            } else {
               // Log.d("Selfjet","selfjet is not null");
                setSelfJetDest(event);
            }
        }
    }

}
