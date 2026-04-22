import alchemy.Quantity;
import alchemy.State;
import alchemy.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class for rapid testing
 */
public class Main {
    public static void main(String[] args) {
        Map<Unit,Integer> myMap1 = new HashMap<>();
        myMap1.put(Unit.SPOON,4);

        Map<Unit,Integer> myMap2 = new HashMap<>();
        myMap2.put(Unit.BOTTLE,10);

        Map<Unit,Integer> myMap3 = new HashMap<>();
        myMap3.put(Unit.DROP,7);

        Map<Unit,Integer> myMap4 = new HashMap<>();
        myMap4.put(Unit.PINCH,4);

        Map<Unit,Integer> myMap5 = new HashMap<>();
        myMap5.put(Unit.PINCH,3);

        Quantity myQuantity1 = new Quantity(State.LIQUID, myMap1);
        Quantity myQuantity2 = new Quantity(State.LIQUID, myMap2);
        Quantity myQuantity3 = new Quantity(State.LIQUID, myMap3);
        Quantity myQuantity4 = new Quantity(State.POWDER, myMap4);
        IO.println(myQuantity4.getSpoons());
        Quantity myQuantity5 = new Quantity(State.POWDER, myMap5);

        Quantity sumQuantity = Quantity.sum(List.of(myQuantity1,myQuantity2,myQuantity3,myQuantity4,myQuantity5),State.LIQUID);

        IO.println("Drops: "+sumQuantity.getAmountOf(Unit.DROP));
        IO.println("Spoons: "+sumQuantity.getAmountOf(Unit.SPOON));
        IO.println("Vials: "+sumQuantity.getAmountOf(Unit.VIAL));
        IO.println("Bottles: "+sumQuantity.getAmountOf(Unit.BOTTLE));
    }
}
