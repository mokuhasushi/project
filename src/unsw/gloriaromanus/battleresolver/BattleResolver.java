package unsw.gloriaromanus.battleresolver;

import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Soldier;

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
        ArrayList<Soldier> attackerRouted = new ArrayList<>();
        ArrayList<Soldier> defenderRouted = new ArrayList<>();
        while (engagementsTotal <= 200 && !(attacker.isDefeated() || defender.isDefeated())) {
            int indexAttacker = rng.nextInt(attacker.getSize());
            Soldier soldierAttacker = attacker.getUnit(indexAttacker);
            int indexDefender = rng.nextInt(defender.getSize());
            Soldier soldierDefender = defender.getUnit(indexDefender);

            Skirmish s = new Skirmish(soldierAttacker, soldierDefender);
            SkirmishReport sr = s.solve();
            switch (sr.getResult()) {
                case U1_DEFEAT -> attacker.deleteUnit(indexAttacker);
                case U2_DEFEAT -> defender.deleteUnit(indexDefender);
                case U1_FLEE -> {
                    attackerRouted.add(soldierAttacker);
                    attacker.deleteUnit(indexAttacker);
                }
                case U2_FLEE -> {
                    defenderRouted.add(soldierDefender);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_DEFEATED -> {
                    attacker.deleteUnit(indexAttacker);
                    defender.deleteUnit(indexDefender);
                }
                case DRAW_FLED -> {
                    attackerRouted.add(soldierAttacker);
                    defenderRouted.add(soldierDefender);
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
