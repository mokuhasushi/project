package unsw.gloriaromanus.game;

import unsw.gloriaromanus.units.SoldierFactory;
import unsw.gloriaromanus.world.Province;

import java.util.ArrayList;

public class Faction {
    private ArrayList<Province> provinces;
    private int wealth;
    private int treasure;
    private String name;
    private SoldierFactory soldierFactory;

    public Faction() {}

    public Faction(String name) {
        this.provinces = new ArrayList<>();
        this.name = name;
        this.wealth = 0;
        this.treasure = 0;
        this.soldierFactory = new SoldierFactory(name);
    }

    //TODO: pass this factory around, instead of creating/destroying one each time
    //      each time a province changes its state/is created
    public SoldierFactory getSoldierFactory() {
        return soldierFactory;
    }

    public ArrayList<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(ArrayList<Province> provinces) {
        this.provinces = provinces;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public String getName() {
        return name;
    }

    public void removeProvince(Province province) {
        provinces.remove(province);
    }

    public void addProvince(Province province) {
        provinces.add(province);
    }

    public void addWealth(int wealth) {
        this.wealth += wealth;
    }

    public void addTreasure(int gold) {
        this.treasure += gold;
    }

    public int getTreasure() {
        return treasure;
    }
    public void setTreasure (int treasure) {
        this.treasure = treasure;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public Province getProvince(String province) {
        Province p = null;
        for (int i = 0; i < provinces.size(); i++) {
            if (provinces.get(i).getName().equals(province)){
                p = provinces.get(i);
                break;
            }
        }
        return p;
    }
/*
    public void updateWealth () {
        int wealth = 0;
        for (Province p: provinces)
            wealth += p.getWealth();
        this.wealth = wealth;
    }
*/
    public void update() {
        int wealth = 0;
        for (Province p: provinces){
            p.update();
            this.addTreasure(p.getTaxRevenue());
            wealth += p.getWealth();
        }
        this.wealth = wealth;
    }
}
