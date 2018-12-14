import Utility.LootHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {


    @Override
    public void onStart() {

    }


    @Override
    public int loop() {

        Log.info(Interfaces.getComponent(90,45).getText());
        return 300;
    }


    public static void main(String[] args) {
        Predicate<String> hornPred = item -> item.matches("(\\w*)(\\s)(\\w*)(\\s)(horn)|(\\w*)(\\s)(horn)");
        List<String> strings = Arrays.asList("Unicorn horn", "Desert goat horn", "Goat horn dust", "Unicorn dust");
        strings.stream().filter(hornPred).forEach(System.out::println);

    }

}







































