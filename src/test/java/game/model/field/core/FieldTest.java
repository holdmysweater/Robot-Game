package game.model.field.core;

import org.jetbrains.annotations.NotNull;
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
        Cell cell_0_0 = field.getCell(new Point(0, 0));
        Cell cell_0_1 = field.getCell(new Point(1, 0));
        Cell cell_1_0 = field.getCell(new Point(0, 1));
        Cell cell_1_1 = field.getCell(new Point(1, 1));

        assertEquals(cell_1_0, cell_0_0.getNeighborCell(Direction.SOUTH));
        assertEquals(cell_1_1, cell_0_1.getNeighborCell(Direction.SOUTH));
        assertEquals(cell_0_1, cell_1_1.getNeighborCell(Direction.NORTH));
        assertEquals(cell_0_0, cell_1_0.getNeighborCell(Direction.NORTH));
        assertEquals(cell_0_1, cell_0_0.getNeighborCell(Direction.EAST));
        assertEquals(cell_1_1, cell_1_0.getNeighborCell(Direction.EAST));
        assertEquals(cell_0_0, cell_0_1.getNeighborCell(Direction.WEST));
        assertEquals(cell_1_0, cell_1_1.getNeighborCell(Direction.WEST));
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
        assertNull(field.getRobot());
    }

    @Test
    public void test_getRobotsOnField_oneRobot() {
        Robot robot = new Robot(new Battery());
        field.getCell(new Point(0, 0)).setObject(NonStationaryCellObject.class, robot);

        assertEquals(robot, field.getRobot());
    }

    @Test
    public void test_TeleportedRobots_oneRobot() {
        Robot robot = new Robot(new Battery());
        ExitCell cell = (ExitCell) field.getCell(new Point(1, 1));
        cell.setObject(NonStationaryCellObject.class, robot);

        assertEquals(robot, cell.getTeleportedRobot());
        assertTrue(robot.isTeleported());
    }

    @Test
    public void test_teleportEvent_oneRobot() {
        int expectedEventCount = 1;
        Robot robot = new Robot(new Battery());

        field.getCell(new Point(1, 1)).setObject(NonStationaryCellObject.class, robot);

        assertEquals(expectedEventCount, eventCount);
    }
}
