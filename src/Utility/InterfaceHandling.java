package Utility;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;

public abstract class InterfaceHandling extends Script {




    public static InterfaceComponent firstTradeWindow(){
        return Interfaces.getComponent(335,30);
    }

    public static InterfaceComponent secondTradeWindow(){
        return Interfaces.getComponent(334,4);
    }

    public static final InterfaceComponent PUT_UP_ALL_GE(){
        return Interfaces.getComponent(465,24,48);
    }
}
