package Backups;

import Utility.BankHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.function.Predicate;



@ScriptMeta(developer = "Martin", desc = "Goblin Killer", name = "Goblin Killer")

public class GoblinKiller extends Script {

    public static Position GOBLIN_POSITION = new Position(3145,3295);
    public static Predicate<Item> FOOD = item -> item.containsAction("Eat");

    public static final String[] BANKFOOD = new String[]{"Trout", "Salmon", "Tuna"};


    @Override
    public int loop() {
        if(!playerHasFood()) {
            if (!BankHandling.canSeeBank()) {
                BankHandling.walkToNearestBank();
            }
            else {
                BankHandling.startBanking();
            }
        }
        else if(!isCloseToGoblins()){
            walkToGoblins();
        }

        else if(isCloseToGoblins()){
            if(playerHasLowHP() && playerHasFood()){
                eatFood();
                }
            else{
                fightGoblins();
            }
        }

        return 500;
    }


    public void fightGoblins(){
        String npcToKill = "Goblin";
        if(Players.getLocal().getTargetIndex() == -1){
            Log.info("Attacking");
            Npcs.getNearest("Goblin").interact("Attack");
        }
    }

    public boolean isCloseToGoblins(){
        return GOBLIN_POSITION.distance()<20;
    }

    public void walkToGoblins(){
            Movement.walkTo(GOBLIN_POSITION);
    }

    public boolean playerHasLowHP(){
        return Players.getLocal().getHealthPercent()<=30;
    }

    public boolean playerHasFood(){
        return Inventory.contains(FOOD);
    }

    public void eatFood(){
        Inventory.getFirst(FOOD).interact("Eat");
    }


}