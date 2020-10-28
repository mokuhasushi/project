package gloriaromanus.units;

import java.util.ArrayList;

/*
 Stub for armies, to be passed in the BattleResolver
 Antonio
*/
public class Army {
    private ArrayList<Soldier> army = new ArrayList<>();

    public Army (ArrayList<Soldier> army) {
        this.army = army;
    }

    public Army (){}

    public Soldier getUnit(int index){
        return army.get(index);
    }

    public void addUnit(Soldier soldier){
        army.add(soldier);
    }

    public void deleteUnit(int index){
        army.remove(index);
    }

    public void updateUnit(int index, Soldier soldier) { army.set(index, soldier);}

    public int getSize(){
        return army.size();
    }

    public boolean isDefeated() {return army.size() == 0;}

    public void joinArmy (ArrayList<Soldier> other) {
        army.addAll(other);
    }
}
