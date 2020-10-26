package project.unsw.gloriaromanus.units;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers, heavy cavalry, elephants, chariots, archers, slingers, horse-archers, onagers, ballista, etc...
 * higher classes include ranged infantry, cavalry, infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */
public class HeavyInfantry extends Unit {
    public HeavyInfantry(){
        // TODO = obtain these values from the file for the unit
        this.numTroops = 50;
        ranged = false;
        armour = 5;
        morale = 10;
        speed = 10;
        attack = 6;
        defenseSkill = 10;
        shieldDefense = 3;
    }

}
