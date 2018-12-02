import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ScriptMeta(developer = "Slazter", desc = "DeathSpotLooter", name = "DeathSpot looter")

public class DeathSpot extends Script {
    private static int deathAnimation;
    private static LinkedList<DyingSpot> dyingSpotsList;
    private static int returnTime;
    private static List<String> loots;
    private static boolean lootHaveSpawned;
    private static long backupTime;
    private static boolean timerStarted;

    @Override
    public void onStart() {
        dyingSpotsList = new LinkedList<>();
        deathAnimation = 836;
        returnTime = 400;
        loots = Arrays.asList("Lobster","Swordfish","Adamant arrow","Salmon");
        lootHaveSpawned = false;
        timerStarted = false;

        Log.info("\n\n\n\n\n\n");
        super.onStart();
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
        //Used for saving their position, and current time of their death
        //Log.info("Saving a "+ deathPosition +" to List of deathSpots");
        long deathTime = 61500;
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
        return Arrays.stream(list).anyMatch(p->"Bones".equals(p.getName())) && lootSpawningSoon();
    }

    public static boolean isDeathInList(){
        return !dyingSpotsList.isEmpty();
    }

    public static boolean lootSpawningSoon(){
        return dyingSpotsList.getFirst().getDeathTime() <=3500;
    }

    public static void walkToSpot(){
        Movement.walkTo(dyingSpotsList.getFirst().getDeathPosition());
    }

    public static boolean standingAtDeathPosition(){
        return Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition());
    }

    public static void startBackupTimer(){
        backupTime = 8500;
        timerStarted=true;
    }

    public static void updateBackupTimer(long reduction){
        backupTime -= reduction;
    }

    public static void resetBackupTimerAndLoot(){
        backupTime = System.currentTimeMillis();
        lootHaveSpawned = false;
    }

    public static Pickable getLootUnderPlayer(){
        return Pickables.getNearest(item -> loots.contains(item.getName()) && item.getPosition().distance(Players.getLocal().getPosition())<1);
    }

    public static void removeDeathInstance(){
        Log.info("Removing " + dyingSpotsList.getFirst().getDeathPosition() + " from list");
        dyingSpotsList.removeFirst();
    }

    public static void clearMissedDeathPositions(){
        List<DyingSpot> spotsToRemove = new ArrayList<>();
        for(DyingSpot d : dyingSpotsList){
            if(d.getDeathTime()<=500){
                Log.info("Removing " + d.getDeathPosition() +" from List" + "It's current DeahTime is " + d.getDeathTime());
                spotsToRemove.add(d);
            }
        }
        dyingSpotsList.removeAll(spotsToRemove);
    }

    public static boolean playerIsDyingAndNotOnList(){
        return getDyingPlayer()!=null && !listContainsPosition(getDyingPlayer().getPosition());
    }

    public void handleDeathSpotLooting(){
        if(playerIsDyingAndNotOnList()){
            savePositionToList(getDyingPlayer().getPosition());
        }

        else if(!dyingSpotsList.isEmpty() && standingAtDeathPosition()){

            if(!timerStarted && lootSpawningSoon()){
                Log.info("Starting Timer");
                startBackupTimer();
            }

            else if(bonesAtDeathSpot() && !lootHaveSpawned){
                Log.info("Bones at Deathspot so loot have spawned");
                lootHaveSpawned = true;
            }

            else if(getLootUnderPlayer()!=null){
                Log.info("Trying to loot " + getLootUnderPlayer().getName());
                getLootUnderPlayer().interact("Take");
            }

            else if(backupTime<=0 || (lootHaveSpawned)){
                Log.info("BackupTime <=0 or loot have spawned and doesn't exist anymore");
                removeDeathInstance();

                if(isDeathInList()){
                    clearMissedDeathPositions();
                }
            }
            updateBackupTimer(returnTime);
        }

        else if(isDeathInList() && !Players.getLocal().isMoving() && !standingAtDeathPosition()){
            Log.info("Walking to "+ dyingSpotsList.getFirst().getDeathPosition());
            resetBackupTimerAndLoot();
            walkToSpot();
        }

        if(isDeathInList()){
            updateTimer(returnTime);
        }
    }


    @Override
    public int loop() {
        if(playerIsDyingAndNotOnList()){
            savePositionToList(getDyingPlayer().getPosition());
        }

        else if(!dyingSpotsList.isEmpty() && standingAtDeathPosition()){

            if(!timerStarted && lootSpawningSoon()){
                Log.info("Starting Timer");
                startBackupTimer();
            }

            else if(bonesAtDeathSpot() && !lootHaveSpawned){
                Log.info("Bones at Deathspot so loot have spawned");
                lootHaveSpawned = true;
            }

            else if(getLootUnderPlayer()!=null){
                Log.info("Trying to loot " + getLootUnderPlayer().getName());
                getLootUnderPlayer().interact("Take");
            }

            else if(backupTime<=0 || (lootHaveSpawned)){
                Log.info("BackupTime <=0 or loot have spawned and doesn't exist anymore");
                removeDeathInstance();

                if(isDeathInList()){
                    clearMissedDeathPositions();
                }
            }
            updateBackupTimer(returnTime);
        }

        else if(isDeathInList() && !Players.getLocal().isMoving() && !standingAtDeathPosition()){
            Log.info("Walking to "+ dyingSpotsList.getFirst().getDeathPosition());
            resetBackupTimerAndLoot();
            walkToSpot();
        }

        if(isDeathInList()){
            updateTimer(returnTime);
        }

        return returnTime;
    }

}

class DyingSpot {
    private Position deathPosition;
    private long deathTime;

    public DyingSpot(Position deathPosition, long deathTime) {
        this.deathPosition = deathPosition;
        this.deathTime = deathTime;
    }

    public Position getDeathPosition() {
        return deathPosition;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public long getDeathTime() {
        return deathTime;
    }
}
