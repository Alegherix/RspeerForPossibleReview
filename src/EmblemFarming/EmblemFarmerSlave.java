package EmblemFarming;

import Utility.BankHandling;
import static Utility.InterfaceHandling.gloryInterface;
import static Utility.InterfaceHandling.targetInterface;


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

@ScriptMeta(developer = "Slazter", desc = "Emblem slave", name = "Emblem Slave")
public class EmblemFarmerSlave extends EmblemFarmer implements ChatMessageListener {


    private String master;
    private boolean tradePending;
    boolean haveDied;
    private List<String> masters;
    Predicate<Player> playerPredicate;

    @Override
    public void onStart() {
        super.onStart();
        tradePending = false;
        master = "";
        haveDied=false;
        masters = Arrays.asList("psychoalfa9","Anotherone");
    }

    @Override
    public int loop() {
        if(playerInLumbridge()) {
            if(Inventory.contains(gloryPred)){
                if(Bank.isOpen()){
                    Bank.close();
                }
                else{
                    teleportToEdgeville();
                }
            }
            else if (BankHandling.walkToBankAndOpen()) {
                if (!Inventory.contains(gloryPred)) {
                    withdrawGlory();
                }
            }
        }

        else if(shouldBankGlory()){
            bankGlory();
        }
        else if(!playerInLootArea()){
            walkToLootArea();
        }
        else if(!inventoryContainsEmblem()){
            acceptEmblem();
        }
        // Allt fungerar fram hit minst
        else if(shouldWalkOut()){
            Log.info("Should walk out");
            walkOut();
        }
        else if(shouldSkipTarget()){
            Log.info("Should Skip target");
            skipTarget();
        }
        else if(getMaster()!=null && master==null){
            Log.info("Setting Master");
                setMaster();
        }
        else if(shouldInitiateAttack()){
            initiateAttack();
        }
        return RandomHandling.randomNumber(350,580);
    }




     boolean shouldSkipTarget() {
        return targetInterface()!= null && !masters.contains(targetInterface().getText());
    }

    private void initiateAttack() {
        Log.info("Attacking");
        Players.getNearest(master).interact("Attack");
    }

    private boolean shouldInitiateAttack() {
        Log.info("Should initiate Attack");
        //Master har samma y som vi har, därför initia attack
        return Players.getNearest(master).getY()>=Players.getLocal().getY();
    }

    public void setMaster(){
        Log.info("Master exist so setting it");
        master = getMaster();
    }

    public String getMaster(){
        Log.info("Trying to get master");
        return masters.stream().filter(master -> master.equals(targetInterface().getText())).findFirst().orElse(null);
    }




    public void acceptEmblem(){
        Log.info("Waiting for Emblem");
        if(Inventory.isEmpty()){
            if(Trade.isOpen()){
                Log.info("Acceptng Emblem");
                if(Trade.hasOtherAccepted()){
                    Trade.accept();
                }
            }
            else if(tradePending){
                Players.getNearest(master).interact("Trade with");
                tradePending = false;
            }
        }
    }


    void withdrawGlory(){
        if(BankHandling.walkToBankAndOpen()){
            if(!Inventory.contains(gloryPred)){
                Bank.withdraw(gloryPred, 1);
                Time.sleep(RandomHandling.randomNumber(580,650));
            }
        }
    }


    boolean shouldBankGlory(){
        return Players.getLocal().getY()>3483 && Inventory.contains(generalGloryPred);
    }

    public void bankGlory(){
        Log.info("Banking Glory");
        if(BankHandling.walkToBankAndOpen()){
            Bank.deposit(generalGloryPred,1);
            RandomHandling.randomReturn();
        }
    }

    public boolean inventoryContainsEmblem(){
        return Inventory.contains(item -> item.getName().contains("emblem"));
    }

    boolean shouldWalkOut(){
        Log.info("Should walk out");
        return inventoryContainsEmblem() && Players.getLocal().getY() < MAX_Y;
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if (!Trade.isOpen()) {
            if (msg.getType().equals(ChatMessageType.TRADE)) {
                master = msg.getSource();
                tradePending = true;
            }
        }
    }

}
