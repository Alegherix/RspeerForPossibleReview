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

    public static InterfaceComponent gloryInterface(){
        return Interfaces.getComponent(387,8);
    }

    public static InterfaceComponent edgevilleTeleportOption(){
        return Interfaces.getComponent(219,1,1);
    }

    public static InterfaceComponent targetInterface(){
        return Interfaces.getComponent(90,47);
    }

    public final static String targetName(){
        return InterfaceHandling.targetInterface().getText();
    }

    public static InterfaceComponent combatSpellInterface(){
        return Interfaces.getComponent(593,25);
    }

    public static InterfaceComponent autoCastInterface(){
        return Interfaces.getComponent(201,1);
    }

    public static void abandonTarget(){
        if(Dialog.isOpen()){
            Dialog.process(0);
            RandomHandling.randomSleep();
        }
        else{
            Interfaces.getComponent(90,50).interact("Abandon target");
            RandomHandling.randomSleep();
        }

    }
}
