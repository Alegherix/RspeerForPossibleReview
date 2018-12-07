import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;


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


}







































