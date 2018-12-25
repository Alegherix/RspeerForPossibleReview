import Utility.*;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Definitions;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
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

import static Utility.PotionHandling.*;
import static Utility.RunningHandling.*;


@ScriptMeta(developer = "Martin", desc = "Wilderness Looter", name = "Wildy Looter")
public class WildernessLooter extends Script {
    private static int deathAnimation;
    private static LinkedList<DyingSpot> dyingSpotsList;
    private static int returnTime;
    private static List<String> loots;
    private static boolean lootHaveSpawned;
    private static long backupTime;
    private static boolean timerStarted;
    private static final int WORLD = 318;

    private Predicate<Item> energyPredicate;
    private Predicate<Pickable> generalLootPredicate;
    private Predicate<Item> emblemPred;
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

        //Initiate Variables
        deathAnimation = 836;
        lootHaveSpawned = false;
        timerStarted = false;
        haveDied=false;

        //Initiate Predicates
        emblemPred = emblem -> emblem.getName().startsWith("Mysterious");
        energyPredicate = item -> item.getName().startsWith("Energy");
        generalLootPredicate = loot ->
                loot.getPosition().getY()>=3522 && loot.getPosition().getY()<=3563 &&
                loot.getPosition().getX() >=3057 && loot.getPosition().getX()<=3107 && loots.contains(loot.getName());


