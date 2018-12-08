import Utility.AreaHandling;
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
import static Utility.InterfaceHandling.*;


@ScriptMeta(developer = "Martin", desc = "Mule Accepter", name = "Mule Accepter")
public class MuleAccepter extends Script {


    //Todo - Accept Incoming trades,
    //Todo - Acceptera igenom hela traden
    //Todo Banka när Inventoriet är fullt och jag står i Edge
    //Todo - Gå till Grand Exchange
    //Todo Ta ut Fullt Inventory Noted
    //Todo Posta Auktioner



    @Override
    public int loop() {
        openGE();

        return 400;
    }

    void walkToGrandExchange(){
        if(Movement.getDestinationDistance()<=3.0){
            Movement.walkTo(AreaHandling.GRAND_EXCHANGE());
        }
    }

    public boolean shouldPutOnGe(){
        return !Inventory.isEmpty() && !Bank.isOpen();
    }

    public void openGE() {
        if (!GrandExchange.isOpen()) {
            Log.info("Trying to open GE");
            GrandExchange.open();
        }
        else if (GrandExchange.isOpen()) {

            if(shouldCollect()){
                collectAll();
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

    //Todo - If no empty slots, Collect

    public boolean shouldCollect(){
        return GrandExchange.getFirstEmpty()!=null;
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

}
