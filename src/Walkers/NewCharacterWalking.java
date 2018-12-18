package Walkers;

import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

import java.util.Arrays;

@ScriptMeta(developer = "Slazter", desc = "Drops And Walks", name = "New Character Walking")
public class NewCharacterWalking extends Script {

    @Override
    public int loop() {
        if(!Inventory.isEmpty()) {
            dropAll();
        }
        else{
            RunningHandling.smartWalking(BankLocation.EDGEVILLE.getPosition());
        }

        return RandomHandling.randomReturn();
    }

    public void dropAll(){
        Arrays.stream(Inventory.getItems()).forEach(item -> item.interact("Drop"));
    }
}

