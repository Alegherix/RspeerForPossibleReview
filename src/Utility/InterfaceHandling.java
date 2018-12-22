package Utility;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String targetTimer(){
        return Interfaces.getComponent(90,48).getText();
    }

    public static boolean hasTimer(){
        try{
            if(targetTimer()!=null && targetTimer().length()>0){
                Pattern pattern = Pattern.compile("\\d+");
                Matcher m = pattern.matcher(targetTimer().substring(0,2));
                if(m.find()){
                    return true;
                }
            }
        }
        catch (Exception e){
            Log.info("Target timer doesn't exist atm");
        }
        return false;
    }

    public static boolean hasTarget(){
        return targetInterface()!=null &&
                targetName()!=null &&
                !"None".equals(targetInterface().getText()) &&
                !"<col=ff0000>---</col>".equals(targetName()) &&
                targetName().length()>0;
    }

    public static void abandonTarget(){
        if(Dialog.isOpen()){
            Dialog.process(0);
            RandomHandling.randomSleep(8500,10000);
        }
        else{
            Interfaces.getComponent(90,50).interact("Abandon target");
            RandomHandling.randomSleep(8500,10000);
            RandomHandling.randomSleep();
        }

    }
}
