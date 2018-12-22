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
import org.rspeer.runetek.api.input.Camera;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.geom.Area;
import java.lang.reflect.Array;
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
    private final Predicate<Pickable> lootPred = item -> item.distance(Players.getLocal())<=2 && item.getName().equals(LOWEST_TIER_EMBLEM);
    private Predicate<Item> emblempred = item -> item.getName().contains("emblem");


    // Todo - Hämta personen som hintaArrowen pekar på
    // Todo om den personen finns i listan, attacerka den.

    @Override
    public void onStart() {
        super.onStart();
        slaves = FileReading.readText("slaves");
        haveTarget = false;
        haveDied = false;
        gear = Arrays.asList("Zamorak monk bottom", "Zamorak monk top", "Staff of fire", "Blue wizard hat");
        runes = Arrays.asList("Mind rune", "Air rune");
        haveAllGear = true;
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
            Log.info("Should Bank");

            if(AreaHandling.shouldCrossDitch()){
                Log.info("Should cross ditch");
                AreaHandling.crossDitchToBank();
            }

            else{
                bankHandling();
            }

        }

        else if(MagicHandling.shouldSetupAutoCast()){
            Log.info("Should setup Magic");
            MagicHandling.enableFireStrike();
        }

        else if(!playerInLootArea()){
            Log.info("Should walk to Loot Area");
            walkToLootArea();
        }
        else if(hasFoodAndShouldEat(90)){
            Log.info("Should Eat");
            eat();
        }
        else if(emblemOnGround()!= null){
            Log.info("Should Loot");
            lootEmblem();
        }
        else if (target!=null && Players.getNearest(target.getName()).isHidden()){
            // Om har target men ej ser längre, då är target null
            Log.info("Setting to null as Target probably died");
            target = null;
        }

        else if(shouldFindTarget()){
          findTarget(slaves);
        }

        else if(shouldFightBack()){
            Log.info("Should fight back");
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



    public void bankHandling(){
        if (BankHandling.walkToBankAndOpen()) {
            if(Combat.isPoisoned() && !Inventory.isEmpty()){
                Bank.depositInventory();
            }

            else if(!Combat.isPoisoned()){
                if (Inventory.contains(HIGHEST_TIER_EMBLEM)) {
                    Bank.depositAll(HIGHEST_TIER_EMBLEM);
                }
                else if (!Inventory.contains(emblempred)) {
                    Log.info("Should withdraw Emblem");
                    Bank.withdraw(LOWEST_TIER_EMBLEM, 1);
                }
                else if (!CombatHandling.canEat()) {
                    Log.info("Should withdraw Food");
                    int amount = Math.min(10, Inventory.getFreeSlots());
                    Bank.withdraw("Shark", amount);
                }
                else if(!hasRunes()){
                    Bank.withdraw("Mind rune",500);
                    randomSleep();
                    Bank.withdraw("Air rune",500);
                    randomSleep();
                }
            }
            randomSleep();
        }
    }

    private boolean shouldBank() {
        return Inventory.contains(HIGHEST_TIER_EMBLEM) || !Inventory.contains(emblempred)
                || !hasFood() || Combat.isPoisoned() || !hasRunes();
    }

    private boolean hasRunes() {
        return Inventory.contains("Mind rune","Air rune");
    }

    public boolean shouldFightBack(){
        return Players.getLocal().isHealthBarVisible();
    }


}
