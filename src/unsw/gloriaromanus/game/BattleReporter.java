package unsw.gloriaromanus.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/*
 Observer for BattleResolver
 To be instantiated in the controller, and setted in the BattleResolver
 */

public class BattleReporter implements PropertyChangeListener {
    /*
    TODO:
        At the moment it always prints as if you are the attacker.
        It can easily be set to display faction name instead (at the begginning
        of a battle set property
     */

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getNewValue());
    }
}
