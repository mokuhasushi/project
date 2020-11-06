package unsw.gloriaromanus.game;

import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private String name;
    public Game game;
    private Random rng;
    private Faction faction;

    public AI (String name) {
        this.name = name;
        this.game = Game.getInstance();
        this.faction = game.getFaction(name);
        this.rng = new Random();
    }

    /**
     * Implementation without taking into account gold costs and buildings
     * Random strategy
     */
    public void playTurn() {
        for (int i = 0; i < faction.getProvinces().size(); i++) {
            Province p = faction.getProvinces().get(i);
            if (rng.nextDouble()< 0.4)
                p.recruit(SoldierType.values()[rng.nextInt(5)]);
            if (rng.nextDouble() < 0.2)
                game.moveOrInvade(p, game.getProvince(p.getNeighbours().get(rng.nextInt(p.getNeighbours().size()))));
        }
    }
}
