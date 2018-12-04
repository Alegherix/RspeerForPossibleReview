package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class RunningHandling extends Script {

    static Map<String, Double> weights = new HashMap<>();


    public static void drinkEnergyPotion(){
        if(!Players.getLocal().isAnimating()){
            Inventory.getFirst(item-> item.getName().startsWith("Energy")).interact("Drink");
        }
    }

    public static void dropEmptyVials(){
        Item[] dropItem = Inventory.getItems(i-> i.getName().startsWith("Energy") || i.getName().equals("Vial"));
        Arrays.stream(dropItem).forEach(item -> item.interact("Drop"));
    }

    public static boolean shouldDrinkEnergyPot(){
        return Movement.getRunEnergy()<=95 && Inventory.contains(item -> item.getName().startsWith("Energy"));
    }

    public static boolean shouldDropVials(){
        return Inventory.contains("Vial") ||
                (Inventory.contains(item -> item.getName().startsWith("Energy")) && Movement.getRunEnergy()>=100);
    }

    public static void drinkAndDrop(){
        if(shouldDrinkEnergyPot()){
            drinkEnergyPotion();
        }
        else if(shouldDropVials()){
            dropEmptyVials();
        }
    }

    public static int nPotionsToWithdraw(){
        return (100 - Movement.getRunEnergy()) / 30;
    }

    public static double playerWeight(){
        return Arrays.stream(Inventory.getItems())
                .map(Item::getName)
                .mapToDouble(RunningHandling::calculateWeight)
                .sum();
    }

    public static double calculateWeight(String item){
        return weights.getOrDefault(item, 0.0);
    }



    public static boolean shouldRunToLoot(){

        return true;
    }
}
