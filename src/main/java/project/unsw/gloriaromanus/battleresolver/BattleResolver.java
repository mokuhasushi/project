package project.unsw.gloriaromanus.battleresolver;

import project.unsw.gloriaromanus.units.Army;
import project.unsw.gloriaromanus.units.Unit;

import java.util.ArrayList;
import java.util.Random;

public class BattleResolver {
    Army attacker;
    Army defender;
    Random rng;

    public BattleResolver(Army attacker, Army defender) {
        this.attacker = attacker;
        this.defender = defender;
        this.rng = new Random();
    }

    public BattleResult battle() {
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
        return null;
    }
}
