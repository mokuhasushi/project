package gloriaromanus.world;

public class Province {
    private int wealth;
    private int wealthGrowth;
    private int taxes;
    private TaxLevels taxLevel;
    private int moraleModifier;
    private String name;

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

}
