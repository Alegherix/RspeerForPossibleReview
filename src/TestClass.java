import Utility.WeightHandling;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.HashMap;
import java.util.Map;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    Map<String, Double> weights;

    @Override
    public void onStart() {
        weights = WeightHandling.initiateMap(new HashMap<>());
        Log.info("\n\n\nRestart\n\n\n");
        super.onStart();
    }


    @Override
    public int loop() {

        return 300;
    }


}







































