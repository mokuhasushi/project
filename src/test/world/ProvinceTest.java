package test.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.gloriaromanus.units.Army;
import unsw.gloriaromanus.units.Barrack;
import unsw.gloriaromanus.units.Soldier;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProvinceTest {
    Province province;
    final int WEALTH = 100;
    int WEALTH_GROWTH = 0;
    int TAXES = 15;
    TaxLevel TAX_LEVEL = TaxLevel.NORMAL_TAX;
    int MORALE_MODIFIER = 0;
    String NAME = "rome";
    String OWNER = "rome";
    Army army;
    boolean JUST_CONQUERED = false;
    BarrackStub barrack;
    ArrayList<String> neighbours = new ArrayList<>();

    @BeforeEach
    public void init() {
        barrack = new BarrackStub(OWNER);
        army = new Army();
        neighbours.add("tuscany");
        neighbours.add("napoli");
        province = new Province(WEALTH, WEALTH_GROWTH, TAXES, TAX_LEVEL,
                MORALE_MODIFIER, NAME, OWNER, army, JUST_CONQUERED,
                barrack, neighbours);
    }

    @Test
    public void taxRevenueWorksCorrectlyNormalTax() {
        province.setTaxLevel(TaxLevel.NORMAL_TAX);
        assertEquals(Math.round((WEALTH*15/100.0) + 0.5), province.getTaxRevenue());
    }
    @Test
    public void addTroopsArmy() {
        Army toAdd = new Army();
        Soldier s1 = new Soldier();
        Soldier s2 = new Soldier();
        toAdd.addUnit(s1);
        toAdd.addUnit(s2);
        province.addTroops(toAdd);
        assertTrue(province.getArmy().getArmy().contains(s1));
        assertTrue(province.getArmy().getArmy().contains(s2));
        assertEquals(2, province.getArmy().getSize());
    }
    @Test
    public void addTroopSoldier() {
        Soldier s1 = new Soldier();
        Soldier s2 = new Soldier();
        province.addTroop(s1);
        province.addTroop(s2);
        assertTrue(province.getArmy().getArmy().contains(s1));
        assertTrue(province.getArmy().getArmy().contains(s2));
        assertEquals(2, province.getArmy().getSize());
    }
    @Test
    public void removeTroops() {
        Army toAddAndRemove = new Army();
        Soldier s1 = new Soldier();
        Soldier s2 = new Soldier();
        toAddAndRemove.addUnit(s1);
        toAddAndRemove.addUnit(s2);
        province.addTroops(toAddAndRemove);
        province.removeTroops(toAddAndRemove);
        assertEquals(0, province.getArmy().getSize());
    }
    @Test
    public void recruitmentTest () {
        assertEquals(0, province.getArmy().getSize());
        Soldier s = new Soldier();
        barrack.setNext(s);
        barrack.setTurnsExpired(true);
        province.recruit(null);
        province.update();
        assertEquals(1, province.getArmy().getSize());
    }


    class BarrackStub extends Barrack{
        Soldier next;
        boolean turnsExpired;
        BarrackStub (String f) {super(f);}
        public void setNext(Soldier s){
            this.next = s;
        }
        public void setTurnsExpired (boolean b) {
            turnsExpired = b;
        }
        @Override
        public Soldier getSoldierSlot1 () {
            if (turnsExpired){
                turnsExpired = false;
                return next;}
            return null;
        }
        @Override
        public Soldier getSoldierSlot2 () {
            if (turnsExpired){
                turnsExpired = false;
                return next;}
            return null;
        }
        @Override
        public boolean createSoldier (SoldierType s) {
            return turnsExpired;
        }
    }
}
