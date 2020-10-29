package unsw.gloriaromanus.units;

public enum SoldierType {
    MELEE_INFANTRY("melee-infantry"),
    RANGED_INFANTRY("ranged-infantry"),
    MELEE_CHIVALRY("melee-chivalry"),
    RANGED_CHIVALRY("ranged-chivalry"),
    MELEE_ARTILLERY("melee-artillery"),
    RANGED_ARTILLERY("ranged-artillery");

    public final String type;

    SoldierType(String type) {
        this.type = type;
    }
}
