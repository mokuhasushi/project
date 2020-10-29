package unsw.gloriaromanus.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/*
 Observer for BattleResolver
 To be instantiated in the controller, and setted in the BattleResolver
 */

public class BattleReporter implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getNewValue());
    }
}
