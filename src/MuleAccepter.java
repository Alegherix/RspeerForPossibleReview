import Utility.RandomHandling;
import Utility.WorldHandling;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.InterfaceAddress;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.chatter.ChatMode;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import static Utility.WorldHandling.*;
import static Utility.BankHandling.*;

@ScriptMeta(developer = "Martin", desc = "Accepts trades, then deposits", name = "Accepter")
public class MuleAccepter extends Script implements ChatMessageListener {

    private String traderName;
    private boolean tradePending;
    private final int TRADEWORLD = 318;

    @Override
    public void onStart() {
        tradePending = false;
        traderName = "";
    }

    @Override
    public int loop() {
        if(isLoggedIn()){

            if(shouldSwitchWorld((TRADEWORLD))){
                switchToWorld(TRADEWORLD);
            }

            else if(!Inventory.isEmpty()){
                if(walkToBankAndOpen()){
                    Bank.depositInventory();
                    Time.sleep(RandomHandling.randomNumber(300,450));

                }
            }

            else if(Inventory.isEmpty() && Bank.isOpen()){
                Bank.close();
            }

            else if(Inventory.isEmpty()){
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
        return RandomHandling.randomNumber(300,450);
    }



    public void declineTrade(){
        Interfaces.getComponent(Trade.FIRST_DECLINE.getRoot(), Trade.FIRST_DECLINE.getComponent()).click();
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
