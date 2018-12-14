import Utility.BankHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.script.Script;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Tanner extends Script {


    private List<String> hideList;
    private Predicate<Item> hides = item -> hideList.contains(item.getName());
    private Predicate<Item> stamInInv = (i) -> Inventory.contains(item -> item.getName().matches("Stamina"));
    private Area tannerArea;

    @Override
    public void onStart() {
        hideList = Arrays.asList("Green dragonhide ", "Blue dragonhide", "Red dragonhide", "Black dragonhide");
        tannerArea = Area.rectangular(3264, 3161, 3279, 3194);

        super.onStart();
    }

    @Override
    public int loop() {

        return 0;
    }

    boolean shouldStartTaning(){
        return Inventory.contains(hides);
    }

    public void walkToBank(){
        BankHandling.walkAndDepositAllAndWithdraw(hides, 27);
    }



}
