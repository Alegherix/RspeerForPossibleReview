package Utility;

import org.rspeer.script.Script;

import java.util.HashMap;

public abstract class WeightHandling extends Script {

    public static HashMap<String, Double> initiateMap(HashMap<String, Double> weights){
        weights.put("Shark", 0.65);
        weights.put("Tuna potato", 0.5);
        weights.put("Anglerfish", 0.33);
        weights.put("Dark crab", 0.3);
        weights.put("Sea turtle", 0.35);
        weights.put("Cooked karambwan", 0.5);
        weights.put("Manta ray", 0.4);
        weights.put("Pineapple pizza", 0.25);
        weights.put("Mushroom potato", 0.5);
        weights.put("Amethyst arrow", 0.0);
        weights.put("Dragon arrow", 0.0);
        weights.put("Onyx bolts (e)", 0.0);
        weights.put("Dragonstone bolts (e)", 0.0);
        weights.put("Dragonstone dragon bolts (e)", 0.0);
        weights.put("Diamond dragon bolts (e)", 0.0);
        weights.put("Amethyst broad bolts", 0.0);
        weights.put("Abyssal whip", 0.45);
        weights.put("Abyssal dagger", 0.0);
        weights.put("Scythe of vitur (uncharged)", 0.0);
        weights.put("Ghrazi rapier", 1.0);
        weights.put("Elder maul", 5.0);
        weights.put("Saradomin sword", 3.0);
        weights.put("Armadyl godsword", 11.0);
        weights.put("Bandos godsword", 10.0);
        weights.put("Saradomin godsword", 10.0);
        weights.put("Zamorak godsword", 10.0);
        weights.put("3rd age longsword", 1.0);
        weights.put("Granite maul", 4.5);
        weights.put("Tzhaar-ket-om", 3.0);
        weights.put("Dragon thrownaxe", 0.0);
        weights.put("Dragon javelin", 0.0);
        weights.put("Dragon dart", 0.0);
        weights.put("Dragon dart(p++)", 0.0);
        weights.put("Dragon dart(p+)", 0.0);
        weights.put("Dragon dart(p)", 0.0);
        weights.put("Dragon 2h sword", 3.0);
        weights.put("Dragon claws", 0.0);
        weights.put("Dragon scimitar", 1.8);
        weights.put("Dragon sword", 1.0);
        weights.put("Dragon dagger(p++)", 1.0);
        weights.put("Mystic hat (dark)", 0.4);
        weights.put("Mystic hat (light)", 0.4);
        weights.put("Dharok's platelegs 0", 0.0);
        weights.put("Dharok's platebody 0", 9.0);
        weights.put("Dharok's helm 0", 0.0);
        weights.put("Dharok's greataxe 0", 0.0);
        weights.put("Ahrim's robetop 0", 0.0);
        weights.put("Ahrim's robeskirt 0", 0.0);
        weights.put("Ahrim's hood 0", 0.0);
        weights.put("Ahrim's staff 0", 0.0);
        weights.put("Torag's platelegs 0", 0.0);
        weights.put("Torag's platebody 0", 0.0);
        weights.put("Torag's helm 0", 0.0);
        weights.put("Guthan's warspear 0", 0.0);
        weights.put("Guthan's platebody 0", 0.0);
        weights.put("Guthan's helm 0", 0.0);
        weights.put("Guthan's chainskirt 0", 0.0);
        weights.put("Verac's plateskirt 0", 0.0);
        weights.put("Verac's helm 0", 0.0);
        weights.put("Verac's flail 0", 0.0);
        weights.put("Verac's brassard 0", 0.0);
        weights.put("Mystic robe bottom (dark)", 1.8);
        weights.put("Mystic robe bottom (light)", 1.8);
        weights.put("Mystic robe top", 0.0);
        weights.put("Mystic mud staff", 2.0);
        weights.put("Mystic robe bottom", 0.0);
        weights.put("Mystic robe top (dark)", 2.7);
        weights.put("Mystic robe top (light)", 2.7);
        weights.put("Mystic smoke staff", 2.0);
        weights.put("Infinity boots", 0.4);
        weights.put("Infinity bottoms", 1.0);
        weights.put("Infinity gloves", 0.0);
        weights.put("Infinity hat", 0.0);
        weights.put("Infinity top", 2.0);
        weights.put("Elysian spirit shield", 2.0);
        weights.put("Arcane spirit shield", 2.6);
        weights.put("Ancestral robe top", 2.7);
        weights.put("Kodai wand", 0.0);
        weights.put("Ancestral robe bottom", 1.8);
        weights.put("3rd age platebody", 9.0);
        weights.put("3rd age platelegs", 2.0);
        weights.put("Spectral spirit shield", 2.6);
        weights.put("Dragon full helm", 2.0);
        weights.put("Dragon warhammer", 2.0);
        weights.put("Armadyl chestplate", 4.0);
        weights.put("Ranger boots", 0.2);
        weights.put("Pegasian boots", 1.0);
        weights.put("Armadyl chainskirt", 1.0);
        weights.put("Armadyl crossbow", 6.0);
        weights.put("3rd age range top", 4.0);
        weights.put("Bandos tassets", 8.0);
        weights.put("Abyssal bludgeon", 6.0);
        weights.put("Primordial boots", 1.0);
        weights.put("Dragon platebody", 1.3);
        weights.put("Bandos chestplate", 12.0);
        weights.put("Ancestral hat", 0.0);
        weights.put("Necklace of anguish", 0.0);
        weights.put("Amulet of torture", 0.0);
        weights.put("Ring of suffering", 0.0);
        weights.put("Tormented bracelet", 0.0);
        weights.put("Rangers' tunic", 3.0);
        weights.put("Dragon crossbow", 6.0);
        weights.put("Super combat potion(4)", 0.0);
        weights.put("Super combat potion(3)", 0.0);
        weights.put("Super combat potion(2)", 0.0);
        weights.put("Super combat potion(1)", 0.0);
        weights.put("Super restore(4)", 0.0);
        weights.put("Super restore(3)", 0.0);
        weights.put("Super restore(2)", 0.0);
        weights.put("Super restore(1)", 0.0);
        weights.put("Saradomin brew(4)", 0.0);
        weights.put("Saradomin brew(3)", 0.0);
        weights.put("Saradomin brew(2)", 0.0);
        weights.put("Saradomin brew(1)", 0.0);
        weights.put("Death rune", 0.0);
        weights.put("Ancient staff", 2.2);
        weights.put("Toxic staff (uncharged)", 0.0);
        weights.put("Staff of the dead", 1.5);
        weights.put("Staff of light", 1.5);
        weights.put("Ranging potion(4)", 0.0);
        weights.put("Ranging potion(3)", 0.0);
        weights.put("Super strength(4)", 0.0);
        weights.put("Super strength(3)", 0.0);
        weights.put("Super strength(2)", 0.0);
        weights.put("Super strength(1)", 0.0);
        weights.put("Amulet of glory", 0.0);
        weights.put("Amulet of glory(4)", 0.0);
        weights.put("Helm of neitiznot", 2.2);
        weights.put("Berserker helm", 2.7);
        weights.put("Berserker ring", 0.0);
        weights.put("Seers' ring", 0.0);
        weights.put("Archers' ring", 0.0);
        weights.put("Warrior ring", 0.0);
        weights.put("Magic shortbow", 1.36);
        weights.put("Obsidian cape", 1.8);
        weights.put("Obsidian helm", 3.0);
        weights.put("Obsidian platebody", 9.0);
        weights.put("Obsidian platelegs", 9.0);
        weights.put("Toktz-ket-xil", 3.4);
        weights.put("Black d'hide body", 6.8);
        weights.put("Amulet of fury", 0.0);
        weights.put("Black d'hide chaps", 5.4);
        weights.put("Black d'hide vamb", 0.28);
        weights.put("Rune Platebody", 9.9);
        weights.put("Karil's leathertop 0", 0.0);
        weights.put("Karil's leatherskirt 0", 0.0);
        weights.put("Karil's crossbow 0", 0.0);
        weights.put("Elder chaos hood", 0.1);
        weights.put("Elder chaos robe", 0.1);
        weights.put("Elder chaos top", 0.2);
        weights.put("Rune plateskirt", 8.0);
        weights.put("Gilded plateskirt", 9.0);
        weights.put("Rune platelegs", 9.0);
        weights.put("Gilded platelegs", 9.0);
        weights.put("Gilded boots", 1.3);
        weights.put("Rune knife", 0.0);
        weights.put("Rune knife(p++)", 0.0);
        weights.put("Rune knife(p+)", 0.0);
        weights.put("Rune knife(p)", 0.0);
        weights.put("Rune dart(p++)", 0.0);
        weights.put("Rune dart(p+)", 0.0);
        weights.put("Rune dart(p)", 0.0);
        weights.put("Rune dart", 0.0);
        weights.put("Prayer potion(4)", 0.0);
        weights.put("Prayer potion(3)", 0.0);
        weights.put("Prayer potion(2)", 0.0);
        weights.put("Prayer potion(1)", 0.0);
        weights.put("Ring of recoil", 0.0);
        weights.put("Bastion potion(4)", 0.0);
        weights.put("Bastion potion(3)", 0.0);
        weights.put("Bastion potion(2)", 0.0);
        weights.put("Bastion potion(1)", 0.0);
        weights.put("Rune crossbow", 6.0);
        weights.put("Monkfish", 0.4);
        weights.put("Dark bow", 1.9);
        weights.put("Heavy ballista", 6.0);
        weights.put("Light ballista", 4.0);
        weights.put("Amulet of strength", 0.0);
        weights.put("Dragon boots", 1.0);
        weights.put("Tome of fire (empty)", 0.0);
        weights.put("Burnt page", 0.0);
        weights.put("Wizard boots", 0.0);
        weights.put("Regen bracelet", 0.25);

        return weights;
    }
}