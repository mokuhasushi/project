package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Army;
import project.unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;
import java.util.Random;

// Using Singleton pattern TODO: not sure if it is actually an improvement
public class BattleResolver {
    private final Random rng;
    private static BattleResolver instance = null;

    private BattleResolver() {
        this.rng = new Random();
    }

    public static BattleResolver getInstance() {
        if (instance == null)
            instance = new BattleResolver();
        return instance;
    }

    public BattleResult battle(Army attacker, Army defender) {
        int engagementsTotal = 0;
        ArrayList<Unit> attackerRouted = new ArrayList<>();
        ArrayList<Unit> defenderRouted = new ArrayList<>();
        while (engagementsTotal <= 200 && !(attacker.isDefeated() || defender.isDefeated())) {
            int indexAttacker = rng.nextInt(attacker.getSize());
            Unit unitAttacker = attacker.getUnit(indexAttacker);
            int indexDefender = rng.nextInt(defender.getSize());
            Unit unitDefender = defender.getUnit(indexDefender);

            Skirmish s = new Skirmish(unitAttacker, unitDefender);
            SkirmishReport sr = s.solve();
            switch (sr.getResult()) {
                case U1_DEFEAT -> attacker.deleteUnit(indexAttacker);
                case U2_DEFEAT -> defender.deleteUnit(indexDefender);
                case U1_FLEE -> {
                    attackerRouted.add(unitAttacker);
                    attacker.deleteUnit(indexAttacker);
                }
                case U2_FLEE -> {
                    defenderRouted.add(unitDefender);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_DEFEATED -> {
                    attacker.deleteUnit(indexAttacker);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_FLED -> {
                    attackerRouted.add(unitAttacker);
                    defenderRouted.add(unitDefender);
                    attacker.deleteUnit(indexAttacker);
                    defender.deleteUnit(indexDefender);
                }
            }
            engagementsTotal += sr.getNumEngagements();
        }
        if (engagementsTotal > 200 || (attacker.isDefeated() && defender.isDefeated())) {
            attacker.joinArmy(attackerRouted);
            defender.joinArmy(defenderRouted);
            return BattleResult.DRAW;
        }
        if (defender.isDefeated()){
            attacker.joinArmy(attackerRouted);
            return BattleResult.ATTACKER_WON;
        }
        if (attacker.isDefeated()){
            attacker.joinArmy(attackerRouted);
            return BattleResult.ATTACKER_DEFEATED;
        }

        return null;
    }
}
