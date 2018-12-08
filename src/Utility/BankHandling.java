package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.Timer;
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

    public static boolean walkToBankAndOpen(){
        if(!canSeeBank()){
            walkToNearestBank();
        }
        else if(canSeeBank()){
            if(!Bank.isOpen()){
                Bank.open();
            }
            else if(Bank.isOpen()){
                return true;
            }
        }
        return false;
    }

    public static boolean notedMode(){
        return Bank.getWithdrawMode()== Bank.WithdrawMode.NOTE;
    }

    public static void setNotedWithdrawals(){
        Bank.setWithdrawMode(Bank.WithdrawMode.NOTE);
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

    public static void walkAndDepositAllAndWithdraw(Predicate<Item> itemPredicate, int amount){
        int tot = (int)Arrays.stream(Inventory.getItems(itemPredicate)).count();
        Log.info("Total amount of potions to withdraw is: "+ amount);

        if(!canSeeBank()){
            Log.info("Can't see bank so walking towards it");
            walkToNearestBank();
        }
        else if(canSeeBank()){
            if(Combat.isPoisoned()){
                if(!Bank.isOpen() && !Inventory.isEmpty()){
                    Bank.open();
                }
                else if(Bank.isOpen()){
                    Bank.depositInventory();
                    if(Inventory.isEmpty()){
                        Bank.close();
                    }
                }
            }
            else if(!Players.getLocal().isMoving() && !Bank.isOpen()){
                if (!Bank.isOpen() && tot != amount || (!Bank.isOpen() && amount==0)) {
                    Log.info("Opening Bank");
                    Bank.open();
                }
            }
            else {
                Log.info("Bank is open");
                if(Inventory.getFreeSlots() > amount && !Combat.isPoisoned()){
                    Log.info("Withdrawing item from Predicate");
                    if(!Inventory.contains(itemPredicate)){
                        Log.info("We don't have energy potion in Inventory");
                        if(amount==0){
                            amount=1;
                        }
                        Bank.withdraw(itemPredicate, amount);
                        Time.sleep(700);
                        Bank.close();
                    }
                    else if(Inventory.contains(itemPredicate) || !Bank.contains(itemPredicate)){
                        Log.info("We have the energy potion in our inventory, or bank doesn't contain it");
                        Bank.close();
                    }

                }
                else if (Inventory.getItems().length > amount) {
                    Log.info("Depositing Inventory");
                    Bank.depositInventory();
                }
            }
        }
    }

}
