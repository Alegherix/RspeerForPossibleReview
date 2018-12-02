import org.rspeer.runetek.api.movement.position.Position;

import java.util.LinkedList;
import java.util.List;

public class TestKlass {
    static LinkedList<DyingSpot> dyingSpots = new LinkedList<>();

    public static void main(String[] args){
        addSpots();
        System.out.println(containsPosition(new Position(3963,8723,0)));
    }



    public static void addSpots(){
        dyingSpots.add(new DyingSpot(new Position(3333,3333,0),61500));
        dyingSpots.add(new DyingSpot(new Position(3423,6383,0),61500));
        dyingSpots.add(new DyingSpot(new Position(3453,1233,0),61500));
        dyingSpots.add(new DyingSpot(new Position(3963,8723,0),61500));
    }
    static boolean containsPosition(Position position){
        return dyingSpots.stream().anyMatch(spot -> spot.getDeathPosition().equals(position));
    }
}
