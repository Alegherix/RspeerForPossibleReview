import Utility.BankHandling;
import Utility.CombatHandling;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;


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

/*
    private static String[] p2pLoots = {"Shark","Tuna potato","Anglerfish", "Dark crab", "Sea turtle", "Cooked karambwan","Manta ray", "Pineapple pizza", "Mushroom potato",
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
            "Rune crossbow"};
*/

    //private final static String[] f2pLoots = {"Adamant arrow", "Lobster", "Swordfish", "Anchovy pizza", "Green d'hide body", "Green d'hide chaps", "Green d'hide vamb"};
    private static Area WILDY_LOOT_AREA;

    @Override
    public void onStart() {
        WILDY_LOOT_AREA = Area.polygonal(
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
                "Rune crossbow","Monkfish","Dark bow", "Heavy ballista", "Light ballista", "Amulet of strength", "Dragon boots");
        lootHaveSpawned = false;
        timerStarted = false;
        energyPredicate = item -> item.getName().startsWith("Energy");
        super.onStart();
    }

    //Todo - Spring till platsen att loota på straight away
    //Todo - Fixa så han dricker energy potion i Banken
    //Todo - Splitta upp WalkToLootArea till 2 metoder

    @Override
    public int loop() {
        if(shouldBank()){
            if(shouldCrossDitch()){
                crossDitchToBank();
            }
            else{
                BankHandling.walkAndDepositAll();
            }
        }
        else if(!playerInLootArea()){
            walkToLootArea();
        }
        else if(playerInLootArea()){
            if(isUnderAttack()){
                if(CombatHandling.canAndShouldEat(50)){
                    CombatHandling.eatFood();
                }
                else if(underNpcAttack()){
                    Log.info("Under NPC Attack");
                    walkRandomly();
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
            }
        }
        return returnTime;
    }

    public boolean shouldDrinkEnergyPotion(){
        return Movement.getRunEnergy()<=90;
    }

    public boolean canDrinkEnergyPotion(){
        return Inventory.contains(item -> item.getName().startsWith("Energy"));
    }

    public void drinkEnergyPotion(){
        Inventory.getFirst(item-> item.getName().startsWith("Energy")).interact("Drink");
    }

    public boolean shouldRun(){
        return Movement.getRunEnergy()>=30 && !Movement.isRunEnabled();
    }

    public void enableRun(){
        Movement.toggleRun(true);
    }


    public void startLooting(){
        if(!Players.getLocal().isMoving()){
            Predicate<Pickable> lootPred = loot -> (loots).contains(loot.getName()) && loot.isPositionWalkable();
            Pickable item = Pickables.getNearest(lootPred);

            if(item!=null){
                item.interact("Take");
            }
        }
    }


    public boolean underNpcAttack(){
        List<String> npcNames = Arrays.asList("Skeleton","Scorpion");
        Npc attackingNpc = Arrays.stream(Npcs.getLoaded())
                .filter(npc -> npc.getPosition().distance(Players.getLocal())<=1)
                .filter(npc -> npcNames.contains(npc.getName()))
                .findFirst().orElse(null);
        return attackingNpc!= null;
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


    public void walkRandomly(){
        int diffX = ThreadLocalRandom.current().nextInt(-25,25);
        int diffY = ThreadLocalRandom.current().nextInt(-25,25);
        Movement.walkTo(new Position(Players.getLocal().getX()+diffX, Players.getLocal().getY()+diffY));
    }


    public boolean shouldBank(){
        return Inventory.getItems().length >27 || Combat.isPoisoned();
    }

    public boolean shouldCrossDitch(){
        return Players.getLocal().getY()>3523;
    }
    public void crossDitchToBank(){
        SceneObject ditch = SceneObjects.getNearest("Wilderness Ditch");
        if(ditch!=null && Players.getLocal().getPosition().getY()>=3523){
            ditch.interact("Cross");
        } else{
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
        else if(ditch!=null){
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

}
