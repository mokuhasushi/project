package project.unsw.gloriaromanus.units;

/*
    I just need this class to test the various parameters in the BattleResolver
    Antonio
 */
public abstract class Unit {
    private int numTroops;  // the number of troops in this unit (should reduce based on depletion)
    private boolean ranged;  // range of the unit
    private int armour;  // armour defense
    private int morale;  // resistance to fleeing
    private int speed;  // ability to disengage from disadvantageous battle
    private int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    private int shieldDefense; // a shield

    public int getNumTroops() {
        return numTroops;
    }
    public boolean getRanged() {
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

}
