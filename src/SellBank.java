
import Utility.BankHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.GrandExchangeSetup;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import static Utility.BankHandling.*;

@ScriptMeta(developer = "Martin", desc = "Sells entire bank at GE", name = "Grand Exchange Seller")
public class SellBank extends Script {

    private boolean shouldSell;

    @Override
    public int loop() {
        openGe();
        putUpAtGe();
        return 400;
    }

   public void openGe(){
        if(!GrandExchange.isOpen()){
            Log.info("Trying to open GE");
            GrandExchange.open();
        }
   }
   public void putUpAtGe(){
       Item[] sellList = Inventory.getItems();
       String toSell = sellList[0].getName();
       int amount = sellList[0].getStackSize();


        Log.info("Creating an offer");
        GrandExchange.getFirstEmpty().create(RSGrandExchangeOffer.Type.SELL);

        if(GrandExchangeSetup.isOpen()){
            Log.info("Setting item to sell");
            
            //GrandExchangeSetup.confirm();
            GrandExchangeSetup.setPrice(5);
            //GrandExchangeSetup.setItem(toSell);
        }




        /*
        Log.info("Setting the price");
        GrandExchangeSetup.setPrice(1);

        Log.info("Setting the quantity");
        GrandExchangeSetup.setQuantity(amount);

        Log.info("Confirming");
        GrandExchangeSetup.confirm();
        */
    }



   public void withdrawEverythingNoted() {
       if (!Bank.isOpen()) {
           Bank.open();
       } else if (Bank.isOpen()) {
           if (!notedMode()) {
               setNotedWithdrawals();
           }
       }
   }
}



