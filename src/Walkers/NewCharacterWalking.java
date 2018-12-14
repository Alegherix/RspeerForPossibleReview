package Walkers;

import Utility.RunningHandling;
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

        Movement.walkTo(new Position(3094,3491));
        return Random.mid(250,400);
    }

    public void dropAll(){
        Arrays.stream(Inventory.getItems()).forEach(item -> item.interact("Drop"));
    }
}

