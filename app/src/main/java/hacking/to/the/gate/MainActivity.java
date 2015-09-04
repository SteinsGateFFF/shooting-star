package hacking.to.the.gate;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;



public class MainActivity extends Activity {
    GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mGameView= (GameView) findViewById(R.id.gameview);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /**
         * Since SurfaceView cannot gain focus. This is the only way to forward KeyEvent.
         */
        return mGameView.onKeyDown(keyCode,event);
    }

}
