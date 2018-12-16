import Utility.InterfaceHandling;
import Utility.LootHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {


    @Override
    public void onStart() {

    }


    @Override
    public int loop() {
        InterfaceHandling.gloryInterface().interact("Edgeville");
        return 2500;
    }


    public static void main(String[] args) throws Exception {

        System.out.println(Random.mid(200,500));
        ThreadLocalRandom.current().nextInt(200,500);
    }


}







































