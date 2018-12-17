package Utility;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class CombatHandling extends Script {

    private static int[] getMeleeLevels(){
        return new int[]{Skills.getCurrentLevel(Skill.ATTACK),Skills.getCurrentLevel(Skill.STRENGTH), Skills.getCurrentLevel(Skill.DEFENCE)};
    }

    public int chooseSkill(){
        int index = 0;
        int lowest = getMeleeLevels()[0];

        for (int i = 1; i < getMeleeLevels().length; i++) {
            if(lowest < getMeleeLevels()[i]){
                lowest = getMeleeLevels()[i];
                index++;
            }
        }
        return index;
    }

    public boolean shouldUpdateStance(){
        return chooseSkill() != Combat.getSelectedStyle();
    }

    public void updateStance(){
        int skillToTrain = chooseSkill();
        int currentStyle = Combat.getSelectedStyle();

        if(currentStyle!= skillToTrain){

        }
    }

    public boolean shouldSwitchWeapon(){
        return false;
    }

    public static boolean shouldEat(int percent){
        return Players.getLocal().getHealthPercent()<=percent;
    }

    public static boolean canEat(){
        return Inventory.contains(i -> i.containsAction("Eat"));
    }

    public static boolean canAndShouldEat(int limit){
        return shouldEat(limit) && canEat();
    }

    public static void eatFood(){
        Inventory.getFirst(item -> item.containsAction("Eat")).interact("Eat");
        RandomHandling.randomSleep(550,640);
    }
}
