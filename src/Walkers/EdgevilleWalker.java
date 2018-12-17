package Walkers;

import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

import java.util.Arrays;

@ScriptMeta(developer = "Slazter", desc = "Edge Walkers.EdgevilleWalker", name = "Edgeville Walker")
public class EdgevilleWalker extends Script {

    @Override
    public int loop() {
        RunningHandling.smartWalking(BankLocation.EDGEVILLE.getPosition());
        return RandomHandling.randomReturn();
    }

}
