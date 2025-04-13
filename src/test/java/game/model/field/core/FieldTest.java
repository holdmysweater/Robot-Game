package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;

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
        AbstractCell abstractCell_0_0 = field.getCell(new Point(0, 0));
        AbstractCell abstractCell_0_1 = field.getCell(new Point(1, 0));
        AbstractCell abstractCell_1_0 = field.getCell(new Point(0, 1));
        AbstractCell abstractCell_1_1 = field.getCell(new Point(1, 1));

        Assertions.assertEquals(Direction.SOUTH, abstractCell_0_0.getNeighborDirection(abstractCell_1_0));
        assertEquals(Direction.SOUTH, abstractCell_0_1.getNeighborDirection(abstractCell_1_1));
        assertEquals(Direction.NORTH, abstractCell_1_1.getNeighborDirection(abstractCell_0_1));
        assertEquals(Direction.NORTH, abstractCell_1_0.getNeighborDirection(abstractCell_0_0));
        assertEquals(Direction.EAST, abstractCell_0_0.getNeighborDirection(abstractCell_0_1));
        assertEquals(Direction.EAST, abstractCell_1_0.getNeighborDirection(abstractCell_1_1));
        assertEquals(Direction.WEST, abstractCell_0_1.getNeighborDirection(abstractCell_0_0));
        assertEquals(Direction.WEST, abstractCell_1_1.getNeighborDirection(abstractCell_1_0));
        assertTrue(abstractCell_1_1 instanceof ExitCell);
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
        assertNull(field.getRobot());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() {
        Robot robot = new Robot(new Battery());
        field.getCell(new Point(0, 0)).setBigObject(robot);

        assertEquals(robot, field.getRobot());
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
