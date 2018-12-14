import Utility.BankHandling;
import Utility.MuleHandling;
import Utility.RandomHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@ScriptMeta(developer = "Slazter", desc = "Grinds Unicorn and Goat horns to dust", name = "Dust Maker")

public class DustMaker extends Script {
    private List<String> horns = Arrays.asList("Unicorn horn", "Desert goat horn", "Bird nest");
    private Predicate<Item> hornPred = item -> item.getName().equals("Desert goat horn") || item.getName().equals("Unicorn horn")|| item.getName().equals("Bird nest");
    private boolean bankIsEmpty;
    private String pestle = "Pestle and mortar";

    @Override
    public void onStart() {
        bankIsEmpty = false;
        super.onStart();
    }

    @Override
    public int loop() {
        if(bankIsEmpty){
            MuleHandling.startMuling(BankLocation.EDGEVILLE);
        }
        else if(shouldBank() && !bankIsEmpty){
            Log.info("Trying to withdraw");
            bank();
        }
        else if(Inventory.contains(hornPred)){
            if(Bank.isOpen()){
                if(!Bank.contains(hornPred)){
                    bankIsEmpty=true;
                }
                Bank.close();
            }
            else{
                makeDust();
            }
        }
        return 350;
    }


    public void makeDust(){
        for(Item i : Inventory.getItems()){
            if(hornPred.test(i)){
                Inventory.getFirst(pestle).interact("Use");
                Time.sleep(RandomHandling.randomNumber(65,101));
                i.interact("Use");
            }
        }
    }

    public boolean shouldBank(){
        return Arrays.stream(Inventory.getItems()).noneMatch(item -> horns.contains(item.getName()));
    }

    public void bank(){
        BankHandling.depositAllAndWithdrawAll(hornPred,"Pestle and mortar");
    }
}
