package bomb;

import android.graphics.Color;
import android.graphics.Paint;
import hacking.to.the.gate.GameManager;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class BombFactory {
    public final static int ATOMIC_BOMB = 0;
    public final static int LIGHTENING_BOMB = 1;
    public final static int KAMIKAZE_BOMB = 2;
    public final static int AT_FIELD_BOMB = 3;
    public Bomb getBomb(int bombType, float x, float y){

        Paint p = new Paint();
        switch (bombType){
            case ATOMIC_BOMB:

                p.setColor(Color.YELLOW);
                return new AtomicBomb(x,y,x/2f,p);
            case LIGHTENING_BOMB:
                p.setColor(Color.BLUE);
                return new LighteningBomb(x,y,30,
                        GameManager.getInstance().getScreenRect().height(),
                        p);
            case KAMIKAZE_BOMB:
                p.setColor(Color.GRAY);
                return new KamikazeBomb(x,y,p);
            case AT_FIELD_BOMB:
                p.setColor(Color.CYAN);
                p.setStyle(Paint.Style.STROKE);
                return new ATFieldBomb(x,y,100,p);

        }
        return null;
    }
}
