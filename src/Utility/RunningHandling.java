package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class RunningHandling extends Script {

    static Map<String, Double> weights;
    public static int msPerSquare = 300;


    private static double playerWeight(){
        return Arrays.stream(Inventory.getItems())
                .map(Item::getName)
                .mapToDouble(RunningHandling::calculateWeight)
                .sum();
    }

    private static double calculateWeight(String item){
        return weights.getOrDefault(item, 0.0);
    }

    public static double depletionRatePerSquare(){
        double a = Math.min(playerWeight(), 64);
        return ((a / 100)+ 0.64) / 2;
    }

    public static int nSquaresWeCanRunTo(){
        return (int)(Movement.getRunEnergy() / depletionRatePerSquare());
    }

    public static boolean energyEnoughForPosition(Position positionOfLoot){
        return Players.getLocal().getPosition().distance(positionOfLoot) <= nSquaresWeCanRunTo();
    }

    public static boolean haveTimeForLoot(long nextLootSpawn, Position positionOfLoot){
        int nSquaresBackAndForth = (int)(nextLootSpawn / msPerSquare) / 2;
        return Players.getLocal().getPosition().distance(positionOfLoot) <= nSquaresBackAndForth;
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
