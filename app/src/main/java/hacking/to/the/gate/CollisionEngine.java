package hacking.to.the.gate;

import java.util.ArrayList;
import java.util.List;

import bomb.Bomb;

import jet.EnemyJet;
import jet.SelfJet;

/**
 * Created by Ruiqian on 9/2/2015.
 */
public class CollisionEngine {

    private List<EnemyJet> mEnemies;
    private SelfJet mPlayer;
    private List<PowerUp> mPowerups;
    private List<Bomb> mBombs;
    private List<Hittable> mNegativeOnes;
    private List<Hittable> mPositiveOnes;


    public CollisionEngine(List<EnemyJet> jets, List<PowerUp> powerups, SelfJet player, List<Bomb> bombs){

        mEnemies = jets;
        mPowerups = powerups;
        mPlayer = player;
        mBombs = bombs;
        mNegativeOnes = new ArrayList<>();
        mPositiveOnes = new ArrayList<>();
    }

    public void setPlayer(SelfJet newPlayer){
        mPlayer = newPlayer;
    }
    private boolean detectCollision(Hittable t1, Hittable t2){

        return detectCollision(t1.getCollider(),t2.getCollider());
    }

    private boolean detectCollision(Collider c1, Collider c2){

        if(c1==null || c2 == null){
            return false;
        }

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

    private void traverse(Hittable root1,Hittable root2){

        traverseHelper(root1,root2);
        if(root2.getHittableChildren() == null)
            return;
        for(Hittable h :root2.getHittableChildren()){
            traverse(root1,h);
        }
    }
    private void traverseHelper(Hittable h1,Hittable h2){

        if(detectCollision(h1,h2)){
            doCollision(h1,h2);
        }
       if(h1.getHittableChildren() == null){
           return;
        }
        for(Hittable h:h1.getHittableChildren()){
            traverseHelper(h,h2);
        }

    }
    public void tick(){
        mNegativeOnes.clear();
        mPositiveOnes.clear();
        mNegativeOnes.addAll(mEnemies);
        mNegativeOnes.addAll(mPowerups);
        mPositiveOnes.addAll(mBombs);
        mPositiveOnes.add(mPlayer);
        if(mNegativeOnes!= null && mPositiveOnes != null){
            for(Hittable h : mNegativeOnes){
                for(Hittable h2 : mPositiveOnes){
                    traverse(h,h2);
                }
            }
        }
    }
}
