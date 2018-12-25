import Utility.BankHandling;
import Utility.MuleHandling;
import Utility.RandomHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

import java.util.function.Predicate;

public class CompostMaker extends Script {
    Predicate<Item> compostBucket = item -> item.getName().equals("Compost");
    Predicate<Item> compostPotion = item -> item.getName().contains("Compost potion");
    boolean bankIsEmpty;

    @Override
    public void onStart() {
        bankIsEmpty = false;
    }

    @Override
    public int loop() {
        if(shouldWithdrawIngredients() && !bankIsEmpty){
            withdrawIngredients();
        }
        else if(Bank.isOpen()){
            Bank.close();
        }
        else if(shouldStartCombining()){
            combinePotionAndCompost();
        }
        else{
            MuleHandling.startMuling(BankLocation.EDGEVILLE);
        }
        return RandomHandling.randomNumber(285,415);
    }

    public boolean shouldWithdrawIngredients(){
        return !Inventory.contains(compostBucket);
    }

    public void withdrawIngredients(){
        BankHandling.depositAllAndWithdrawTwoItems(compostPotion,6, compostBucket, true);
        if(!Bank.contains(compostBucket) || !Bank.contains(compostPotion)){
            bankIsEmpty = true;
        }
    }

    public void combinePotionAndCompost(){
        Item[] buckets = Inventory.getItems(compostBucket);
        for (int i = 0; i < buckets.length; i++) {
            Inventory.getLast(compostPotion).interact("Use");
            RandomHandling.randomNumber(65,120);
            Inventory.getLast(compostBucket).interact("Use");
        }
    }

    public boolean shouldStartCombining(){
        return Inventory.contains(compostBucket) && Inventory.contains(compostPotion);
    }
}

