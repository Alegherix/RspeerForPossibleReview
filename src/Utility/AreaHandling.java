package Utility;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.pathfinding.region.util.Reachable;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;

public abstract class AreaHandling extends Script {

    public static Area initiateWildernessArea(){
        return Area.polygonal(
                new Position[] {
                        new Position(3125, 3522, 0),
                        new Position(3102, 3563, 0),
                        new Position(3031, 3563, 0),
                        new Position(3013, 3562, 0),
                        new Position(3019, 3546, 0),
                        new Position(3027, 3542, 0),
                        new Position(3029, 3535, 0),
                        new Position(3033, 3529, 0),
                        new Position(3039, 3522, 0)
                }
        );
    }

    public static final Position GRAND_EXCHANGE(){
        return BankLocation.GRAND_EXCHANGE.getPosition();
    }

    public static boolean shouldCrossDitch(){
        return Players.getLocal().getY()>3523;
    }

    public static void crossDitchToBank(){
        SceneObject ditch = SceneObjects.getNearest("Wilderness Ditch");
        if(ditch!=null && Players.getLocal().getPosition().getY()>=3523){
            ditch.interact("Cross");
        }
        else{
            Movement.walkTo(new Position(Players.getLocal().getX(),3523));
        }
    }
}
