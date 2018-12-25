import Utility.BankHandling;
import Utility.FoodHandling;
import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.pathfinding.region.util.Reachable;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.function.Predicate;

@ScriptMeta(developer = "Slazter", desc = "Al Kharid leverler", name = "AL-Kharid fighter")
public class AlkharidFighter extends Script {


    String warriors = "Al-Kharid warrior";
    Area fighterArea;
    Predicate<Item> strengthPot = item -> item.getName().contains("Strength pot");

    @Override
    public void onStart() {
        fighterArea = Area.rectangular(3303, 3177, 3282, 3159);
        super.onStart();
    }

    @Override
    public int loop() {
        if(!FoodHandling.hasFood()){
            if(BankHandling.walkToBankAndOpen()){
                if(Bank.contains(strengthPot)){
                    Bank.withdraw(strengthPot, 2);
                    RandomHandling.randomSleep(650,780);
                }
                else if(!Inventory.contains("Tuna")){
                    Bank.withdraw("Tuna",20);
                    RandomHandling.randomSleep(650,780);
                }
            }
        }
        else if(FoodHandling.hasFoodAndShouldEat(40)){
            FoodHandling.eat();
        }
        else if(!fighterArea.contains(Players.getLocal().getPosition())){
            RunningHandling.smartWalking(fighterArea.getCenter());
        }
        else if(Inventory.contains(strengthPot) && Skills.getCurrentLevel(Skill.STRENGTH) == Skills.getLevel(Skill.STRENGTH)){
            Log.info("Should boost stats");
            Inventory.getFirst(strengthPot).interact("Drink");
            RandomHandling.randomSleep(680,750);
        }
        else if(warrior()!=null){
            if(doorIsClosed() && (warrior().getX()<=3298 && Players.getLocal().getX()>=3299) ||(warrior().getX()>=3299 && Players.getLocal().getX()<=3298 )){
                Log.info("Should open door");
                SceneObjects.getNearest("Large door").interact("Open");
                RandomHandling.randomSleep(650,780);
            }
            else if(Players.getLocal().getTargetIndex()==-1){
                Log.info("Attacking warrior");
                warrior().interact("Attack");
            }
        }
        return RandomHandling.randomReturn();
    }

    public boolean doorIsClosed(){
        return SceneObjects.getNearest("Large door").containsAction("Open");
    }

    public Npc warrior(){
        return Npcs.getNearest(warriors);
    }

}
