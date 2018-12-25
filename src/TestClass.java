import Utility.RandomHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;

import org.rspeer.runetek.api.Definitions;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.providers.RSItemDefinition;
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

    @Override
    public void onStart() {

    }

    @Override
    public int loop() {
        Player p = Players.getLocal();
        List<String> items  = getPlayerEquipment(p);
        if(items.size()>0){
            Log.info("Wearing items");
            items.stream().forEach(Log::info);
        }
        else{
            Log.info("Not wearing items");
        }
        return 2500;
    }



























}







