        Log.info("\n\n\n" + "~~~~~~~~Welcome to Wilderness Looter~~~~~~~~" +"\n\n\n");
        super.onStart();
    }


    // TODO - Bättre Check på DeathPosition
    // TODO - Smartare Walking
    // TODO - Loota påväg till mitt mål

    // DE JAG ÄNDRAT PÅ 22/12
    // NÄR MAN KAN LOOTA


    @Override
    public int loop() {
        returnTime = ThreadLocalRandom.current().nextInt(200,455);

        if(Game.isLoggedIn() && Worlds.getCurrent()!=WORLD){
            Log.info("Switching World");
            WorldHopper.hopTo(WORLD);
        }
        else if(Game.isLoggedIn()){
            if(lumbridge.contains(Players.getLocal().getPosition()) || haveDied){
                walkBackFromLumbridge();
            }
            else if(shouldBank()){
                walkToBank();
            }
            else if(!playerInLootArea()){
                walkToLootAreaWithConditions();
            }
            else if(playerInLootArea()){
                if(!preLootHandling()){

                     if(playerIsDyingAndNotOnList() && shouldSavePlayerToList(getDyingPlayer())){
                             savePositionToList(getDyingPlayer().getPosition());
                     }

                     else if(itemToLoot()!=null && haveTimeToLoot(itemToLoot())){
                         if(Movement.getDestination()==null ||
                                 !itemToLoot().getPosition().equals(Movement.getDestination())){
                             itemToLoot().interact("Take");
                         }
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
                         resetBackupTimerAndLoot();
                         clearBadSpots();
                         if(!Movement.isRunEnabled() && Movement.getRunEnergy()>=1 && firstDeathTime()<=2500){
                             enableRun();
                         }
                         // Ändra så att om Inte walkflaggen är på utsatt position så gå dit
                         if(isDeathInList()){
                             if(Movement.getDestination()==null ||
                                     !Movement.getDestination().equals(dyingSpotsList.getFirst().getDeathPosition())){
                                 walkToDeathPos();
                             }
                             else if(Players.getLocal().isMoving()){
                                 clearBadSpots();
                             }
                         }
                     }
                     if(isDeathInList()){
                         int extra = 0;
                         if(!standingAtDeathPosition() && haveTarget() || (standingAtDeathPosition() && dyingSpotsList.getFirst().getDeathTime()>2500 && haveTarget())){
                             extra = abandonTarget();

                         }
                         updateTimer(returnTime + extra);
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


    public List<String> getPlayerEquipment(Player p) {
        List<String> equipmentList = new LinkedList<String>();
        if (p != null) {
            int[] equipment = p.getAppearance().getEquipmentIds();
            for (int i = 0; i < equipment.length; i++) {
                if (equipment[i] - 512 > 0) {
                    equipmentList.add(Definitions.getItem(equipment[i] - 512).getName());
                }
            }
        }
        return equipmentList;
    }

    public boolean shouldSavePlayerToList(Player p){
        return getPlayerEquipment(p).size()>=3;
    }

    public long timeTakenToLoot(Pickable itemToLoot){
        double myDistanceToItem = itemToLoot.distance(Players.getLocal().getPosition());
        double distanceFromLootToDeath = itemToLoot.distance(dyingSpotsList.getFirst().getDeathPosition());
        double totalDistance = myDistanceToItem + distanceFromLootToDeath;
        long timeTaken = (long)totalDistance * msPerSquareWalking;
        return timeTaken;
    }

    public boolean haveTimeToLoot(Pickable itemToLoot){
        // Kanske tänka på delay i samband med msPerSquareWalking som en statisk faktor
        return !dyingSpotsList.isEmpty() &&
                dyingSpotsList.getFirst().getDeathTime() > timeTakenToLoot(itemToLoot);
    }

    public Pickable itemToLoot(){
        return Pickables.getNearest(generalLootPredicate);
    }

    // This part is for Calculating if we should run to Loot



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
    }


    public boolean shouldBank(){
        return Inventory.getItems().length >27 || Combat.isPoisoned() ||
                (Bank.isOpen() && Bank.contains(energyPredicate) && !Inventory.contains(energyPredicate)) ||
                Inventory.getCount(emblemPred)>1;
    }


    boolean playerInLootArea(){
        return WILDY_LOOT_AREA.contains(Players.getLocal().getPosition());
    }

    public void walkToLootArea(){
        SceneObject ditch = SceneObjects.getNearest("Wilderness Ditch");
        InterfaceComponent wildyInterface = Interfaces.getComponent(475,11);

        if (wildyInterface!=null && Players.getLocal().getY()<=3521) {
            wildyInterface.interact("Enter Wilderness");
            RandomHandling.randomSleep();
        }
        else if(ditch!=null && Players.getLocal().getPosition().getX()<=3110 && Players.getLocal().getY()<=3521){
            ditch.interact("Cross");
            RandomHandling.randomSleep();
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

    public boolean shouldWaitOutAttacker(){
        return Players.getLocal().getY()<=3224 && haveTarget();
    }

    public void abandonAttacker(){
        Log.info("Sleeping then abandoning attacker");
        Time.sleep(RandomHandling.randomNumber(15000,16000));
        abandonTarget();


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

    public Pickable emblemExist(){
        return Pickables.getNearest(item -> item.getName().contains("emblem"));
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
        return dyingSpotsList.getFirst().getDeathTime()<=0;
    }

    public static void walkToDeathPos(){
        //Uppdaterat och börjat använda setWalkFlag
        Movement.setWalkFlag(dyingSpotsList.getFirst().getDeathPosition());
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

    public Pickable getLootUnderPlayer(){
        List<Pickable> listOfLoots = Arrays.asList(Pickables.getAt(Players.getLocal().getPosition()));
        listOfLoots = listOfLoots.stream().filter(item -> loots.contains(item.getName())).collect(Collectors.toList());
        Pickable loot = null;

        if(listOfLoots.size()>0){
            int nr = new Random(System.currentTimeMillis()).nextInt(listOfLoots.size());
            loot = listOfLoots.get(nr);
        }
        return loot;
    }



    public static void removeDeathInstance(){
        //Log.info("Removing " + dyingSpotsList.getFirst().getDeathPosition() + " from list");
        dyingSpotsList.removeFirst();
    }

    public static void clearMissedDeathPositions(){
        List<DyingSpot> spotsToRemove = new ArrayList<>();
        for(DyingSpot d : dyingSpotsList){
            if(d.getDeathTime()<=-14500){
                //Log.info("Removing " + d.getDeathPosition() +" from List" + "It's current DeahTime is " + d.getDeathTime());
                spotsToRemove.add(d);
            }
        }
        dyingSpotsList.removeAll(spotsToRemove);
    }

    public static boolean playerIsDyingAndNotOnList(){
        return getDyingPlayer()!=null && !listContainsPosition(getDyingPlayer().getPosition());
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

    //This deals with the interface stuff

    public static boolean haveTarget(){
        try {
            return Interfaces.getComponent(90, 47).getText() != null
                    && !"None".equals(Interfaces.getComponent(90, 47).getText())
                    && !"<col=ff0000>---</col>".equals(Interfaces.getComponent(90, 47).getText());
        }
        catch (NullPointerException e){
            System.out.println("No target");
        }
        return false;
    }

    public static int abandonTarget(){
        int number = RandomHandling.randomNumber(980,1050);
        if(Dialog.isOpen()){
            Dialog.process(0);
        }
        else{
            Interfaces.getComponent(90,50).interact("Abandon target");
            Time.sleep(number);
            return number;
        }
        return 0;
    }

    // Main script logic reduced
    public void walkToLootAreaWithConditions(){
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

    //
    public void walkToBank(){
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

    public boolean preLootHandling() {
        if (emblemExist() != null) {
            if (!Players.getLocal().isMoving()) {
                emblemExist().interact("Take");
                return true;
            }
        }
        else if (shouldWaitOutAttacker()) {
            Log.info("Should wait out attacker");
            abandonAttacker();
            return true;
        }
        else if (isUnderAttack()) {
            if (CombatHandling.canAndShouldEat(50)) {
                CombatHandling.eatFood();
                return true;
            }
            else {
                Log.info("Under Player attack");
                runToSafety();
                return true;
            }
        }
        else if(shouldRun()){
            enableRun();
            return true;
        }
        return false;
    }

    public void walkBackFromLumbridge(){
        haveDied=true;
        if(RunningHandling.shouldRun()){
            RunningHandling.enableRun();
        }
        else if(isDeathInList()){
            dyingSpotsList.clear();
        }
        else if(Players.getLocal().getPosition().getY()>=3487){
            haveDied=false;
        }
        else{
            Movement.walkTo(new Position(3089,3488,0));
        }
    }
}
