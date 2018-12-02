
import Utility.BankHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

import java.util.function.Predicate;

import static Utility.RunningHandling.nPotionsToWithdraw;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    private Predicate<Item> energyPredicate;

    @Override
    public void onStart() {
        energyPredicate = item -> item.getName().startsWith("Energy");

        super.onStart();
    }

    @Override
    public int loop() {

        if(shouldBank()){
            BankHandling.walkAndDepositAllAndWithdraw(energyPredicate, nPotionsToWithdraw());
        }

        return 400;
    }

    public static void main(String[] args) {

    }

    public boolean shouldBank(){
        boolean keepOpen = Bank.isOpen() && Bank.contains(energyPredicate) && !Inventory.contains(energyPredicate);
        return Inventory.getItems().length >27 || Combat.isPoisoned() || keepOpen;
    }

}
