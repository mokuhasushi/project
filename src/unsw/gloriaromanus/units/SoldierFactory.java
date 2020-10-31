package unsw.gloriaromanus.units;

import java.util.HashMap;
import java.util.Map;

public class SoldierFactory {
    private Map<SoldierType, Soldier> nameToSoldier;

    public SoldierFactory(){
        nameToSoldier = new HashMap<>();
        for (SoldierType st : SoldierType.values()){
            nameToSoldier.put(st, soldierFromJSON(st.type));
        }
    }
    public SoldierFactory(String faction){
        nameToSoldier = new HashMap<>();
        for (SoldierType st : SoldierType.values()){
            nameToSoldier.put(st, soldierFromJSON(st.type, faction));
        }
    }

    public Soldier createSoldier(String soldier){
        return null;
    }
    public Soldier createSoldier(SoldierType soldierType){
        Soldier s = new Soldier();
        s.setName(soldierType.type);
        return s;
    }

    private Soldier soldierFromJSON (String soldierType) {
        return null;
    }
    private Soldier soldierFromJSON (String soldierType, String faction) {
        return null;
    }
}
