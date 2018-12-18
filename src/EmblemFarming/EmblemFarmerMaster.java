package EmblemFarming;

import Utility.*;
import com.sun.media.jfxmedia.events.PlayerTimeListener;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static Utility.FoodHandling.*;
import static Utility.InterfaceHandling.firstTradeWindow;
import static Utility.InterfaceHandling.secondTradeWindow;
import static Utility.InterfaceHandling.targetName;
import static Utility.RandomHandling.randomReturn;
import static Utility.RandomHandling.randomSleep;
import static Utility.RandomHandling.slaves;


@ScriptMeta(developer = "Slazter", desc = "Emblem Master", name = "Emblem MASTER")
public class EmblemFarmerMaster extends EmblemFarmer {

    private final String HIGHEST_TIER_EMBLEM = "Mysterious emblem(10)";
    boolean haveTarget;
    private boolean haveDied;
    private List<String> gear;
    private List<String> runes;
    private List<String> slaves;
    private boolean haveAllGear;
    private boolean haveTraded;
    private final String TRADE_TEXT = "Waiting for other player...";
    private final Predicate<Pickable> lootPred = item -> item.distance(Players.getLocal())<=2 && item.getName().equals(LOWEST_TIER_EMBLEM);


    @Override
    public void onStart() {
        super.onStart();
        slaves = slaves();
        haveTarget = false;
        haveDied = false;
        gear = Arrays.asList("Zamorak monk bottom", "Zamorak monk top", "Staff of fire", "Blue wizard hat");
        runes = Arrays.asList("Mind rune", "Air rune");
        haveAllGear = true;
        haveTraded = false;

    }

    @Override
    public int loop() {
        if(playerInLumbridge()) {
            haveDied = true;
            haveAllGear = false;
            lumbridgeTeleportHandling();
        }
        else if(haveDied){
            if(Bank.isOpen() && haveAllGear){
                Bank.close();
            }
            else if(haveAllGear){
                equipGear();
                haveDied=false;
            }
            else if(BankHandling.walkToBankAndOpen()){
                withdrawGear();
            }
        }
        else if (shouldBank()) {
            bankHandling();
        }

        else if(MagicHandling.shouldSetupAutoCast()){
            MagicHandling.enableFireStrike();
        }

        else if(!playerInLootArea()){
            walkToLootArea();
        }
        else if(hasFoodAndShouldEat(90)){
            eat();
        }
        else if(emblemOnGround()!= null){
            lootEmblem();
        }
        else if(shouldFindTarget()){
          findTarget(slaves);
        }
        else if(shouldTradeEmblem()){
            Log.info("Trading over Emblem");
            tradeTarget();
        }
        else if(shouldFightBack()){
            attackTarget();
        }
        return RandomHandling.randomReturn();
    }


    private Pickable emblemOnGround(){
        return Pickables.getNearest(lootPred);
    }

    private void lootEmblem(){
        emblemOnGround().interact("Take");
        randomSleep();
    }


    private void withdrawGear() {
        if(BankHandling.walkToBankAndOpen()){
            if(!Inventory.contains(generalGloryPred)){
                Bank.withdraw(gloryPred,1);
                randomSleep();
            }
            else {
                for(String s: gear){
                    if(!Inventory.contains(s)){
                        Bank.withdraw(s,1);
                        randomSleep();
                    }
                }
                for(String rune : runes){
                    if(!Inventory.contains(rune)){
                        Bank.withdraw(rune,1500);
                        randomSleep();
                    }
                }
                haveAllGear = true;
            }
        }
    }

    public void equipGear(){
        for(Item armor : Inventory.getItems()){
            if(armor.containsAction("Wear")){
                armor.interact("Wear");
                randomReturn(75,100);
            }
            else if(armor.containsAction("Wield")){
                armor.interact("Wield");
            }
        }
    }


    public boolean interfaceIsShowingTarget(){
        return InterfaceHandling.targetInterface()!=null && !"None".equals(InterfaceHandling.targetInterface().getText());
    }

    public void bankHandling(){
        if (BankHandling.walkToBankAndOpen()) {

            if(Combat.isPoisoned() && !Inventory.isEmpty()){
                Bank.depositInventory();
            }

            else if(!Combat.isPoisoned()){
                if (Inventory.contains(HIGHEST_TIER_EMBLEM)) {
                    Bank.depositAll(HIGHEST_TIER_EMBLEM);
                }
                else if (!Inventory.contains(LOWEST_TIER_EMBLEM)) {
                    Log.info("Should withdraw Emblem");
                    Bank.withdraw(LOWEST_TIER_EMBLEM, 2);
                }
                else if (!CombatHandling.canEat()) {
                    Log.info("Should withdraw Food");
                    int amount = Math.min(10, Inventory.getFreeSlots());
                    Bank.withdraw("Sea turtle", amount);
                }
            }
            randomSleep();
        }
    }

    private boolean shouldBank() {
        return Inventory.contains(HIGHEST_TIER_EMBLEM) || !Inventory.contains(LOWEST_TIER_EMBLEM)
                || !hasFood() || Combat.isPoisoned();
    }

    public void updateTarget(){
        for(String s  : slaves){
            if(s.equals(targetName())){
                target = Players.getNearest(player -> player.getName().equals(s));
            }
        }
    }

    public boolean shouldTradeEmblem(){
        return Inventory.contains(LOWEST_TIER_EMBLEM) && !haveTraded;
    }


    public void tradeTarget(){
        if(Trade.isOpen()){
            if(!haveTraded){
                Inventory.getFirst(LOWEST_TIER_EMBLEM).interact("Offer");
                RandomHandling.randomSleep();
                haveTraded = true;
            }
            else if(haveTraded){
                if(secondTradeWindow()!=null){
                    if(!TRADE_TEXT.equals(secondTradeWindow().getText())){
                        Trade.accept();
                        haveTraded = false;
                    }
                }
                else if(firstTradeWindow()!=null){
                    if(!TRADE_TEXT.equals(firstTradeWindow().getText())){
                        Trade.accept();
                    }
                }
            }
        }
        else{
            target.interact("Trade with");
            Time.sleep(RandomHandling.randomNumber(1500,3800));
        }
    }

    public boolean shouldFightBack(){
        return Players.getLocal().isHealthBarVisible();
    }


    public void lootTarget(){

    }

}
