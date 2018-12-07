import Utility.*;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.WorldHopper;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static Utility.WorldHandling.*;
import static Utility.PotionHandling.*;
import static Utility.RunningHandling.*;
import static Utility.LootHandling.*;
import static Utility.InterfaceHandling.*;



@ScriptMeta(developer = "Martin", desc = "Wilderness Looter", name = "Wildy Looter")
public class WildernessLooter extends Script {
    private static int deathAnimation;
    private static LinkedList<DyingSpot> dyingSpotsList;
    private static int returnTime;
    private static List<String> loots;
    private static boolean lootHaveSpawned;
    private static long backupTime;
    private static boolean timerStarted;
    private Predicate<Item> energyPredicate;
    private Predicate<Pickable> generalLootPredicate;
    private static Area WILDY_LOOT_AREA;
    private boolean haveDied;
    private static Area lumbridge;


    @Override
    public void onStart() {
        // Initiate Areas
        WILDY_LOOT_AREA = AreaHandling.initiateWildernessArea();
        lumbridge = Area.rectangular(3217, 3223, 3226, 3214);

        // Initiate Lists
        dyingSpotsList = new LinkedList<>();
        loots = LootHandling.initiateLoots();
        //weights = WeightHandling.initiateMap(new HashMap<>());

        //Initiate Variables
        deathAnimation = 836;
        lootHaveSpawned = false;
        timerStarted = false;
        haveDied=false;

        //Initiate Predicates
        energyPredicate = item -> item.getName().startsWith("Energy");
        generalLootPredicate = loot ->
                loot.getPosition().getY()>=3522 && loot.getPosition().getY()<=3563 &&
                loot.getPosition().getX() >=3057 && loot.getPosition().getX()<=3107 && loots.contains(loot.getName());


        Log.info("\n\n\n" + "~~~~~~~~Welcome to Wilderness Looter~~~~~~~~" +"\n\n\n");
        super.onStart();
    }

    @Override
    public int loop() {
        returnTime = ThreadLocalRandom.current().nextInt(200,455);

        if(loggedInAndShouldSwitch()){
            Log.info("Switching World");
            WorldHopper.hopTo(319);
        }
        else if(isLoggedIn()){
            if(lumbridge.contains(Players.getLocal().getPosition()) || haveDied){
                haveDied=true;
                enableRun();

                if(isDeathInList()){
                    dyingSpotsList.clear();
                }
                else if(Players.getLocal().getPosition().getY()>=3487){
                    haveDied=false;
                }
                else{
                    Movement.walkTo(new Position(3089,3488,0));
                }

            }
            else if(shouldBank()){
                if(isDeathInList()){
                    dyingSpotsList.clear();
                }
                else if(shouldCrossDitch()){
                    crossDitchToBank();
                }
                else if(CombatHandling.canAndShouldEat(60)){
                    CombatHandling.eatFood();
                }
                else{
                    BankHandling.walkAndDepositAllAndWithdraw(energyPredicate, nPotionsToWithdraw());
                }
            }
            else if(!playerInLootArea()){
                if(shouldDrinkEnergyPot()){
                    drinkEnergyPotion();
                }
                else if(shouldDropVials()){
                    dropEmptyVials();
                }
                else if(shouldRun()){
                    enableRun();
                }
                else{
                    dyingSpotsList.clear();
                    walkToLootArea();
                }
            }
            else if(playerInLootArea()){
                if(isUnderAttack()){
                    if(CombatHandling.canAndShouldEat(50)){
                        CombatHandling.eatFood();
                    }
                    else{
                        Log.info("Under Player attack");
                        runToSafety();
                    }

                }
                else if(shouldRun()){
                    enableRun();
                }
                else{

                    if(playerIsDyingAndNotOnList()){
                        savePositionToList(getDyingPlayer().getPosition());
                    }

                    else if(shouldLoot()){
                        itemToLootBasedOnWalking().interact("Take");
                    }

                    // This part deals with what happends when we stand at the Location where player died
                    else if(!dyingSpotsList.isEmpty() && standingAtDeathPosition()){


                        if(!timerStarted && lootSpawningSoon()){
                            startBackupTimer();
                        }

                        if(bonesAtDeathSpot() && lootShouldHaveSpawned() && !lootHaveSpawned){
                            lootHaveSpawned = true;
                        }

                        if(getLootUnderPlayer()!=null){
                            getLootUnderPlayer().interact("Take");
                        }

                        else if(backupTime<=0 || (lootHaveSpawned)){
                            removeDeathInstance();
                        }
                        updateBackupTimer(returnTime);
                    }


                    // This part deals with walking to the death position
                    else if(isDeathInList() && !standingAtDeathPosition()){
                        //Log.info("Walking to "+ dyingSpotsList.getFirst().getDeathPosition());
                        //Log.info("Loot should spawn in " + dyingSpotsList.getFirst().getDeathTime() + " ms");
                        resetBackupTimerAndLoot();
                        clearBadSpots();
                        if(!Movement.isRunEnabled() && Movement.getRunEnergy()>=1 && firstDeathTime()<=2500){
                            enableRun();
                        }
                        if(isDeathInList() && !Players.getLocal().isMoving()){
                            walkToDeathPos();
                        }

                    }
                    if(isDeathInList()){
                        updateTimer(returnTime);
                        if(!standingAtDeathPosition() && haveTarget()){
                            abandonTarget();
                        }
                        clearMissedDeathPositions();
                    }

                    else{
                        lootWithoutRestrictions();
                    }

                }

            }
        }

        return returnTime;
    }

