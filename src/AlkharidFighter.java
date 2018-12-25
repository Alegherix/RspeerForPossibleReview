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
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;

import java.util.function.Predicate;

public class AlkharidFighter extends Script {


    String warriors = "Al-Kharid warrior";
    Area fighterArea;
    Predicate<Item> strengthPot = item -> item.getName().matches("Strength pot");

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
        else if(warrior()!=null){
            if(warrior().getX()<=3298 && Players.getLocal().getX()>=3299 && doorIsClosed()){
                SceneObjects.getNearest("Door").interact("Open");
                RandomHandling.randomSleep(650,780);
            }
            else if(Players.getLocal().getTargetIndex()==-1){
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
