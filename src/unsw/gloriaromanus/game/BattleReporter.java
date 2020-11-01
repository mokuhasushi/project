package unsw.gloriaromanus.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.OutputStream;
import java.io.PrintStream;

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
    private PrintStream out;

    public BattleReporter(PrintStream out) {
        this.out = out;
    }
    public BattleReporter() {
        this.out = System.out;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        out.println(evt.getNewValue());
    }
}
