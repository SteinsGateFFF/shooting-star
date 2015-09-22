package hacking.to.the.gate;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

import bomb.ATFieldBomb;
import bomb.AtomicBomb;
import bomb.Bomb;
import bomb.KamikazeBomb;
import bomb.LighteningBomb;
import jet.EnemyJet;
import jet.FriendJet;
import jet.SelfJet;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public class CollisionEngine {

    private List<EnemyJet> mEnemies;
    private SelfJet mPlayer;
    private List<PowerUp> mPowerups;
    private List<Bomb> mBombs;


    public CollisionEngine(List<EnemyJet> jets, List<PowerUp> powerups, SelfJet player, List<Bomb> bombs){
        mEnemies = jets;
        mPowerups = powerups;
        mPlayer = player;
        mBombs = bombs;
    }

    public void setPlayer(SelfJet newPlayer){
        mPlayer = newPlayer;
    }
    private boolean detectCollision(Hittable t1, Hittable t2){

        return detectCollision(t1.getCollider(),t2.getCollider());
    }

    private boolean detectCollision(Collider c1, Collider c2){

        if(c1 instanceof CircleCollider && c2 instanceof CircleCollider){
            CircleCollider circle = (CircleCollider) c1;
            CircleCollider circle2 = (CircleCollider) c2;
            return Math.pow(
                    circle.getPosition().getPositionX()-circle2.getPosition().getPositionX(),
                    2)
                    +Math.pow(
                    circle.getPosition().getPositionY()-circle2.getPosition().getPositionY(),
                    2)
                    < Math.pow(circle.getRadius()+circle2.getRadius(),2);

        }
        else if(c1 instanceof  BoxCollider && c2 instanceof  BoxCollider){
            BoxCollider box = (BoxCollider) c1;
            BoxCollider box2 = (BoxCollider) c2;
            return (Math.abs(box.getPosition().getPositionX() - box2.getPosition().getPositionX()) * 2
                    < (box.getWidth() + box2.getWidth()))
                    &&
                    (Math.abs(box.getPosition().getPositionY() - box2.getPosition().getPositionY()) * 2
                            < (box.getHeight() + box2.getHeight()));
        }
        else{
            BoxCollider box;
            CircleCollider circle;
            if(c1 instanceof BoxCollider){
                box = (BoxCollider) c1;
                circle = (CircleCollider) c2;
            }
            else{
                box = (BoxCollider) c2;
                circle = (CircleCollider) c1;
            }

            float distanceX = Math.abs(circle.getPosition().getPositionX() - box.getPosition().getPositionX());
            float distanceY = Math.abs(circle.getPosition().getPositionY() - box.getPosition().getPositionY());

            if (distanceX > (box.getWidth()/2 + circle.getRadius())) { return false; }
            if (distanceY > (box.getHeight()/2 + circle.getRadius())) { return false; }

            if (distanceX <= (box.getWidth()/2)) { return true; }
            if (distanceY <= (box.getHeight()/2)) { return true; }

            double distance = Math.pow((distanceX - box.getWidth()/2),2) +
                    Math.pow((distanceY - box.getHeight()/2),2);

            return (distance <= (Math.pow(circle.getRadius(),2)));

        }


    }

    private void doCollision(Hittable h1, Hittable h2){
        h1.onCollision(h2);
        h2.onCollision(h1);
    }

    public void tick(){
        for(Iterator<PowerUp> i = mPowerups.iterator();i.hasNext();) {
            PowerUp p = i.next();
            if(!mPlayer.isDead()&&p.isVisible()&& detectCollision(mPlayer,p)){
                doCollision(mPlayer,p);
            }
        }

        for(EnemyJet jet:mEnemies){
            for (Iterator<Bullet> it = jet.getBullets().iterator(); it.hasNext(); ) {
                Bullet b = it.next();
                for(Bomb bomb:mBombs){
                    if(!bomb.shouldRecycle()){
                        if(bomb instanceof AtomicBomb){
                            AtomicBomb atomicBomb = (AtomicBomb) bomb;
                            if(detectCollision(atomicBomb,b)){
                                doCollision(atomicBomb,b);
                            }
                        }
                        if(bomb instanceof ATFieldBomb){
                            ATFieldBomb atFieldBomb = (ATFieldBomb) bomb;
                            if(detectCollision(atFieldBomb,b)){
                                doCollision(atFieldBomb,b);
                            }
                        }
                        if(bomb instanceof KamikazeBomb){
                            KamikazeBomb kamikazeBomb = (KamikazeBomb) bomb;
                            for(Iterator<FriendJet> i = kamikazeBomb.getFriendJets().iterator();i.hasNext();){
                                FriendJet friendJet = i.next();
                                if(!friendJet.isDead()  && detectCollision(friendJet,b)){
                                    doCollision(friendJet,b);
                                }
                            }
                        }

                    }

                }

                if (!mPlayer.isDead() && detectCollision(mPlayer, b)) {
                    Log.d("collision", "hit by enemy's bullet");
                    doCollision(mPlayer,b);
                }
            }
            for(Bomb bomb:mBombs){
                if(!bomb.shouldRecycle()){
                    if(bomb instanceof AtomicBomb){
                        AtomicBomb atomicBomb = (AtomicBomb) bomb;
                        if(detectCollision(atomicBomb,jet)){
                            doCollision(atomicBomb,jet);
                        }
                    }
                    if(bomb instanceof LighteningBomb){
                        LighteningBomb lighteningBomb = (LighteningBomb) bomb;
                        if(!jet.isDead()&&detectCollision(lighteningBomb,jet)){
                            doCollision(lighteningBomb,jet);
                        }
                    }
                    if(bomb instanceof KamikazeBomb){
                        KamikazeBomb kamikazeBomb = (KamikazeBomb) bomb;
                        for(Iterator<FriendJet> i = kamikazeBomb.getFriendJets().iterator();i.hasNext();){
                            FriendJet friendJet = i.next();
                            if(!friendJet.isDead() && !jet.isDead() && detectCollision(friendJet,jet)){
                                doCollision(friendJet,jet);
                            }
                        }
                    }
                }
            }
            for(Iterator<Bullet> it = mPlayer.getBullets().iterator(); it.hasNext();){
                Bullet b = it.next();
                if(!jet.isDead()&& detectCollision(jet,b)) {
                    Log.d("collision","hit by player's bullet");
                    doCollision(jet,b);

                }
            }
        }
    }

}
