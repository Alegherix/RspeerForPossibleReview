package Utility;

import org.rspeer.runetek.api.component.tab.Magic;
import org.rspeer.runetek.api.component.tab.Spell;
import org.rspeer.script.Script;

import static Utility.InterfaceHandling.autoCastInterface;
import static Utility.InterfaceHandling.combatSpellInterface;

public abstract class MagicHandling extends Script {

    public static void enableFireStrike(){
        if(autoCastInterface()!=null){
            autoCastInterface().getComponent(4).interact("Fire Strike");
        }
        else if(combatSpellInterface()!=null){
            combatSpellInterface().interact("Choose spell");

        }
        RandomHandling.randomSleep();
    }

    public static boolean shouldSetupAutoCast(){
        return !Magic.isAutoCasting();
    }
}
