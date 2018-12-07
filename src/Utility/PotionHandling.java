package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;

import java.util.Arrays;

public abstract class PotionHandling extends Script {

    public static void drinkEnergyPotion() {
        if (!Players.getLocal().isAnimating()) {
            Inventory.getFirst(item -> item.getName().startsWith("Energy")).interact("Drink");
            Time.sleep(500);
        }
    }

    public static void dropEmptyVials() {
        Item[] dropItem = Inventory.getItems(i -> i.getName().startsWith("Energy") || i.getName().equals("Vial"));
        Arrays.stream(dropItem).forEach(item -> item.interact("Drop"));
    }

    public static boolean shouldDrinkEnergyPot() {
        return Movement.getRunEnergy() <= 95 && Inventory.contains(item -> item.getName().startsWith("Energy"));
    }

    public static boolean shouldDropVials() {
        return Inventory.contains("Vial") ||
                (Inventory.contains(item -> item.getName().startsWith("Energy")) && Movement.getRunEnergy() >= 100);
    }

    public static int nPotionsToWithdraw() {
        return (100 - Movement.getRunEnergy()) / 30;
    }
}