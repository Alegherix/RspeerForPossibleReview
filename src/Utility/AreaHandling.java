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
                        new Position(3057, 3522, 0),
                        new Position(3052, 3564, 0),
                        new Position(3112, 3559, 0),
                        new Position(3106, 3551, 0),
                        new Position(3117, 3538, 0),
                        new Position(3123, 3522, 0)
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
