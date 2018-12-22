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

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static Utility.InterfaceHandling.*;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    private static LinkedList<DyingSpot> dyingSpotsList;
    public static final int msPerSquareWalking = 950;
    private final int deathAnimation = 836;

    private final List<String> loots = Arrays.asList("Adamant arrow","Lobster","Swordfish");
    private final Predicate<Pickable> itemPredicate = item -> loots.contains(item.getName());

    @Override
    public void onStart() {
        dyingSpotsList = new LinkedList<>();
    }

    @Override
    public int loop() {
        // Lägg till i listan
        if(playerIsDyingAndNotOnList()){
            savePositionToList(getDyingPlayer().getPosition());
        }
        // OM loot under mig, Loota
        else if(lootUnderMe()){
            if(!dyingSpotsList.isEmpty() && Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition())){
                dyingSpotsList.removeFirst();
            }
            else if(goodLootUnderMe()!=null){
                goodLootUnderMe().interact("Take");
            }

        }
        // Om vi listan inte är tom, och det inte finns loot under personen
        else if(!dyingSpotsList.isEmpty()){
            // Om det finns loot att hämta och vi har tid, Loota
            if(itemToLoot()!= null && haveTimeToLoot(itemToLoot())){
                if(!Players.getLocal().isMoving()){
                    itemToLoot().interact("Take");
                }
            }
            else{
                //Annars gå till korrekt position för att invänta looting
                if(!Players.getLocal().getPosition().equals(dyingSpotsList.getFirst().getDeathPosition())){
                    Movement.setWalkFlag(dyingSpotsList.getFirst().getDeathPosition());
                }
            }
        }
        else{
            //Allmän lootning vid tom Lista.
            if(itemToLoot()!=null && !Players.getLocal().isMoving()){
                itemToLoot().interact("Take");
            }
        }

        return RandomHandling.randomReturn();
    }



    public Pickable itemToLoot(){
        return Pickables.getNearest(itemPredicate);
    }

    public long timeTakenToLoot(Pickable itemToLoot){
        double distanceToItem =  itemToLoot.distance(dyingSpotsList.getFirst().getDeathPosition());
        long timeTaken = (long)distanceToItem * msPerSquareWalking;
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
        return Pickables.getAt(Players.getLocal().getPosition()).length >= 2;
    }

    public Pickable goodLootUnderMe(){
        Pickable[] lootUnderMe = Pickables.getAt(Players.getLocal().getPosition());
        List<Pickable> itemsToLoot = Arrays.stream(lootUnderMe).filter(itemPredicate).collect(Collectors.toList());
        return (itemsToLoot.size()>0)? itemsToLoot.get(0) : null;
    }


























}







































