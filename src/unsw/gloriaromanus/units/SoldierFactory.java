package unsw.gloriaromanus.units;

import unsw.gloriaromanus.game.SaveLoad;

import java.util.HashMap;
import java.util.Map;

public class SoldierFactory {
    private Map<SoldierType, Soldier> nameToSoldier;

    public Map<SoldierType, Soldier> getNameToSoldier() {
        return nameToSoldier;
    }

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

    public Soldier createSoldier(SoldierType soldierType){
        return nameToSoldier.get(soldierType).clone();
    }

    private Soldier soldierFromJSON (String soldierType) {
//        return SaveLoad.loadSoldier(soldierType+".json");
        return SaveLoad.loadSoldier("src/unsw/gloriaromanus/unitsjsons/"+soldierType+".json");
    }
    private Soldier soldierFromJSON (String soldierType, String faction) {
        Soldier s = SaveLoad.loadSoldier("src/unsw/gloriaromanus/unitsjsons/"+soldierType+"-"+faction+".json");
        if (s == null)
            s = soldierFromJSON(soldierType);
        return s;
    }
}
