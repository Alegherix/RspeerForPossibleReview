import Utility.RandomHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;

import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static Utility.InterfaceHandling.*;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    private static LinkedList<DyingSpot> dyingSpotsList;
    public static final int msPerSquareWalking = 950;
    private final int deathAnimation = 836;

    private final List<String> loots = Arrays.asList("Adamant arrow","Lobster","Swordfish", "Maple shortbow");
    private final Predicate<Pickable> itemPredicate = item -> loots.contains(item.getName()) && item.getX()>=3072;

    @Override
    public void onStart() {
        dyingSpotsList = new LinkedList<>();
    }

    @Override
    public int loop() {
        int returnTid = RandomHandling.randomReturn();
        // Lägg till i listan
        if(playerIsDyingAndNotOnList()){
            Log.info("Should add player do List");
            savePositionToList(getDyingPlayer().getPosition());
        }
        // OM loot under mig, Loota
        else if(goodLootUnderMe()!=null){
            Log.info("Loot under me");
            if(!dyingSpotsList.isEmpty() && Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition())){
                dyingSpotsList.removeFirst();
            }
            else{
                goodLootUnderMe().interact("Take");
            }
        }
        else if(bonesAtDeathPosition()){
            dyingSpotsList.removeFirst();
        }
        // Om vi listan inte är tom, och det inte finns loot under personen
        else if(!dyingSpotsList.isEmpty()){
            Log.info("Have death in list");
            Log.info(dyingSpotsList.getFirst().getDeathTime());
            // Om det finns loot att hämta och vi har tid, Loota
            if(itemToLoot()!= null && haveTimeToLoot(itemToLoot())){
                Log.info("Have time to loot before Spawn");
                if(!Players.getLocal().isMoving()){
                    itemToLoot().interact("Take");
                }
            }
            else{
                //Annars gå till korrekt position för att invänta looting
                if(!Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition())){
                    Log.info("Should move to DeathPosition");
                    Movement.setWalkFlag(dyingSpotsList.getFirst().getDeathPosition());
                }
            }
        }
        else{
            Log.info("Inne i else");
            //Allmän lootning vid tom Lista.
            if(itemToLoot()!=null && !Players.getLocal().isMoving()){
                Log.info("Vi har loot och rör mig inte");
                itemToLoot().interact("Take");
            }
        }

        if(!dyingSpotsList.isEmpty()){
            for(DyingSpot d : dyingSpotsList){
                d.setDeathTime(d.getDeathTime() - returnTid);
            }
        }

        return returnTid;
    }

    private boolean bonesAtDeathPosition() {
        return !dyingSpotsList.isEmpty() &&
                Arrays.stream(Pickables.getAt(dyingSpotsList.getFirst().getDeathPosition()))
                .anyMatch(item -> item.getName().equals("Bones"));
    }


    public Pickable itemToLoot(){
        return Pickables.getNearest(itemPredicate);
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

    public Player getDyingPlayer(){
        //Used for finding dying players
        Player dyingPlayer = Players.getNearest(player -> player.getAnimation() == deathAnimation);
        if(dyingPlayer!=null){
            return dyingPlayer;
        }
        else{
            return null;
        }
    }

    public  boolean playerIsDyingAndNotOnList(){
        return getDyingPlayer()!=null && !listContainsPosition(getDyingPlayer().getPosition());
    }

    public boolean listContainsPosition(Position position){
        return dyingSpotsList.stream().anyMatch(spot -> spot.getDeathPosition().equals(position));
    }

    public void savePositionToList(Position deathPosition){
        //Log.info("Saving a "+ deathPosition + " to List of deathSpots");
        long deathTime = 59000;
        dyingSpotsList.add(new DyingSpot(deathPosition, deathTime));
    }

    public boolean lootUnderMe(){
        return Pickables.getAt(Players.getLocal().getPosition()).length >= 1;
    }

    public Pickable goodLootUnderMe(){
        Pickable[] lootUnderMe = Pickables.getAt(Players.getLocal().getPosition());
        List<Pickable> itemsToLoot = Arrays.stream(lootUnderMe).filter(itemPredicate).collect(Collectors.toList());
        return (itemsToLoot.size()>0)? itemsToLoot.get(0) : null;
    }


























}







































