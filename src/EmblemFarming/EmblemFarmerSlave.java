package EmblemFarming;

import Utility.BankHandling;
import static Utility.InterfaceHandling.gloryInterface;
import static Utility.InterfaceHandling.targetInterface;


import Utility.FileReading;
import Utility.InterfaceHandling;
import Utility.RandomHandling;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@ScriptMeta(developer = "Slazter", desc = "Emblem slave", name = "Emblem SLAVE")
public class EmblemFarmerSlave extends EmblemFarmer{


    boolean haveDied;
    private List<String> masters;

    @Override
    public void onStart() {
        super.onStart();
        haveDied=false;
        masters = FileReading.readText("masters");
    }

    @Override
    public int loop() {
        if(playerInLumbridge()) {
            target = null;
            lumbridgeTeleportHandling();
        }
        else if(shouldBankGlory()){
            bankGlory();
        }
        else if(!playerInLootArea()){
            walkToLootArea();
        }
        else if(shouldFindTarget()){
          findTarget(masters);
        }
        else if(shouldInitiateAttack()){
            initiateAttack();
        }
        return RandomHandling.randomReturn();
    }



    private void initiateAttack() {
        Log.info("Attacking");
        if(Players.getLocal().getTargetIndex() == -1){{
            Players.getNearest(target.getName()).interact("Attack");
        }}

    }

    private boolean shouldInitiateAttack() {
        Log.info("Should initiate Attack");
        return Players.getNearest(target.getName()).getY() >= Players.getLocal().getY();
    }


    boolean shouldBankGlory(){
        return Players.getLocal().getY()>3483 && Inventory.contains(generalGloryPred);
    }

    public void bankGlory(){
        Log.info("Banking Glory");
        if(BankHandling.walkToBankAndOpen()){
            Bank.deposit(generalGloryPred,1);
            RandomHandling.randomSleep();
        }
    }

}
