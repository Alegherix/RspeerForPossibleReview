import Utility.WeightHandling;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;




@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {

    @Override
    public void onStart() {
        Log.info("\n\n\nRestart\n\n\n");
        super.onStart();
    }


    @Override
    public int loop() {
        return 300;
    }

}







































