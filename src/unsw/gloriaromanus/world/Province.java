package unsw.gloriaromanus.world;

import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.units.*;

import java.util.ArrayList;

public class Province {

    private int wealth;
    private int wealthGrowth;
    private int taxes;
    private TaxLevel taxLevel;
    private int moraleModifier = 0;
    private String name;
    private String owner;
    private Army army;
    private boolean justConquered = false;
    private Barrack barrack;
    private ArrayList<String> neighbours;

    public Province(){}

    public Province(String name, String owner) {
        this.wealth = 100;
        this.wealthGrowth = 0;
        this.taxes = 15;
        this.taxLevel = TaxLevel.NORMAL_TAX;
        this.moraleModifier = 0;
        this.name = name;
        this.owner = owner;
        this.army = new Army();
        this.justConquered = false;
        this.barrack = new Barrack(owner);
        this.neighbours = new ArrayList<>();
    }
    public Province(String name) {
        this.wealth = 100;
        this.wealthGrowth = 0;
        this.taxes = 15;
        this.taxLevel = TaxLevel.NORMAL_TAX;
        this.moraleModifier = 0;
        this.name = name;
        this.army = new Army();
        this.justConquered = false;
        this.neighbours = new ArrayList<>();
    }
    public Province(int wealth, int wealthGrowth, int taxes, TaxLevel taxLevel, int moraleModifier, String name, String owner, Army army, boolean justConquered, Barrack barrack, ArrayList<String> neighbours) {
        this.wealth = wealth;
        this.wealthGrowth = wealthGrowth;
        this.taxes = taxes;
        this.taxLevel = taxLevel;
        this.moraleModifier = moraleModifier;
        this.name = name;
        this.owner = owner;
        this.army = army;
        this.justConquered = justConquered;
        this.barrack = barrack;
        this.neighbours = neighbours;
    }

    public int getTaxRevenue () {
        return (int) Math.round((wealth*taxes/100.0) + 0.5);
    }

    public void setTaxLevel (TaxLevel taxLevel) {
        this.taxLevel = taxLevel;
        switch (taxLevel) {
            case LOW_TAX:
                taxes = 10;
                wealthGrowth = 10;
                break;
            case NORMAL_TAX :
                taxes = 15;
                wealthGrowth = 0;
                break;
            case HIGH_TAX:
                taxes = 20;
                wealthGrowth = -10;
                break;
            case VERY_HIGH_TAX:
                taxes = 25;
                wealthGrowth = -30;
                moraleModifier = -1;
                break;
        }
    }

    public TaxLevel getTaxLevel() {
        return this.taxLevel;
    }

    public Army getArmy() {
//        return new Army((ArrayList<Soldier>) army.getArmy().clone());

        Army other = new Army();
        for (int i = 0; i < army.getSize(); i++)
            other.addUnit(army.getUnit(i));
        return other;


    }

    /**
     * Updates the Province according to the new Owner
     * @param faction String
     * @param army Army
     */
    public void conqueredBy(String faction, Army army) {
        this.army = army;
        this.owner = faction;
        this.justConquered = true;
        this.barrack = new Barrack(new SoldierFactory(faction));
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof Province))
            return false;
        return this.getName().equals(((Province)other).getName());
    }
    @Override
    public String toString () {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void removeTroops(Army army) {
        this.army.removeTroops(army);
    }

    public void addTroops(Army army) {
        this.army.joinArmy(army);
    }
    public void addTroop(Soldier s) {
        this.army.addUnit(s);
    }

    // This method accepts the army which has moved
    public void moveTroops(Army army) {
        army.moved(1);
        this.army.joinArmy(army);
    }

    public boolean recruit(SoldierType soldier) {
        return this.barrack.createSoldier(soldier);
    }

    /**
     * Updates the province for the passed turn
     */
    public void update() {
        this.wealth = Math.max(0, wealth+wealthGrowth);
        justConquered = false;
        barrack.turnPassed();
        Soldier s = barrack.getSoldierSlot1();
        if (s != null)
            army.addUnit(s);
        s = barrack.getSoldierSlot2();
        if (s != null)
            army.addUnit(s);
    }

    public int getWealth() {
        return wealth;
    }

    public void setNeighbours(ArrayList<String> neighbours) {this.neighbours = neighbours;}
    public void addNeighbour(String n) {
        this.neighbours.add(n);
    }
    public ArrayList<String> getNeighbours () {
        return this.neighbours;
    }

    public void setOwner(String key) {
        this.owner = key;
        this.barrack = new Barrack(key);
    }
    public int numberOfTrainingSlotsAvailable() {
        int free = 2;
        if (barrack.getTurnToSlot1()>0)
            free -=1;
        if (barrack.getTurnToSlot2()>0)
            free -=1;
        return free;
    }
}
