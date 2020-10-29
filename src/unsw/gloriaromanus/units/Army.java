package unsw.gloriaromanus.units;

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
}
