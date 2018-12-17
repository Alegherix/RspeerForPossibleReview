import Utility.MagicHandling;
import Utility.RandomHandling;
import Utility.RunningHandling;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;


@ScriptMeta(developer = "Slazter", desc = "TestClass", name = "For various testing purposes")
public class TestClass extends Script {


    private double targetDistance;
    private double currentDistance;
    private boolean nextClickSet;
    private int nextClick;

    @Override
    public void onStart() {

    }


    @Override
    public int loop() {
        if(MagicHandling.shouldSetupAutoCast()){
            MagicHandling.enableFireStrike();
        }
        return RandomHandling.randomReturn();
    }


    public static void main(String[] args) throws Exception {


    }


}







































