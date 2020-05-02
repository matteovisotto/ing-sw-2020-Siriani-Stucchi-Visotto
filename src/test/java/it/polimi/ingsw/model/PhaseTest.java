package it.polimi.ingsw.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

    public class BlocksTest {

        @Rule
        public final ExpectedException exception = ExpectedException.none();

        @Test
        public void getPhaseId() {
            assertEquals(-4, Phase.WAIT_PLAYERS.getPhaseId());
            assertEquals(-3, Phase.DRAWCARD.getPhaseId());
            assertEquals(-2, Phase.SETWORKER1.getPhaseId());
            assertEquals(-1, Phase.SETWORKER2.getPhaseId());
            assertEquals(0, Phase.MOVE.getPhaseId());
            assertEquals(1, Phase.BUILD.getPhaseId());
        }

        @Test
        public void getPhase() {
            assertEquals(Phase.getPhase(-4), Phase.WAIT_PLAYERS);
            assertEquals(Phase.getPhase(-3), Phase.DRAWCARD);
            assertEquals(Phase.getPhase(-2), Phase.SETWORKER1);
            assertEquals(Phase.getPhase(-1), Phase.SETWORKER2);
            assertEquals(Phase.getPhase(0), Phase.MOVE);
            assertEquals(Phase.getPhase(1), Phase.BUILD);
        }

        @Test
        public void getPhaseException(){
            exception.expect(IllegalArgumentException.class);
            Phase.getPhase(10);
        }

        @Test
        public void testNext() {
            Phase phase = Phase.WAIT_PLAYERS;
            Phase.next(phase);
            assertEquals(Phase.getPhase(-3), phase);
            Phase.next(phase);
            assertEquals(Phase.getPhase(-2), phase);
            Phase.next(phase);
            assertEquals(Phase.getPhase(-1), phase);
            Phase.next(phase);
            assertEquals(Phase.getPhase(0), phase);
            Phase.next(phase);
            assertEquals(Phase.getPhase(1), phase);
            Phase.next(phase);
            assertEquals(Phase.getPhase(0), phase);
        }
    }
}