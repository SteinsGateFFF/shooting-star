package hacking.to.the.gate.Hittables.bomb;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import hacking.to.the.gate.Collider;
import hacking.to.the.gate.Hittables.Hittable;
import hacking.to.the.gate.Hittables.jet.FriendJet;

/**
 * Created by Ruiqian on 9/20/2015.
 */
public class KamikazeBomb extends Bomb{

    private List<FriendJet> mFriendJets;
    public void onCollision(Hittable h){}
    public Collider getCollider(){
        return null;
    }

    @Override
    public List<? extends Hittable> getHittableChildren() {
        return mFriendJets;
    }

    public KamikazeBomb(float x, float y,Paint p){
        super(x,y,0,p);
        mFriendJets = new ArrayList<>();
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
            mFriendJets.add(new FriendJet.Builder()
                    .selfPosition(xPos,mSelfPos.getPositionY())
                    .radius(20)
                    .build());
            i++;
        }

    }
}
