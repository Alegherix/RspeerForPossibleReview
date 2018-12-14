package Utility;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.*;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

import static Utility.BankHandling.*;
import static Utility.InterfaceHandling.*;

@ScriptMeta(developer = "Martin", desc = "Mule Trader", name = "Mule Trader")
public abstract class MuleHandling extends Script {


    private static final String PLAYER_TRADING_TO = "psychoalfa9";
    private static final String TRADE_TEXT = "Waiting for other player...";
    static boolean shouldTrade = false;
    static boolean secondScreenWasVisible = false;
    static boolean bankIsEmpty = false;


    private static void offerEntireInventory(){
        for(Item i : Inventory.getItems()){
            Trade.offerAll(i.getName());
            Time.sleep(ThreadLocalRandom.current().nextInt(65,125));
        }
        Trade.offerAll(item -> Inventory.contains(item.getName()));
    }

    private static boolean canFindPersonToMuleTo(){
        return playerToTrade()!=null;
    }

    private static Player playerToTrade(){
        return Players.getNearest(player -> player.getName().equals(PLAYER_TRADING_TO));
    }

    private static void tradePlayer(){
        if(Trade.isOpen()){
            if(!Inventory.isEmpty()){
                offerEntireInventory();
            }
            else if(Inventory.isEmpty()){
                if(secondTradeWindow()!=null){
                    if(!TRADE_TEXT.equals(secondTradeWindow().getText())){
                        secondScreenWasVisible=true;
                        Trade.accept();
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
            playerToTrade().interact("Trade with");
            Time.sleep(RandomHandling.randomNumber(1500,14523));
        }
    }

    private static void withdrawEverythingNoted(){
        //Assume bank is open
        if(walkToBankAndOpen()) {
            if (!notedMode()) {
                setNotedWithdrawals();
            }
            else if (!Inventory.isFull() && Bank.getCount()>0) {
                Item[] items = Bank.getItems();
                for (int i = 0; i < items.length; i++) {
                    Bank.withdrawAll(items[i].getName());
                    Time.sleep(ThreadLocalRandom.current().nextInt(65,185));
                }
            }
            else if(Inventory.isFull() || Bank.getItems().length==0){
                //Log.info("Inventory is full, or no more items in bank, Closing bank");
                shouldTrade = true;
                if(Bank.getItems().length==0){
                    bankIsEmpty = true;
                }
                Bank.close();
            }
        }
    }


    public static int startMuling(BankLocation location) {
        if(location.getPosition().compareTo(Players.getLocal().getPosition())<=10){

            if(secondScreenWasVisible && shouldTrade && !bankIsEmpty && secondTradeWindow()==null){
                shouldTrade = false;
                secondScreenWasVisible = false;
            }

            else if(secondScreenWasVisible && bankIsEmpty && secondTradeWindow()==null){
                Log.info("We've cleared the bank by now");
                Log.info("Time to logout");
                Time.sleep(900000);
                Game.logout();
            }

            else if(shouldTrade){
                Log.info("Should Trade other player");
                if(canFindPersonToMuleTo()){
                    Log.info("Can find player to trade to");
                    tradePlayer();
                }
            }
            else{
                Log.info("Should withdraw items from bank");
                withdrawEverythingNoted();
            }
        }
        else {
            if(AreaHandling.shouldCrossDitch()){
                AreaHandling.crossDitchToBank();
            }
            else{
                Movement.walkTo(location.getPosition());
            }
        }
        return RandomHandling.randomNumber(350,600);
    }
}
