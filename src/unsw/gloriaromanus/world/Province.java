package unsw.gloriaromanus.world;

import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.units.Barrack;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierFactory;

public class Province {
    private int wealth;
    private int wealthGrowth;
    private int taxes;
    private TaxLevel taxLevel;
    private int moraleModifier;
    private final String name;
    private Faction owner;
    private Army army;
    private boolean justConquered;
    private Barrack barrack;

    public Province(int wealth, int wealthGrowth, int taxes, TaxLevel taxLevel, int moraleModifier, String name, Army army, Faction owner) {
        this.wealth = wealth;
        this.wealthGrowth = wealthGrowth;
        this.taxes = taxes;
        this.taxLevel = taxLevel;
        this.moraleModifier = moraleModifier;
        this.name = name;
        this.army = army;
    }
    public Province(String name, Faction owner) {
        this.wealth = 100;
        this.wealthGrowth = 0;
        this.taxes = 15;
        this.taxLevel = TaxLevel.NORMAL_TAX;
        this.moraleModifier = 0;
        this.name = name;
        this.owner = owner;
        this.army = new Army();
        this.justConquered = false;
        this.barrack = new Barrack(owner.getName());
    }

    public int getTaxRevenue () {
        return (int) Math.round((wealth*taxes/100.0) + 0.5);
    }

    public void setTaxLevel (TaxLevel taxLevel) {
        this.taxLevel = taxLevel;
        switch (taxLevel) {
            case LOW_TAX -> {
                taxes = 10;
                wealthGrowth = 10;
            }
            case NORMAL_TAX -> {
                taxes = 15;
                wealthGrowth = 0;
            }
            case HIGH_TAX -> {
                taxes = 20;
                wealthGrowth = -10;
            }
            case VERY_HIGH_TAX -> {
                taxes = 25;
                wealthGrowth = -30;
                moraleModifier = -1;
            }
        }
    }

    public TaxLevel getTaxLevel() {
        return this.taxLevel;
    }

    public Army getArmy() {
        return army;
    }

    public void conqueredBy(Faction faction, Army army) {
        this.army = army;
        this.owner = faction;
        this.justConquered = true;
        this.barrack = new Barrack(new SoldierFactory(faction.getName()));
    }

    public Faction getOwner() {
        return this.owner;
    }

    @Override
    public boolean equals (Object other) {
        if (!(other instanceof Province))
            return false;
        return this.getName().equals(((Province)other).getName());
    }

    private String getName() {
        return name;
    }

    public void removeTroops(Army attacker) {
        this.army.removeTroops(attacker);
    }

    public void addTroops(Army attacker) {
        this.army.joinArmy(attacker);
    }

    public void moveTroops(Army army) {
        army.moved(1);
        this.army.joinArmy(army);
    }

    public void recruit(String soldier) {
        this.barrack.createSoldier(soldier);
    }

    public void update() {
        this.wealth = Math.max(0, wealth+wealthGrowth);
        justConquered = false;
        barrack.turnPassed();
        // TODO: Here implementing an observer would be good sense.
        // Since it's just two slots I leave it for later
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
}
