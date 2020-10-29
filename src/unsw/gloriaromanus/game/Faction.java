package unsw.gloriaromanus.game;

import unsw.gloriaromanus.world.Province;

import java.util.ArrayList;

public class Faction {
    private ArrayList<Province> provinces;
    private int wealth;
    private final String name;

    public Faction(String name) {
        this.name = name;
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
}
