package unsw.gloriaromanus.units;

public class Barrack {
    private int turnToSlot1 = 0;
    private int turnToSlot2 = 0;

    private Soldier soldierSlot1 = null;
    private Soldier soldierSlot2 = null;

    private SoldierFactory soldierFactory;

    public Barrack (String faction) {
        this.soldierFactory = new SoldierFactory(faction);
    }
    public Barrack (SoldierFactory sf) {
        this.soldierFactory = sf;
    }

    /**
     * Sets up a slot with a soldier, if there are any empty.
     * Returns true if the operation has success
     * @param soldier SoldierType
     * @return true if success
     */
    public boolean createSoldier(SoldierType soldier) {
        if (soldierSlot1 == null || turnToSlot1 <= 0) {
            soldierSlot1 = soldierFactory.createSoldier(soldier);
            turnToSlot1 = soldierSlot1.getTrainingTime();
            return true;
        }
        else if (soldierSlot2 == null || turnToSlot2 <= 0) {
            soldierSlot2 = soldierFactory.createSoldier(soldier);
            turnToSlot2 = soldierSlot2.getTrainingTime();
            return true;
        }
        return false;
    }

    public int getTurnToSlot1() {
        return turnToSlot1;
    }

    public int getTurnToSlot2() {
        return turnToSlot2;
    }

    public void turnPassed() {
        this.turnToSlot1 = turnToSlot1 - 1;
        this.turnToSlot2 = turnToSlot2 - 1;
    }

    /**
     *
     * @return null if the Soldier isn't ready or not set
     */
    public Soldier getSoldierSlot1() {
        Soldier s = soldierSlot1;
        if (turnToSlot1 == 0){
            soldierSlot1 = null;
            return s;}
        return null;
    }

    /**
     *
     * @return null if the Soldier isn't ready or not set
     */
    public Soldier getSoldierSlot2() {
        Soldier s = soldierSlot2;
        if (turnToSlot2 == 0){
            soldierSlot2 = null;
            return s;}
        return null;
    }
}
