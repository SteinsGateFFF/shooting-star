package bomb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;
import java.util.List;
import jet.FriendJet;
import jet.JetAnimation;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class KamikazeBomb extends Bomb{

    private List<FriendJet> mFriendJets;
    public KamikazeBomb(float x, float y,Paint p){
        super(x,y,0,p);
        mFriendJets = new LinkedList<>();
        generateFriendJets();

    }

    public List<FriendJet> getFriendJets(){
        return mFriendJets;
    }
    @Override
    public void tick(){
        if(mFriendJets != null){
            for(FriendJet friendJet : mFriendJets){
                friendJet.tick();
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        if(mFriendJets != null){
            for(FriendJet friendJet : mFriendJets){
                friendJet.draw(canvas);
            }
        }
    }

    private void generateFriendJets(){
        int i = 1;
        while(i < 10){
            int xPos = i*60;
            mFriendJets.add(new FriendJet.Builder(xPos,mSelfPos.getPositionY(),20,
                   JetAnimation.TYPE_SELF_JET).build());
            i++;
        }

    }
}
