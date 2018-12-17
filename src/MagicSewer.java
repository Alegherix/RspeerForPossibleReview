import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Magic;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.function.Predicate;

@ScriptMeta(developer = "Slazter", desc = "Level account in Sewers", name = "Sewer Magics")
public class MagicSewer extends Script {

    private String currentSpell;
    private final Position SEWEROPENING = new Position(3237, 3458,0);
    private final String ZOMBIE = "Zombie";
    private final Position CASTINGPOSITION = new Position(3229,9900);
    private final Area CASTINGAREA = Area.rectangular(3228, 9898, 3231, 9901);
    private Npc currentlyAttackingNpc;

    @Override
    public void onStart() {
        currentSpell = "";
        super.onStart();
    }

    @Override
    public int loop() {
        if(shouldWalkToCastingArea()){
            castingAreaWalking();
        }
        else if(canAttackZombies()){
            if(haveLeveledUp()!=null){
                handleLevelUp();
            }
            else if(continuationDialog()!=null){
                handleContinuationDialog();
            }
            else if(shouldSwitchSpell()){
                switchSpell();
            }
            else{
                if(currentlyAttackingNpc!=null){
                    if(currentlyAttackingNpc.getHealthPercent()<2 || !currentlyAttackingNpc.isHealthBarVisible()){
                        currentlyAttackingNpc=null;
                    }
                }
                else{
                    attackZombie();
                }

            }
        }
        return RandomHandling.randomNumber(601,715);
    }



    public InterfaceComponent haveLeveledUp(){
        return Interfaces.getComponent(233,1);
    }

    public void handleLevelUp(){
        Interfaces.getComponent(233,3).click();
    }

    public void fixAttackOptions(){
        Interfaces.getComponent(261,107,3).click();
    }

    public boolean shouldFixAttackOptions(){
        return !Interfaces.getComponent(261,90,4).getText().equals("Left-click where available");
    }

    public void switchSpell(){
        InterfaceComponent defensive = Interfaces.getComponent(593,20);
        InterfaceComponent autoSpells = Interfaces.getComponent(201,1);

        if(autoSpells!=null){
            getSpellToCast().click();
            updateCastingSpell();
        }
        else if(defensive!=null){
            defensive.click();
        }
    }

    public void updateCastingSpell(){
        int magicLevel = Skills.getCurrentLevel(Skill.MAGIC);

        if(magicLevel>=13){
            currentSpell = "Fire strike";
        }
        else if(magicLevel>=9){
            currentSpell = "Earth strike";
        }
        else if(magicLevel>=5){
            Log.info("Setting current spell to : " + "Water strike");
            currentSpell = "Water strike";
        }
        else{
            currentSpell = "Air strike";
        }

    }

    public boolean shouldSwitchSpell(){
        int magicLevel = Skills.getCurrentLevel(Skill.MAGIC);

        if(magicLevel>=13 && !"Fire strike".equals(currentSpell)){
            return true;
        }
        else if(magicLevel>=9 && magicLevel<13 && !"Earth strike".equals(currentSpell)){
            return true;
        }
        else if(magicLevel>=5 && magicLevel<9 && !"Water strike".equals(currentSpell)){
            return true;
        }
        else if(magicLevel>=1 && magicLevel<5 && !"Air strike".equals(currentSpell)){
            Log.info("Over level 1 and not casting Air strike");
            return true;
        }
        else {
            return false;
        }
    }

    public InterfaceComponent getSpellToCast(){
        int magicLevel = Skills.getCurrentLevel(Skill.MAGIC);

        if(magicLevel>=13){
            return Interfaces.getComponent(201,1,4);
        }
        else if(magicLevel>=9) {
            return Interfaces.getComponent(201, 1, 3);
        }
        else if(magicLevel>=5){
            return Interfaces.getComponent(201,1,2);
        }
        else{
            return Interfaces.getComponent(201,1,1);
        }
    }


    // Part that deals with Attacking Zombies
    public void attackZombie(){

        if ((Players.getLocal().getAnimation()==-1 && Players.getLocal().getAnimationFrame()==13)
            || (Players.getLocal().getAnimation()==-1 && Players.getLocal().getAnimationFrame()==11)){
            if(zombie()!=null){
                currentlyAttackingNpc = zombie();
                Log.info("Attacking");
                zombie().interact("Attack");
            }
        }
    }

    public Npc zombie(){
        return Npcs.getNearest(npc-> npc.getPosition().compareTo(Players.getLocal().getPosition())<=15 && npc.getName().equals(ZOMBIE));
    }

    public boolean canAttackZombies(){
        return zombie()!=null;
    }



    public InterfaceComponent continuationDialog(){
        return Interfaces.getComponent(193,3);
    }

    public void handleContinuationDialog(){
        Interfaces.getComponent(193,3).click();
    }

    // Part that deals with walking to Zombies //
    public boolean isInSewers(){
        return Players.getLocal().getPosition().getY()>9000;
    }
    public boolean shouldWalkToCastingArea(){
        return !CASTINGAREA.contains(Players.getLocal().getPosition());
    }

    public void walkToCastingArea(){
        Movement.walkTo(CASTINGPOSITION);
    }

    public void walkToManhole(){
       RunningHandling.smartWalking(SEWEROPENING);
    }

    public SceneObject manhole(){
        Predicate<SceneObject> manhole = hole -> hole.distance(Players.getLocal().getPosition())<=5
                && hole.getName().equals("Manhole");
        return SceneObjects.getNearest(manhole);
    }

    public void openManhole(){
        if(Arrays.asList(manhole().getActions()).contains("Open")){
            manhole().interact("Open");
        }
        else if(Arrays.asList(manhole().getActions()).contains("Climb-down")){
            manhole().interact("Climb-down");
        }
    }


    public void castingAreaWalking(){
        if(isInSewers()){
            walkToCastingArea();
        }
        else{
            if(manhole()!=null){
                openManhole();
            }
            else{
                walkToManhole();
            }
        }
    }

}
