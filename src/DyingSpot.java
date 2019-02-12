package src;

import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



public class DyingSpot {
    private Position deathPosition;
    private long deathTime;

    public DyingSpot(Position deathPosition, long deathTime) {
        this.deathPosition = deathPosition;
        this.deathTime = deathTime;
    }

    public Position getDeathPosition() {
        return deathPosition;
    }

    public void setDeathTime(long deathTime) {
        this.deathTime = deathTime;
    }

    public long getDeathTime() {
        return deathTime;
    }
}
