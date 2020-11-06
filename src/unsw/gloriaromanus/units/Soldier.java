package unsw.gloriaromanus.units;

/*
    I just need this class to test the various parameters in the BattleResolver
    Antonio
 */
public class Soldier {

    int numTroops;  // the number of troops in this soldier (should reduce based on depletion)
    boolean ranged;  // range of the soldier
    int armour;  // armour defense
    int morale;  // resistance to fleeing
    int speed;  // ability to disengage from disadvantageous battle
    int attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    int defenseSkill;  // skill to defend in battle. Does not protect from arrows!
    int shieldDefense; // a shield
    boolean isBroken;
    int trainingTime;
    int movement;
    int max_movement;
    boolean canMove;
    String name;
    int cost;

    public Soldier() {
        this.numTroops = 1;
        this.ranged = false;
        this.armour = 1;
        this.morale = 1;
        this.speed = 1;
        this.attack = 1;
        this.defenseSkill = 1;
        this.shieldDefense = 1;
        this.isBroken = false;
        this.trainingTime = 1;
        this.max_movement = 1;
        this.movement = max_movement;
        this.canMove = true;
        this.name = "";
        this.cost = 0;
    }

    public Soldier(int numTroops, boolean ranged, int armour,
                   int morale, int speed, int attack,
                   int defenseSkill, int shieldDefense, int trainingTime,
                   int max_movement, String name, int cost) {
        this.numTroops = numTroops;
        this.ranged = ranged;
        this.armour = armour;
        this.morale = morale;
        this.speed = speed;
        this.attack = attack;
        this.defenseSkill = defenseSkill;
        this.shieldDefense = shieldDefense;
        this.isBroken = false;
        this.trainingTime = trainingTime;
        this.max_movement = max_movement;
        this.canMove = true;
        this.movement = max_movement;
        this.name = name;
        this.cost = cost;
    }

    /**
     * For the Prototype pattern
     * @return a clone of this soldier
     */
    public Soldier clone () {
        return new Soldier(this.numTroops, this.ranged, this.armour, this.morale,
                this.speed, this.attack, this.defenseSkill, this.shieldDefense, this.trainingTime,
                this.max_movement, this.name, this.cost);
    }

    public int getMovement() {
        return movement;
    }

    public boolean isCanMove() {
        return canMove;
    }


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
    public int getTrainingTime() {
        return trainingTime;
    }
    public String getName() {return this.name;}
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public void addTroops(int numTroops) {this.numTroops += numTroops;}
    public void removeTroops(int numTroops) {this.numTroops -= Math.min(this.numTroops, numTroops);}
    public void setBroken(boolean bol) {this.isBroken = bol;}
    public void setSpeed(int speed) {this.speed = speed;}
    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }
    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
    public void setArmour(int armour) {
        this.armour = armour;
    }
    public void setMorale(int morale) {
        this.morale = morale;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setDefenseSkill(int defenseSkill) {
        this.defenseSkill = defenseSkill;
    }
    public void setShieldDefense(int shieldDefense) {
        this.shieldDefense = shieldDefense;
    }
    public void setTrainingTime(int trainingTime) {this.trainingTime = trainingTime;}
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    public void setMovement(int movement) {
        this.movement = movement;
    }
    public void resetMovement() {movement = max_movement;}


    public void reduceMovement(int distance) {
        this.movement -= distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
