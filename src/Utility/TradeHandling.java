package Utility;

import org.rspeer.script.Script;

public abstract class TradeHandling extends Script {

    private String traderName;
    private boolean tradePending;
    private final int TRADEWORLD = 318;

    @Override
    public void onStart() {
        tradePending = false;
        traderName = "";
    }
}
