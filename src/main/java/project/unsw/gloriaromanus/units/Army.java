package project.unsw.gloriaromanus.units;

import java.util.ArrayList;
import java.util.Random;

/*
 Stub for armies, to be passed in the BattleResolver
 Antonio
*/
public class Army {
    private ArrayList<Unit> army = new ArrayList<>();

    public Army (ArrayList<Unit> army) {
        this.army = army;
    }

    public Army (){}

    public Unit getUnit(int index){
        return army.get(index);
    }

    public void addUnit(Unit unit){
        army.add(unit);
    }

    public void deleteUnit(int index){
        army.remove(index);
    }

    public int getSize(){
        return army.size();
    }
}
