package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public abstract class BankHandling extends Script {

    public static final String[] BANKFOOD = new String[]{"Trout", "Salmon", "Tuna"};
    public static final String FOOD_TO_USE = "Trout";

    public static void startBanking() {
        int nToWithdraw = ThreadLocalRandom.current().nextInt(24,28);
        if(!Bank.isOpen()){
            Bank.open();
        }
        else if(Bank.isOpen()){
            if(Inventory.getItems().length>0){
                Bank.depositInventory();
            }
            if(Inventory.getItems().length<=0){
                if(Bank.contains(BANKFOOD)){
                    Log.info("Contains Bankfood");
                    Bank.withdraw(FOOD_TO_USE , nToWithdraw);
                }
            }
        }
    }

    public static void depositAll() {
        if (!Bank.isOpen()) {
            Bank.open();
        }
        else if (Bank.isOpen()) {
            if (Inventory.getItems().length > 0) {
                Bank.depositInventory();
            }
        }
    }



    public static boolean canSeeBank() {
        return BankLocation.getNearest().getPosition().distance() < 15;
    }

    public static void walkToNearestBank(){
        Movement.walkTo(BankLocation.getNearest().getPosition());
    }

    public static void walkAndDepositAll(){
        if(!canSeeBank()){
            Log.info("Can't se bank so walking towards it");
            walkToNearestBank();
        }else if(canSeeBank()){
            Log.info("Can se the bank, therefore trying to open it");
            depositAll();
        }
    }

    public static void WalkAndDepositAllAndWithdraw(Predicate<Item> itemPredicate, int amount){
        if(!canSeeBank()){
            Log.info("Can't se bank so walking towards it");
            walkToNearestBank();
        }
        else if(canSeeBank()){
            if (!Bank.isOpen()) {
                Bank.open();
            }
            else if (Bank.isOpen()) {
                if(Inventory.isEmpty()){
                    Bank.withdraw(itemPredicate,amount);
                    Bank.close();
                }
                else if (Inventory.getItems().length > 0) {
                    Bank.depositInventory();
                }
            }
        }
    }

}
