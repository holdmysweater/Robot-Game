package game.model.field.cells;

import game.model.field.ExitCell;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.ExitCellActionEvent;
import game.model.events.ExitCellActionListener;
import game.model.field.cell_objects.Robot;
import game.model.field.cell_objects.Battery;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ExitCellTest {

    private ExitCell exitCell;
    private Robot robot;

    private int countEvents = 0;

    private class EventListener implements ExitCellActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitCellActionEvent event) {
            countEvents += 1;
        }
    }

    @BeforeEach
    public void testSetup() {
        // Clear events count
        countEvents = 0;

        // setting up robot
        robot = new Robot(new Battery());

        exitCell = new ExitCell();
        exitCell.addExitCellActionListener(new EventListener());
    }

    @Test
    public void test_setRobot_oneRobot() {
        exitCell.setBigObject(robot);

        int expectedCountEvents = 1;

        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_setRobot_setTeleportedRobot() {
        exitCell.setBigObject(robot);

        int expectedCountEvents = 1;

        assertFalse(exitCell.setBigObject(robot));
        assertEquals(expectedCountEvents, countEvents);
        assertEquals(robot, exitCell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_getTeleportedRobots_empty() {
        assertNull(exitCell.getTeleportedRobot());
    }
}
