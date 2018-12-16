package Walkers;

import Utility.BankHandling;
import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

@ScriptMeta(developer = "Martin", desc = "Walks to Grand Exchange", name = "GE walker")
public class WalkToClosesBank extends Script {
    @Override
    public int loop() {
        if(RunningHandling.shouldRun()){
            RunningHandling.enableRun();
        }
        else{
            if(!Players.getLocal().isMoving())
                Movement.walkTo(BankLocation.GRAND_EXCHANGE.getPosition());
        }
        return RandomHandling.randomReturn();
    }
}
