package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

// Using Singleton pattern
// TODO: Implement observer. This also gives meaning to the singleton
public class BattleResolver {
    private final Random rng;
    private static BattleResolver instance = null;
    private PropertyChangeListener textReport;
    private PropertyChangeSupport support;

    protected BattleResolver() {
        this.rng = new Random();
    }

    public void setTextReport(PropertyChangeListener textReport) {
        support.removePropertyChangeListener(this.textReport);
        this.textReport = textReport;
        support.addPropertyChangeListener(textReport);
    }
    public void clearTextReport(){
        if (this.textReport != null) {
            this.textReport = null;
        }
    }

    public static BattleResolver getInstance() {
        if (instance == null)
            instance = new BattleResolver();
        return instance;
    }

    public BattleResult battle(Army attacker, Army defender) {
        if (textReport != null) {
            support.firePropertyChange("", null, "Battle begun!");
        }
        int engagementsTotal = 0;
        Army attackerRouted = new Army();
        Army defenderRouted = new Army();
        while (engagementsTotal <= 200 && !(attacker.isDefeated() || defender.isDefeated())) {
            int indexAttacker = rng.nextInt(attacker.getSize());
            Soldier soldierAttacker = attacker.getUnit(indexAttacker);
            int indexDefender = rng.nextInt(defender.getSize());
            Soldier soldierDefender = defender.getUnit(indexDefender);

            Skirmish s;
            if (textReport != null)
                s = new Skirmish(soldierAttacker, soldierDefender, textReport);
            else
                s = new Skirmish(soldierAttacker, soldierDefender);
            SkirmishReport sr = s.solve();
            switch (sr.getResult()) {
                case U1_DEFEAT -> attacker.deleteUnit(indexAttacker);
                case U2_DEFEAT -> defender.deleteUnit(indexDefender);
                case U1_FLEE -> {
                    soldierAttacker.setBroken(false);
                    attackerRouted.addUnit(soldierAttacker);
                    attacker.deleteUnit(indexAttacker);
                }
                case U2_FLEE -> {
                    soldierDefender.setBroken(false);
                    defenderRouted.addUnit(soldierDefender);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_DEFEATED -> {
                    attacker.deleteUnit(indexAttacker);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_FLED -> {
                    soldierAttacker.setBroken(false);
                    soldierDefender.setBroken(false);
                    attackerRouted.addUnit(soldierAttacker);
                    defenderRouted.addUnit(soldierDefender);
                    attacker.deleteUnit(indexAttacker);
                    defender.deleteUnit(indexDefender);
                }
            }
            engagementsTotal += sr.getNumEngagements();
        }
        if (engagementsTotal > 200 || (attacker.isDefeated() && defender.isDefeated())) {
            attacker.joinArmy(attackerRouted);
            defender.joinArmy(defenderRouted);
            if (textReport != null)
                support.firePropertyChange("", null,
                        "The battle ended in a draw!");
            return BattleResult.DRAW;
        }
        if (defender.isDefeated()){
            attacker.joinArmy(attackerRouted);
            if (textReport != null)
                support.firePropertyChange("", null, "You won!");
            return BattleResult.ATTACKER_WON;
        }
        if (attacker.isDefeated()){
            attacker.joinArmy(attackerRouted);
            defender.joinArmy(defenderRouted);
            if (textReport != null)
                support.firePropertyChange("", null, "You lost!");
            return BattleResult.ATTACKER_DEFEATED;
        }

        return null;
    }
}
