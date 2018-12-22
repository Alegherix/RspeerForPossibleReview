import Utility.RandomHandling;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(developer = "Martin", desc = "Names Character", name = "Character Namer")
public class CharacterNamer extends Script {


    @Override
    public int loop() {
        setName();
        return RandomHandling.randomReturn();
    }

    private InterfaceComponent getNameComponent(){
        return Interfaces.getComponent(558,7);
    }

    private void setName(){
        if(Interfaces.getComponent(558,12).getText().contains("available")){
            Log.info("Should click on set name");
            setNameClick();
        }
        else if(getNameChatBox().getText().contains("What")){
            Keyboard.sendText("Tedrexluthor142");
            Keyboard.pressEnter();

        }
        else if(getNameChatBox()!=null){
            getNameComponent().click();
        }
        RandomHandling.randomSleep();


    }

    public InterfaceComponent getNameChatBox(){
        return Interfaces.getComponent(162,44);
    }

    public void setNameClick(){
        Interfaces.getComponent(558,18).interact("Set name");
    }
}
