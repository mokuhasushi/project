package unsw.gloriaromanus.world;

import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.player.Barrack;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.SoldierFactory;

public class Province {
    private int wealth;
    private int wealthGrowth;
    private int taxes;
    private TaxLevels taxLevel;
    private int moraleModifier;
    private String name;
    private Faction owner;
    private Army army;
    private boolean justConquered;
    private Barrack barrack;

    public Province(int wealth, int wealthGrowth, int taxes, TaxLevels taxLevel, int moraleModifier, String name) {
        this.wealth = wealth;
        this.wealthGrowth = wealthGrowth;
        this.taxes = taxes;
        this.taxLevel = taxLevel;
        this.moraleModifier = moraleModifier;
        this.name = name;
    }

    public int getTaxRevenue () {
        return (int) Math.round((wealth*taxes/100.0) + 0.5);
    }

    public void setTaxLevel (TaxLevels taxLevel) {
        this.taxLevel = taxLevel;
        switch (taxLevel) {
            case LOW_TAX:
                taxes = 10;
                wealthGrowth = 10;
                break;
            case NORMAL_TAX:
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
        }
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
}
