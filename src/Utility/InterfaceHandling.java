package Utility;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;

public abstract class InterfaceHandling extends Script {


    public static boolean haveTarget(){
        String target = Interfaces.getComponent(90,45).getText();
        return target!=null && !"None".equals(target);
    }

    public static void abandonTarget(){
        if(Dialog.isOpen()){
            Dialog.process(0);
        }
        else{
            Interfaces.getComponent(90,48).interact("Abandon target");
        }

    }

    public static InterfaceComponent firstTradeWindow(){
        return Interfaces.getComponent(335,30);
    }

    public static InterfaceComponent secondTradeWindow(){
        return Interfaces.getComponent(334,4);
    }
}
