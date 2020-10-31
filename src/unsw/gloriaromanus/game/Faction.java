package unsw.gloriaromanus.game;

import unsw.gloriaromanus.world.Province;

import java.util.ArrayList;

public class Faction {
    private ArrayList<Province> provinces;
    private int wealth;
    private int treasure;
    private String name;

    public Faction() {}

    public Faction(String name) {
        this.provinces = new ArrayList<>();
        this.name = name;
        this.wealth = 0;
        this.treasure = 0;
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

    @Override
    public String toString() {
        return this.name;
    }
}
