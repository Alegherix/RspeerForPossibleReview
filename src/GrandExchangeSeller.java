import Utility.AreaHandling;
import Utility.LootHandling;
import Utility.WorldHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Utility.BankHandling.notedMode;
import static Utility.BankHandling.setNotedWithdrawals;
import static Utility.BankHandling.walkToBankAndOpen;
import static Utility.InterfaceHandling.*;


@ScriptMeta(developer = "Martin", desc = "Grand Exchange Seller", name = "Grand Exchange Seller")
public class GrandExchangeSeller extends Script {
    private boolean shouldSell;
    private boolean bankIsEmpty;
    List<String> itemsToSell;
    private final int MULE_WORLD = 503;


    @Override
    public void onStart() {
        shouldSell=false;
        bankIsEmpty=false;
        itemsToSell = LootHandling.lootsToSell();

        super.onStart();
    }

    //Todo - Accept Incoming trades,
    //Todo - Acceptera igenom hela traden
    //Todo Banka när Inventoriet är fullt och jag står i Edge
    //Todo - Gå till Grand Exchange
    //Todo Ta ut Fullt Inventory Noted
    //Todo Posta Auktioner
    //Todo - Byt world till 503
    //Todo - Tradea





    @Override
    public int loop() {
        //if shouldAcceptTrades()
        //Accept trades

        if(!playerAtGrandExchange()){
            walkToGrandExchange();
        }
        else if(playerAtGrandExchange()){
            if(shouldBank()){
                withdrawEverythingNoted();
            }
            if(shouldSell){
                openGE();
            }
            else if(bankIsEmpty){
                if(WorldHandling.loggedInAndShouldSwitch(MULE_WORLD)){
                    WorldHandling.switchToWorld(MULE_WORLD);
                }
                //Byt world
                //Tradea över guldet
            }
        }

        return 400;
    }

    private boolean shouldBank() {
        return Inventory.getFreeSlots()>=27;
    }

    void walkToGrandExchange(){
        if(Movement.getDestinationDistance()<=3.0){
            Movement.walkTo(AreaHandling.GRAND_EXCHANGE());
        }
    }


    public void openGE() {
        if (!GrandExchange.isOpen()) {
            Log.info("Trying to open GE");
            GrandExchange.open();
        }
        else if (GrandExchange.isOpen()) {
            if(shouldCollect()){
                collectAll();
                if(bankIsEmpty && GrandExchange.getOffers().length == 0){
                    shouldSell=false;
                }
            }
            else if (shouldCreateSellOffer()) {
                if (!GrandExchangeSetup.isOpen()) {
                    Log.info("Setup not open, Setting the offertype to Sell");
                    GrandExchange.createOffer(RSGrandExchangeOffer.Type.SELL);
                }
                else if (GrandExchangeSetup.isOpen()) {
                    Log.info("GE setup is open, Attempting to put up auction");
                    int firstItemToSell = Inventory.getItems()[0].getId();
                    GrandExchangeSetup.setItem(firstItemToSell);
                    GrandExchangeSetup.decreasePrice(3);
                    setAll();
                    GrandExchangeSetup.confirm();
                }
            }
        }
    }


    boolean shouldCreateSellOffer(){
        return true;
    }

    public boolean shouldCollect(){
        //Ge full or only Coins in Inv
        return GrandExchange.getOffers().length>=8 || Inventory.getFreeSlots() == 27;
    }

    public void collectAll(){
        GrandExchange.collectAll();
    }

    public void setAll(){
        if(PUT_UP_ALL_GE()!=null){
            PUT_UP_ALL_GE().click();
        }
    }

    public boolean playerAtGrandExchange(){
        return BankLocation.GRAND_EXCHANGE.getPosition().compareTo(Players.getLocal().getPosition())<=10;
    }

    public void withdrawEverythingNoted(){
        if(walkToBankAndOpen()) {
            if (!notedMode()) {
                setNotedWithdrawals();
            }
            else if (!Inventory.isFull() && Bank.getCount()>0) {
                List<String> itemsToWithdraw = new ArrayList<>();
                for(Item item : Bank.getItems()){
                    if(itemsToSell.contains(item.getName())){
                        itemsToWithdraw.add(item.getName());
                    }
                }

                for(String s : itemsToWithdraw){
                    if(!Inventory.isFull()){
                        Bank.withdrawAll(s);
                        Time.sleep(ThreadLocalRandom.current().nextInt(65,135));
                    }
                }
            }
            else if(Inventory.isFull() || Bank.getItems().length==0){
                Log.info("Inventory is full, or no more items in bank, Closing bank");
                shouldSell = true;
                if(Bank.getItems().length==0){
                    bankIsEmpty = true;
                }
                Bank.close();
            }
        }
    }
}