    // This part is for Calculating if we should run to Loot


    public boolean shouldLoot(){
        return isDeathInList() && !Players.getLocal().isMoving() &&
                itemToLootBasedOnWalking()!=null
                && haveTimeToWalk(firstDeathTime(), Players.getLocal().getPosition(), firstDeathPos());
    }

    public Pickable itemToLootBasedOnWalking(){
        Predicate<Pickable> predicate = item -> item.getPosition().distance(firstDeathPos())<=8;
        return Pickables.getNearest(generalLootPredicate.and(predicate));
    }


    //                      Here the part with regards to Calculating the Looting Ends.

    public void clearBadSpots(){
        if(isDeathInList()){
            if(lootShouldHaveSpawned() && bonesAtDeathSpot() && !otherGoodLoot(dyingSpotsList.getFirst().getDeathPosition())){
                dyingSpotsList.removeFirst();
            }
        }
    }


    public boolean otherGoodLoot(Position position){
        return Arrays.stream(Pickables.getAt(position)).anyMatch(pickable -> loots.contains(pickable.getName()));
    }



    public static long firstDeathTime(){
        return dyingSpotsList.getFirst().getDeathTime();
    }

    public static Position firstDeathPos(){
        return dyingSpotsList.getFirst().getDeathPosition();
    }


    public void lootWithoutRestrictions(){
        if(!Players.getLocal().isMoving()){
            Pickable loot = Pickables.getNearest(generalLootPredicate);
            if(loot!=null){
                loot.interact("Take");
            }
        }
    }


    public boolean isUnderAttack(){
        return Players.getLocal().isHealthBarVisible();
    }

    public void runToSafety() {
        int xCoordinate = Players.getLocal().getPosition().getX();
        Movement.toggleRun(true);
        Movement.walkTo(new Position(xCoordinate, 3523));
        int sleepTime = ThreadLocalRandom.current().nextInt(6500,8800);
        Log.info("Trying to sleep for " + sleepTime+ "ms");
        Time.sleep(sleepTime);
    }



    public boolean shouldBank(){
        //boolean keepOpen = Bank.isOpen() && Bank.contains(energyPredicate) && !Inventory.contains(energyPredicate);
        return Inventory.getItems().length >27 || Combat.isPoisoned() ||
                Bank.isOpen() && Bank.contains(energyPredicate) && !Inventory.contains(energyPredicate);
    }

