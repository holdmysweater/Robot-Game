package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.ExitPointActionEvent;
import game.model.events.ExitPointActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class ExitPointTest {

    private ExitPoint exitPoint;
    private Robot robot;

    private int countEvents = 0;

    private class EventListener implements ExitPointActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitPointActionEvent event) {
            countEvents += 1;
        }
    }

    @BeforeEach
    public void testSetup() {
        // Clear events count
        countEvents = 0;

        // setting up robot
        robot = new Robot(new Battery());

        exitPoint = new ExitPoint();
        exitPoint.addExitPointActionListener(new EventListener());
    }

    @Test
    public void test_setRobot_oneRobot() {
        exitPoint.execute(robot);

        int expectedCountEvents = 1;

        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitPoint.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_setRobot_setTeleportedRobot() {
        exitPoint.execute(robot);

        int expectedCountEvents = 1;

        exitPoint.execute(new Robot(new Battery()));

        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitPoint.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_getTeleportedRobots_empty() {
        assertNull(exitPoint.getTeleportedRobot());
    }
}
