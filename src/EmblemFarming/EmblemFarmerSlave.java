package EmblemFarming;

import Utility.BankHandling;
import static Utility.InterfaceHandling.gloryInterface;


import Utility.RandomHandling;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;

import java.util.Arrays;
import java.util.List;

public class EmblemFarmerSlave extends EmblemFarmer implements ChatMessageListener {


    private String traderName;
    private boolean tradePending;
    boolean haveDied;
    private List<String> charactersToDieTo;

    @Override
    public void onStart() {
        tradePending = false;
        traderName = "";
        haveDied=false;
    }

    @Override
    public int loop() {
        if(playerInLumbridge()){
            if(!Inventory.contains(gloryPred)){
                withdrawGlory();
            }
            else if(Inventory.contains(gloryPred)){
                if(Bank.isOpen()){
                    Bank.close();
                }
                else{
                    teleportToEdgeville();
                }
            }
        }
        else if(shouldUnequipGlory()){
            unEquipGlory();
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
        else if(shouldWalkOut()){
            walkOut();
        }
        else if(shouldSkipTarget()){
            skipTarget();
        }
        else if(shouldInitiateAttack()){
            initiateAttack();
        }
        return RandomHandling.randomNumber(350,580);
    }



    private void initiateAttack() {

    }

    private boolean shouldInitiateAttack() {
        //Target . getY > lowest,
        return true;
    }


    public void bankGlory(){
        if(BankHandling.walkToBankAndOpen()){
            Bank.depositInventory();
        }
    }

    public void acceptEmblem(){
        if(Inventory.isEmpty()){
            if(Trade.isOpen()){
                if(Trade.hasOtherAccepted()){
                    Trade.accept();
                }
            }
            else if(tradePending){
                Players.getNearest(traderName).interact("Trade with");
                tradePending = false;
            }
        }
    }


    void withdrawGlory(){
        if(BankHandling.walkToBankAndOpen()){
            if(!Inventory.contains(gloryPred)){
                Bank.withdraw(gloryPred, 1);
            }
        }
    }

    boolean shouldUnequipGlory(){
        return Players.getLocal().getY()>3483 && gloryInterface()!=null && gloryInterface().getActions().length > 0;
    }

    public void unEquipGlory(){

    }

    boolean shouldBankGlory(){
        return Players.getLocal().getY()>3483 && Inventory.contains(item -> item.getName().contains("glory"));
    }

    public boolean inventoryContainsEmblem(){
        return Inventory.contains(item -> item.getName().contains("Emblem"));
    }

    boolean shouldWalkOut(){
        return inventoryContainsEmblem() && Players.getLocal().getY() < MAX_Y;
    }

    @Override
    public void notify(ChatMessageEvent msg) {
        if (!Trade.isOpen()) {
            if (msg.getType().equals(ChatMessageType.TRADE)) {
                traderName = msg.getSource();
                tradePending = true;
            }
        }
    }

}
