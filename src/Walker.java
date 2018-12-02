import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

@ScriptMeta(developer = "Slazter", desc = "Edge Walker", name = "Walks to Edgeville Bank")
public class Walker extends Script {

    @Override
    public int loop() {
        Movement.walkTo(new Position(3094,3491));
        return Random.mid(250,400);
    }
}
