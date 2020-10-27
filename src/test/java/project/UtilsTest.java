package project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.unsw.gloriaromanus.Utils.clamp;

public class UtilsTest {
    @Test
    public void clampFromBelowWorksFineTest() {
        assertEquals(0, clamp(0, -20, 4));
    }

    @Test
    public void clampFromAboveWorksFineTest() {
        assertEquals(4.0, clamp(0.0,15.0,4.0));
    }

}
