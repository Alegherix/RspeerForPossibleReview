package EmblemFarming;

import Utility.*;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.ui.Log;

import static Utility.RandomHandling.*;
import static Utility.InterfaceHandling.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class EmblemFarmer extends Script {

    protected final Predicate<Item> gloryPred = item -> item.getName().matches("Amulet of glory\\([1-6]\\)");
    protected final Predicate<Item> generalGloryPred = item -> item.getName().contains("glory");
    protected final String LOWEST_TIER_EMBLEM = "Mysterious emblem";
    private Area lumbridge;
    private Area lumbridgeLvl1;
    private Area lumbridgeLvl2;
    protected static Area WILDY_LOOT_AREA;
    protected final int MAX_Y = 3524;
    protected Player target;
    protected String targetName;
    protected boolean haveLogged;
    protected int counter;


    @Override
    public void onStart() {
        lumbridge = Area.rectangular(3226, 3205, 3202, 3230,0);
        lumbridgeLvl1 = Area.rectangular(3226, 3205, 3202, 3230,1);
        lumbridgeLvl2 = Area.rectangular(3226, 3205, 3202, 3230,2);
        WILDY_LOOT_AREA = AreaHandling.initiateWildernessArea();
        target = null;
        targetName = "";
        haveLogged = false;
        counter = 0;
    }


    // TODO - Kommer behöva resetta haveLogged

    //                                      Deals with Getting Back                                                  //

    public boolean playerInLumbridge(){
        return lumbridge.contains(Players.getLocal().getPosition()) ||
                lumbridgeLvl1.contains(Players.getLocal().getPosition()) ||
                lumbridgeLvl2.contains(Players.getLocal().getPosition());
    }

    public void lumbridgeTeleportHandling(){
        if(Inventory.contains(gloryPred)){
            if(Bank.isOpen()){
                Bank.close();
            }
            else{
                teleportToEdgeville();
            }
        }
        else if (BankHandling.walkToBankAndOpen()) {
            if (!Inventory.contains(gloryPred)) {
                withdrawGlory();
            }
        }
    }


    protected void withdrawGlory(){
        if(BankHandling.walkToBankAndOpen()){
            if(!Inventory.contains(gloryPred)){
                Bank.withdraw(gloryPred, 1);
                randomSleep(580,650);
            }
        }
    }



    public void teleportToEdgeville(){
        if(edgevilleTeleportOption() != null){
            edgevilleTeleportOption().click();
        }
        else if(!Players.getLocal().isAnimating()){
            Inventory.getFirst(item -> item.getName().contains("glory")).interact("Rub");
        }
        randomSleep();
    }

    //                                        STOPS DEALING WITH WALKING BACK TO EDGEVILLE                            //


    //                                        DEALS WITH WALKING TO WILDERNESS AREA                                   //

    boolean playerInLootArea(){
        return WILDY_LOOT_AREA.contains(Players.getLocal().getPosition());
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

    //                                        STOPS DEALING WITH WALKING TO WILDERNESS AREA                           //


    protected boolean shouldWalkOut(){
        return Players.getLocal().getY()<=3524;
    }

    protected void walkOut(){
        Movement.walkTo(new Position(Players.getLocal().getPosition().getX(), Players.getLocal().getY()+1));
        RandomHandling.randomSleep();
    }

    protected boolean shouldFindTarget() {
        return target == null;
    }

    protected void findTarget(List<String> targets){
        if(shouldWalkOut()){
            walkOut();
        }
        /*
        else if(counter >=6 ){
            Game.logout();
            randomSleep();
            counter =0;
        }
        */
        else if(hasTimer() && !haveLogged){
            logoutUntilNoTimer();
        }
        else if(hasTarget()) {
            if(haveLogged){
                haveLogged = false;
            }
            else if(correctNameInList(targetName(), targets)) {
                if (target == null) {
                    target = getTarget(targets);
                }
            }
            else{
                abandonTarget();
            }
        }
    }

    public static boolean correctNameInList(String name, List<String> nameList){
        return nameList.stream().anyMatch(namesInList -> namesInList.equals(name));
    }


    public Player getTarget(List<String> targets){
        for(String name : targets){
            if(Players.getNearest(name)!=null){
                return Players.getNearest(name);
            }
        }
        return null;
    }

    public void attackTarget(){
        if(Players.getLocal().getTargetIndex() == -1){
            target.interact("Attack");
        }
    }

    public void logoutUntilNoTimer(){
        try{
            String stringTime = targetTimer().substring(0,2).replaceAll("\\D","");
            int time = Integer.parseInt(stringTime);
            Game.logout();
            Time.sleep(time*60 * 1000);
            haveLogged = true;
        }
        catch (Exception e){
            Log.info("Numbers were not parsed correctly");
        }
    }
}
