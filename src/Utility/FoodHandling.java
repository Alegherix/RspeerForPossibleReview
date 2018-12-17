package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;

import java.util.function.Predicate;

public abstract class FoodHandling extends Script {

    private static final Predicate<Item> FOOD = item -> item.containsAction("Eat");

    public static final boolean shouldEat(int percentCap){
        return Players.getLocal().getHealthPercent()<=percentCap;
    }

    public static final boolean hasFood(){
        return Inventory.contains(FOOD);
    }

    public static final boolean hasFoodAndShouldEat(int percentCap){
        return hasFood() && shouldEat(percentCap);
    }

    public static final void eat(){
        Inventory.getFirst(FOOD).interact("Eat");
        RandomHandling.randomSleep();
    }
}
