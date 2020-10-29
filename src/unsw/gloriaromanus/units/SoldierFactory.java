package unsw.gloriaromanus.units;

import java.util.HashMap;
import java.util.Map;

public class SoldierFactory {
    private Map<SoldierType, Soldier> nameToSoldier;

    public SoldierFactory(){
        for (SoldierType st : SoldierType.values()){
            nameToSoldier.put(st, soldierFromJSON(st.type));
        }
    }
    public SoldierFactory(String faction){
        for (SoldierType st : SoldierType.values()){
            nameToSoldier.put(st, soldierFromJSON(st.type, faction));
        }
    }

    public Soldier createSoldier(String soldier){
        return null;
    }

    private Soldier soldierFromJSON (String soldierType) {
        return null;
    }
    private Soldier soldierFromJSON (String soldierType, String faction) {
        return null;
    }
}
