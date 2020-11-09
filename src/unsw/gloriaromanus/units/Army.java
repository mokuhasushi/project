package unsw.gloriaromanus.units;

import java.util.ArrayList;
import java.util.List;

/*
 Stub for armies, to be passed in the BattleResolver
 Antonio
*/
public class Army {
    ArrayList<Soldier> army = new ArrayList<>();

    public Army (List<Soldier> army) {
        this.army = new ArrayList<>(army);
    }

    public Army (){}

    //This getter and setter should never be used.
    // They are here for json conversion
    public ArrayList<Soldier> getArmy() {
        return army;
    }

    public void setArmy(ArrayList<Soldier> army) {
        this.army = army;
    }

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

    public void joinArmy (Army other) {
        for (int i = 0; i < other.getSize(); i++) {
            army.add(other.getUnit(i));
        }
    }

    public void removeTroops(Army other) {
        for (int i = 0; i < other.getSize(); i++) {
            army.remove(other.getUnit(i));
        }
    }

    public void moved(int distance) {
        for (Soldier s: army) {
            s.reduceMovement(distance);
        }
    }

    /**
     * movement of an Army equals its slowest soldier movement
     * @return movement of Army
     */
    public int getMovement() {
        int movement = -1;
        for (Soldier s: army) {
            if (s.movement < movement)
                movement = s.movement;
            else if (movement < 0)
                movement = s.movement;
        }
        return movement;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder("");
        for (Soldier s: army)
            sb.append(s.toString()+", ");
        return sb.toString();
    }
}
