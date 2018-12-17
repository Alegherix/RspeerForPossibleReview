package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RunningHandling extends Script {


    private static double targetDistance;
    private static double currentDistance;
    private static boolean nextClickSet;
    private static int nextClick;
    private static boolean initiated;
    private static Position targetPosition;

    private static void initiateVariables(int x, int y){
        targetDistance = 0.0;
        currentDistance = targetDistance;
        nextClickSet = false;
        initiated = true;
        targetPosition = new Position(x,y);

    }

    private static final HashMap<String, Double> weights = WeightHandling.initiateMap(new HashMap<>());
    public static final int msPerSquareRunning = 400;
    public static final int msPerSquareWalking = 805;


    public static double depletionRatePerSquare() {
        double a = Math.min(Game.getClient().getWeight(), 64);
        return ((a / 100) + 0.64) / 2;
    }

    public static int nSquaresWeCanRunTo(){
        return (int)(Movement.getRunEnergy() / depletionRatePerSquare());
    }

    public static boolean haveTimeToWalk(long nextLootSpawn, Position myPosition, Position positionOfLoot){
        double nSqBackAndForth = myPosition.distance(positionOfLoot)*2;
        long msTaken =  msPerSquareWalking *(long)nSqBackAndForth;
        return nextLootSpawn > (msTaken+1600);
    }


    public static boolean haveTimeToRun(long nextLootSpawn, Position myPos, Position positionOfLoot){
        double nSqBackAndForth = myPos.distance(positionOfLoot)*2;
        long timeTakenBackAndForthRunning = msPerSquareRunning * (long)nSqBackAndForth;

        return nextLootSpawn > timeTakenBackAndForthRunning && myPos.distance(positionOfLoot) <= nSquaresWeCanRunTo();
    }


    public static void smartWalking(Position position){
        smartWalking(position.getX(), position.getY());
    }

    public static void smartWalking(int x, int y ){

        if(!initiated){
            initiateVariables(x,y);
        }
        else{
            currentDistance = targetPosition.distance(Players.getLocal().getPosition());

            if(targetDistance == 0.0){
                targetDistance = targetPosition.distance(Players.getLocal().getPosition());
            }
            else if(!nextClickSet){
                nextClick = Random.mid(12,15);
                nextClickSet = true;
            }
            else if(!Players.getLocal().isMoving() || currentDistance + (double) nextClick <= targetDistance){
                if(shouldRun()){
                    enableRun();
                }
                else if(targetDistance >2 && currentDistance>2){
                    Movement.walkTo(targetPosition);
                    targetDistance = targetPosition.distance(Players.getLocal().getPosition());
                }

                nextClickSet = false;
            }
        }
    }


    public void walkRandomly(){
        int diffX = ThreadLocalRandom.current().nextInt(-25,25);
        int diffY = ThreadLocalRandom.current().nextInt(-25,25);
        Movement.walkTo(new Position(Players.getLocal().getX()+diffX, Players.getLocal().getY()+diffY));
    }

    public static void enableRun(){
        Movement.toggleRun(true);
    }

    public static boolean shouldRun(){
        return Movement.getRunEnergy()>=30 && !Movement.isRunEnabled();
    }
}
