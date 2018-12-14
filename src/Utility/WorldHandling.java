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

    public static final int BOUNTY_WORLD = 319;

    public static boolean isLoggedIn(){
        return Game.isLoggedIn();
    }

    public static  boolean shouldSwitchWorld(int world){
        return Worlds.getCurrent() != world;
    }

    private static  boolean shouldSwitchWorld(){
        return Worlds.getCurrent() != BOUNTY_WORLD;
    }


    static public void switchToWorld(int world){
        WorldHopper.hopTo(world);
    }

    public static boolean loggedInAndShouldSwitch(int world){
        return shouldSwitchWorld(world) && isLoggedIn();
    }


    public static boolean loggedInAndShouldSwitch(){
        return shouldSwitchWorld() && isLoggedIn();
    }
}

