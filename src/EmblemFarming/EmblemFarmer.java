package EmblemFarming;

import Utility.AreaHandling;
import Utility.InterfaceHandling;
import Utility.RandomHandling;
import Utility.WorldHandling;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import static Utility.RandomHandling.*;
import static Utility.RunningHandling.enableRun;
import static Utility.InterfaceHandling.*;

import java.util.List;
import java.util.function.Predicate;

public abstract class EmblemFarmer extends Script {

    protected final Predicate<Item> gloryPred = item -> item.getName().matches("Amulet of glory\\([1-6]\\)");
    List<String> playersToKill;
    private final int WORLD = 318;
    private Area lumbridge;
    protected static Area WILDY_LOOT_AREA;
    protected final int MAX_Y = 3524;


    @Override
    public void onStart() {
        lumbridge = Area.rectangular(3226, 3205, 3202, 3230);
        WILDY_LOOT_AREA = AreaHandling.initiateWildernessArea();

    }

    public boolean playerInLumbridge(){
        return lumbridge.contains(Players.getLocal().getPosition());
    }


    protected void skipTarget() {
        Log.info("Skipping target");
        InterfaceHandling.abandonTarget();
    }



    public void teleportToEdgeville(){
        if(!Players.getLocal().isAnimating()){
            gloryInterface().interact("Edgeville");
            Time.sleep(RandomHandling.randomNumber(350,450));
        }
    }

    public boolean shouldSwitchGlory(){
        return !gloryInterface().containsAction("Edgeville");
    }

    boolean playerInLootArea(){
        return WILDY_LOOT_AREA.contains(Players.getLocal().getPosition());
    }

    protected void walkOut(){
        Movement.walkTo(new Position(Players.getLocal().getPosition().getX(), Players.getLocal().getY()+1));
    }

    public void walkToLootArea(){
        SceneObject ditch = SceneObjects.getNearest("Wilderness Ditch");
        InterfaceComponent wildyInterface = Interfaces.getComponent(475,11);

        if (wildyInterface!=null) {
            wildyInterface.interact("Enter Wilderness");
        }
        else if(ditch!=null && Players.getLocal().getPosition().getX()<=3110){
            ditch.interact("Cross");
        }
        else{
            if(Players.getLocal().getPosition().getY() < 3521){
                Log.info("Trying to walk back to Wilderness");
                Movement.walkTo(new Position(3087, 3520, 0));
            }
            else{
                Log.info("Trying to walk back to the middle of wildy");
                Movement.walkTo(new Position(3090, 3533));
            }
        }
    }
}
