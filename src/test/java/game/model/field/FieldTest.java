package game.model.field;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.Direction;
import game.model.Point;
import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;
import game.model.field.cell_objects.Robot;
import game.model.field.cell_objects.Battery;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private int eventCount = 0;

    class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            eventCount += 1;
        }
    }

    private Field field;

    @BeforeEach
    public void testSetup() {
        eventCount = 0;
        field = new Field(2, 2, new Point(1, 1));
        field.addFieldActionListener(new FieldObserver());
    }

    @Test
    public void test_create_withCorrectParams() {
        Cell cell_0_0 = field.getCell(new Point(0, 0));
        Cell cell_0_1 = field.getCell(new Point(1, 0));
        Cell cell_1_0 = field.getCell(new Point(0, 1));
        Cell cell_1_1 = field.getCell(new Point(1, 1));

        Assertions.assertEquals(Direction.SOUTH, cell_0_0.getNeighborDirection(cell_1_0));
        assertEquals(Direction.SOUTH, cell_0_1.getNeighborDirection(cell_1_1));
        assertEquals(Direction.NORTH, cell_1_1.getNeighborDirection(cell_0_1));
        assertEquals(Direction.NORTH, cell_1_0.getNeighborDirection(cell_0_0));
        assertEquals(Direction.EAST, cell_0_0.getNeighborDirection(cell_0_1));
        assertEquals(Direction.EAST, cell_1_0.getNeighborDirection(cell_1_1));
        assertEquals(Direction.WEST, cell_0_1.getNeighborDirection(cell_0_0));
        assertEquals(Direction.WEST, cell_1_1.getNeighborDirection(cell_1_0));
        assertTrue(cell_1_1 instanceof ExitCell);
    }

    @Test
    public void test_create_withNegativeWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(-1, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Field(0, 1, new Point(0, 0)));
    }

    @Test
    public void test_create_withNegativeHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, -1, new Point(0, 0)));
    }

    @Test
    public void test_create_withZeroHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 0, new Point(0, 0)));
    }

    @Test
    public void test_create_withIncorrectExitPoint() {
        assertThrows(IllegalArgumentException.class, () -> new Field(1, 1, new Point(2, 2)));
    }

    @Test
    public void test_getRobotsOnField_empty() {
        assertNull(field.getRobotOnField());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() {
        Robot robot = new Robot(new Battery());
        field.getCell(new Point(0, 0)).setBigObject(robot);

        assertEquals(robot, field.getRobotOnField());
    }

    @Test
    public void test_TeleportedRobots_oneRobot() {
        Robot robot = new Robot(new Battery());
        ExitCell cell = (ExitCell) field.getCell(new Point(1, 1));
        cell.setBigObject(robot);

        assertEquals(robot, cell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_teleportEvent_oneRobot() {
        int expectedEventCount = 1;
        Robot robot = new Robot(new Battery());

        field.getCell(new Point(1, 1)).setBigObject(robot);

        assertEquals(expectedEventCount, eventCount);
    }
}
