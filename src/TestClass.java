import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.pathfinding.region.util.Reachable;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    long startTime;
    boolean playerHasDied = false;

    @Override
    public int loop() {
        if(DeathSpot.getDyingPlayer() !=null && !playerHasDied){
            Log.info("Player has died");
            startTime = System.currentTimeMillis();
            Movement.walkTo(DeathSpot.getDyingPlayer().getPosition());
            playerHasDied = true;
        }

        else if(playerHasDied){
            startTimer();
        }

        return 300;
    }

    public void startTimer(){
        Predicate<Pickable> pickablePredicate = itemToLoot -> Pickables.getNearest(pickable -> "Bones".equals(pickable.getName())).distance()<=1;
        if(Pickables.getNearest(pickablePredicate)!=null){
            long endTime = System.currentTimeMillis();
            long timePassed = endTime - this.startTime;
            long timePassedSeconds = timePassed/1000;
            Log.info("Time Passed since Death: " + timePassed);
            Log.info("Time Passed since Death in Seconds: "+ timePassedSeconds);
        }
    }

}
