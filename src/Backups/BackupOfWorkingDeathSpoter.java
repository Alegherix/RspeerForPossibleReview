package Backups;

import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class BackupOfWorkingDeathSpoter extends Script {
    private static int deathAnimation;
    private static LinkedList<DyingSpot> dyingSpotsList;
    private int returnTime;
    private List<String> loots;
    private boolean lootHaveSpawned;
    private long backupTime;
    private boolean timerStarted;

    @Override
    public void onStart() {
        dyingSpotsList = new LinkedList<>();
        deathAnimation = 836;
        returnTime = 400;
        //loots = Arrays.asList("Lobster","Swordfish","Adamant arrow","Salmon");
        loots = Arrays.asList("Shark","Tuna potato","Anglerfish", "Dark crab", "Sea turtle", "Cooked karambwan","Manta ray", "Pineapple pizza", "Mushroom potato",
                "Amethyst arrow", "Dragon arrow", "Onyx bolts (e)", "Dragonstone bolts (e)", "Dragonstone dragon bolts (e)", "Diamond dragon bolts (e)", "Amethyst broad bolts",
                "Abyssal whip", "Abyssal dagger", "Scythe of vitur (uncharged)", "Ghrazi rapier", "Elder maul", "Saradomin sword", "Armadyl godsword", "Bandos godsword", "Saradomin godsword", "Zamorak godsword",
                "3rd age longsword", "Granite maul", "Tzhaar-ket-om", "Dragon crossbow", "Dragon thrownaxe", "Dragon javelin", "Dragon dart", "Dragon dart(p++)", "Dragon dart(p+)", "Dragon dart(p)",
                "Dragon 2h sword", "Dragon claws", "Dragon scimitar", "Dragon sword", "Dragon dagger(p++)", "Mystic hat (dark)", "Mystic hat (light)",
                "Dharok's platelegs 0", "Dharok's platebody 0", "Dharok's helm 0", "Dharok's greataxe 0", "Ahrim's robetop 0", "Ahrim's robeskirt 0", "Ahrim's hood 0", "Ahrim's staff 0",
                "Torag's platelegs 0", "Torag's platebody 0", "Torag's helm 0", "Guthan's warspear 0", "Guthan's platebody 0", "Guthan's helm 0", "Guthan's chainskirt 0",
                "Verac's plateskirt 0", "Verac's helm 0", "Verac's flail 0", "Verac's brassard 0", "Mystic robe bottom (dark)", "Mystic robe bottom (light)", "Mystic robe top", "Mystic mud staff",
                "Mystic robe bottom", "Mystic robe top (dark)", "Mystic robe top (light)", "Mystic smoke staff", "Infinity boots", "Infinity bottoms", "Infinity gloves", "Infinity hat", "Infinity top",
                "Elysian spirit shield", "Arcane spirit shield", "Ancestral robe top", "Kodai wand", "Ancestral robe bottom", "3rd age platebody", "3rd age platelegs", "Spectral spirit shield",
                "Dragon full helm", "Dragon warhammer", "Armadyl chestplate", "Ranger boots", "Pegasian boots", "Armadyl chainskirt", "Armadyl crossbow", "3rd age range top", "Bandos tassets",
                "Abyssal bludgeon", "Primordial boots", "Dragon platebody", "Bandos chestplate", "Ancestral hat", "Necklace of anguish", "Amulet of torture", "Ring of suffering", "Tormented bracelet",
                "Rangers' tunic", "Dragon crossbow", "Super combat potion(4)", "Super combat potion(3)", "Super combat potion(2)", "Super combat potion(1)", "Super restore(4)", "Super restore(3)",
                "Super restore(2)", "Super restore(1)", "Saradomin brew(4)", "Saradomin brew(3)", "Saradomin brew(2)", "Saradomin brew(1)", "Death rune", "Ancient staff", "Toxic staff (uncharged)",
                "Staff of the dead", "Staff of light", "Ranging potion(4)", "Ranging potion(3)", "Super strength(4)", "Super strength(3)", "Super strength(2)", "Super strength(1)",
                "Amulet of glory", "Amulet of glory(4)", "Helm of neitiznot", "Berserker helm", "Berserker ring", "Seers' ring", "Archers' ring", "Warrior ring","Magic shortbow",
                "Obsidian cape", "Obsidian helm", "Obsidian platebody", "Obsidian platelegs", "Toktz-ket-xil", "Black d'hide body", "Amulet of fury", "Black d'hide chaps", "Black d'hide vamb",
                "Rune Platebody", "Karil's leathertop 0", "Karil's leatherskirt 0", "Karil's crossbow 0", "Elder chaos hood", "Elder chaos robe", "Elder chaos top", "Rune plateskirt", "Gilded plateskirt",
                "Rune platelegs", "Gilded platelegs", "Gilded boots", "Rune knife", "Rune knife(p++)", "Rune knife(p+)", "Rune knife(p)", "Rune dart(p++)", "Rune dart(p+)", "Rune dart(p)", "Rune dart",
                "Prayer potion(4)", "Prayer potion(3)", "Prayer potion(2)", "Prayer potion(1)", "Ring of recoil", "Bastion potion(4)", "Bastion potion(3)", "Bastion potion(2)","Bastion potion(1)",
                "Rune crossbow");
        lootHaveSpawned = false;
        timerStarted = false;

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
        Log.info("Saving a "+ deathPosition +" to List of deathSpots");
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

    public boolean bonesAtDeathSpot(){
        return Pickables.getNearest(pickable -> "Bones".equals(pickable.getName()) && pickable.getPosition().equals(Players.getLocal().getPosition()))!=null;
    }

    public boolean isDeathInList(){
        return !dyingSpotsList.isEmpty();
    }

    public boolean lootSpawningSoon(){
        return dyingSpotsList.getFirst().getDeathTime() <=4500;
    }

    public void walkToSpot(){
        Movement.walkTo(dyingSpotsList.getFirst().getDeathPosition());
    }

    public boolean standingAtDeathPosition(){
        return Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition());
    }

    public void startBackupTimer(){
        backupTime = 5000;
        timerStarted=true;
    }

    public void updateBackupTimer(long reduction){
        backupTime -= reduction;
    }

    public static Pickable[] shuffleLootList(Pickable[] lootUnderPlayer){
        if (lootUnderPlayer.length>0){
            for (int i = 0; i < lootUnderPlayer.length; i++) {
                int randomPos = ThreadLocalRandom.current().nextInt(0, lootUnderPlayer.length);

                Pickable temp = lootUnderPlayer[i];
                lootUnderPlayer[i] = lootUnderPlayer[randomPos];
                lootUnderPlayer[randomPos] = temp;
            }
            return lootUnderPlayer;
        }
        else{
            return null;
        }
    }

    public Pickable getLootUnderPlayer(){
        Pickable[] lootUnderPlayer = shuffleLootList(Pickables.getAt(Players.getLocal().getPosition()));
        Arrays.asList(lootUnderPlayer).retainAll(loots);
        if(lootUnderPlayer!=null){
            return lootUnderPlayer[0];
        }
        else{
            return null;
        }
        //return Pickables.getNearest(item -> loots.contains(item.getName()) && item.getPosition().distance(Players.getLocal().getPosition())<1);
    }

    public void removeDeathInstance(){
        Log.info("Removing " + dyingSpotsList.getFirst().getDeathPosition() + " from list");
        dyingSpotsList.removeFirst();
    }

    public void clearMissedDeathPositions(){
        List<DyingSpot> spotsToRemove = new ArrayList<>();
        for(DyingSpot d : dyingSpotsList){
            if(d.getDeathTime()<=500){
                Log.info("Removing " + d.getDeathPosition() +" from List" + "It's current DeahTime is " + d.getDeathTime());
                spotsToRemove.add(d);
            }
        }
        dyingSpotsList.removeAll(spotsToRemove);
    }

    public boolean playerIsDyingAndNotOnList(){
        return getDyingPlayer()!=null && !listContainsPosition(getDyingPlayer().getPosition());
    }

    public void savePositionToListIfShould(){
        if(playerIsDyingAndNotOnList()){
            savePositionToList(getDyingPlayer().getPosition());
        }
    }

    public void handleLooting(){
        if(!dyingSpotsList.isEmpty() && standingAtDeathPosition() && lootSpawningSoon()){

            if(!timerStarted){
                Log.info("Starting Timer");
                startBackupTimer();
            }

            if(bonesAtDeathSpot() && !lootHaveSpawned){
                Log.info("Bones at deathspot so loot have spawned");
                lootHaveSpawned = true;
            }

            else if(getLootUnderPlayer()!=null){
                Log.info("Trying to loot " + getLootUnderPlayer().getName());
                getLootUnderPlayer().interact("Take");
            }

            else if(backupTime<=0 || (lootHaveSpawned)){
                Log.info("BackupTime <=0 || loot Have spawned and doesn't exist anymore");
                removeDeathInstance();
                if(isDeathInList()){
                    clearMissedDeathPositions();
                }

                timerStarted=false;
            }

            updateBackupTimer(returnTime);
        }
    }

    public void handleWalking(){
        if(isDeathInList() && lootSpawningSoon() && !Players.getLocal().isMoving() && !standingAtDeathPosition()){
            Log.info("Walking to "+ dyingSpotsList.getFirst().getDeathPosition());
            walkToSpot();
        }
    }

    @Override
    public int loop() {

        /*

        if(!dyingSpotsList.isEmpty() && standingAtDeathPosition() && lootSpawningSoon()){

            if(!timerStarted){
                Log.info("Starting Timer");
                startBackupTimer();
            }

            if(bonesAtDeathSpot() && !lootHaveSpawned){
                Log.info("Bones at deathspot so loot have spawned");
                lootHaveSpawned = true;
            }

            else if(getLootUnderPlayer()!=null){
                Log.info("Trying to loot " + getLootUnderPlayer().getName());
                getLootUnderPlayer().interact("Take");
            }

            else if(backupTime<=0 || (lootHaveSpawned)){
                Log.info("BackupTime <=0 || loot Have spawned and doesn't exist anymore");
                removeDeathInstance();
                if(isDeathInList()){
                    clearMissedDeathPositions();
                }

                timerStarted=false;
            }

            updateBackupTimer(returnTime);
        }

        else if(isDeathInList() && lootSpawningSoon() && !Players.getLocal().isMoving() && !standingAtDeathPosition()){
            Log.info("Walking to "+ dyingSpotsList.getFirst().getDeathPosition());
            walkToDeathPos();
        }
        */
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