package EmblemFarming;

import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;

import static Utility.RandomHandling.characterList;
import static Utility.RandomHandling.slaves;

public class EmblemFarmerMaster extends EmblemFarmer {

    boolean haveTarget;
    private Player target;

    @Override
    public void onStart() {
        super.onStart();
        playersToKill = slaves();
        haveTarget = false;

    }

    @Override
    public int loop() {
        if(playerInLumbridge()){
            teleportToEdgeville();
        }
        else if(!playerInLootArea()){
            walkToLootArea();
        }
        else {

        }
        return 0;
    }



    public boolean shouldTradeEmblem(){
        return Inventory.contains(item -> item.getName().equals("Mysterious emblem"));
    }

    public void tradeEmblem(){
        // Om namnet som mitt nuvarande target är finns i listan över karaktärer att mörda
        // Tradea över emblemet till den
    }

    boolean shouldWalkOut(){
        return target.getY()>Players.getLocal().getY();
    }

    boolean haveCorrectTarget(){
        return false;
    }

    void walkNextToTarget(){

    }

    public void attackTarget(){
        if(!Players.getLocal().isAnimating()){
            target.interact("Attack");
        }
    }

    public void loot(){

    }

    public boolean haveKilledTarget(){
        //
        return true;
    }

    public boolean shouldTurnInEmblem(){
        return true;
    }

    public void turnInEmblem(){

    }

}