    public boolean shouldCrossDitch(){
        return Players.getLocal().getY()>3523;
    }

    public void crossDitchToBank(){
        SceneObject ditch = SceneObjects.getNearest("Wilderness Ditch");
        if(ditch!=null && Players.getLocal().getPosition().getY()>=3523){
            ditch.interact("Cross");
        }
        else{
            Movement.walkTo(new Position(Players.getLocal().getX(),3523));
        }
    }

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
            }else{
                Log.info("Trying to walk back to the middle of wildy");
                Movement.walkTo(new Position(3090, 3533));
            }
        }
    }


    public static Player getDyingPlayer(){
        //Used for finding dying players
        Player dyingPlayer = Players.getNearest(player -> player.getAnimation() == deathAnimation);
        if(dyingPlayer!=null){
            return dyingPlayer;
        }
        else{
            return null;
        }
    }

    public static boolean listContainsPosition(Position position){
        return dyingSpotsList.stream().anyMatch(spot -> spot.getDeathPosition().equals(position));
    }

    public static void savePositionToList(Position deathPosition){
        //Log.info("Saving a "+ deathPosition + " to List of deathSpots");
        long deathTime = 59000;
        dyingSpotsList.add(new DyingSpot(deathPosition, deathTime));
    }

    public static void updateTimer(long reduction){
        // Used for updating the deathTimer of each instance
        for(DyingSpot d : dyingSpotsList){
            long deathtime = d.getDeathTime();
            d.setDeathTime(deathtime - reduction);
        }
    }

    public static boolean bonesAtDeathSpot(){
        Pickable[] list = Pickables.getAt(dyingSpotsList.getFirst().getDeathPosition());
        return Arrays.stream(list).anyMatch(p -> "Bones".equals(p.getName())) && lootSpawningSoon();
    }

    public static boolean isDeathInList(){
        return !dyingSpotsList.isEmpty();
    }

    public static boolean lootSpawningSoon(){
        return dyingSpotsList.getFirst().getDeathTime() <=4500;
    }

    public boolean lootShouldHaveSpawned(){
        return dyingSpotsList.getFirst().getDeathTime()<=-1000;
    }

    public static void walkToDeathPos(){
        Movement.walkTo(dyingSpotsList.getFirst().getDeathPosition());
    }

    public static boolean standingAtDeathPosition(){
        return Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition());
    }

    public static void startBackupTimer(){
        backupTime = 8500;
        timerStarted = true;
    }

    public static void updateBackupTimer(long reduction){
        backupTime -= reduction;
    }

    public static void resetBackupTimerAndLoot(){
        backupTime = System.currentTimeMillis();
        lootHaveSpawned = false;
        timerStarted=false;
    }

    public static Pickable getLootUnderPlayer(){
        Pickable[] lootUnderPlayer = shuffleLootList(Pickables.getAt(Players.getLocal().getPosition()));
        if(lootUnderPlayer!=null){
            List<String> stringList = Arrays.stream(lootUnderPlayer).map(pickable -> pickable.getName()).collect(Collectors.toList());
            stringList.retainAll(loots);

            if(stringList.size()>0){
                return Pickables.getNearest(stringList.get(0));
            }
        }
        return null;
    }

    public static void removeDeathInstance(){
        //Log.info("Removing " + dyingSpotsList.getFirst().getDeathPosition() + " from list");
        dyingSpotsList.removeFirst();
    }

    public static void clearMissedDeathPositions(){
        List<DyingSpot> spotsToRemove = new ArrayList<>();
        for(DyingSpot d : dyingSpotsList){
            if(d.getDeathTime()<=-14500){
                Log.info("Removing " + d.getDeathPosition() +" from List" + "It's current DeahTime is " + d.getDeathTime());
                spotsToRemove.add(d);
            }
        }
        dyingSpotsList.removeAll(spotsToRemove);
    }

    public static boolean playerIsDyingAndNotOnList(){
        return getDyingPlayer()!=null && !listContainsPosition(getDyingPlayer().getPosition());
    }
}
