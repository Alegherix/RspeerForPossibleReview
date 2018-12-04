package Utility;

import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.providers.RSWorld;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(developer = "Martin", desc = "Handles WorldHopping", name = "WorldHopper")
public abstract class WorldHandling extends Script {

    public static boolean isLoggedIn(){
        return Game.isLoggedIn();
    }

    private static  boolean shouldSwitchWorld(){
        return Worlds.getCurrent() != 319;
    }

    static public void switchToBHWorld(){
        WorldHopper.hopTo(RSWorld::isBounty);
    }

    public static boolean loggedInAndShouldSwitch(){
        return shouldSwitchWorld() &&isLoggedIn();
    }
}
