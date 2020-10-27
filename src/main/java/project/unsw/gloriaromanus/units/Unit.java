package project.unsw.gloriaromanus.units;

/*
    I just need this class to test the various parameters in the BattleResolver
    Antonio
 */
public abstract class Unit {
    int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    boolean ranged;  // range of the unit
    int armour;  // armour defense
    int morale;  // resistance to fleeing
    int speed;  // ability to disengage from disadvantageous battle
    int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    int shieldDefense; // a shield
    boolean isBroken;

    public int getNumTroops() {
        return numTroops;
    }
    public boolean isRanged() {
        return ranged;
    }
    public int getArmour() {
        return armour;
    }
    public int getMorale() {
        return morale;
    }
    public int getSpeed() {
        return speed;
    }
    public int getAttack() {
        return attack;
    }
    public int getDefenseSkill() {
        return defenseSkill;
    }
    public int getShieldDefense() {
        return shieldDefense;
    }
    public boolean isBroken() {return isBroken;}
    public boolean isDefeated() {return numTroops == 0;}

    public void addTroops(int numTroops) {this.numTroops += numTroops;}
    public void removeTroops(int numTroops) {this.numTroops -= Math.min(this.numTroops, numTroops);}
    public void setBroken(boolean bol) {this.isBroken = bol;}

}
