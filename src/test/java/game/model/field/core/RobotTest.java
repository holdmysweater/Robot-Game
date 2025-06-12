package game.model.field.core;

import game.model.field.cell_objects.SmallCellObject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.between_cells_objects.WallSegment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {

    private enum EVENT {ROBOT_MOVED}

    private final List<EVENT> events = new ArrayList<>();
    private final List<EVENT> expectedEvents = new ArrayList<>();

    private class EventsListener implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            events.add(EVENT.ROBOT_MOVED);
        }

        @Override
        public void robotUnfrozenChanged(@NotNull RobotActionEvent event) {

        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }
    }

    private Cell cell;
    private Cell neighborCell;
    private final Direction direction = Direction.NORTH;

    private final static int DEFAULT_TEST_BATTERY_CHARGE = 10;
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;
    private static final int AMOUNT_OF_CHARGE_FOR_SKIP_STEP = 2;

    private Robot robot;

    @BeforeEach
    public void testSetup() {
        // clean events
        events.clear();
        expectedEvents.clear();

        // create robot
        robot = new Robot(new Battery());
        robot.addRobotActionListener(new EventsListener());

        // create field
        cell = new Cell();
        neighborCell = new Cell();

        Map<Direction, Cell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);
        cell.setNeighbors(map);
    }

    @Test
    public void test_canStayAtPosition_emptyCell() {
        assertTrue(robot.canSetPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithRobot() {
        cell.setObject(robot);

        assertFalse(robot.canSetPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithBattery() {
        cell.setObject(new Battery());

        assertTrue(robot.canSetPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndEnoughCharge() {
        cell.setObject(robot);

        robot.setUnfrozen(true);
        robot.move(direction);

        expectedEvents.add(EVENT.ROBOT_MOVED);

        assertEquals(robot, neighborCell.getObject(NonStationaryCellObject.class));
        assertEquals(neighborCell, robot.getPosition());
        assertNull(cell.getObject(NonStationaryCellObject.class));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - AMOUNT_OF_CHARGE_FOR_MOVE, robot.getCharge());
        assertEquals(expectedEvents, events);
    }

    @Test
    public void test_move_noCellInDirectionAndRobotActiveAndEnoughCharge() {
        neighborCell.setObject(robot);

        robot.move(Direction.NORTH);

        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertEquals(neighborCell, robot.getPosition());
        assertEquals(robot, neighborCell.getObject(NonStationaryCellObject.class));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionWithWallAndRobotActiveAndEnoughCharge() {
        cell.setObject(robot);
        cell.setNeighborObstacle(direction, new WallSegment());

        robot.setBattery(new Battery());
        robot.move(direction);

        assertEquals(robot, cell.getObject(NonStationaryCellObject.class));
        assertEquals(cell, robot.getPosition());
        assertNull(neighborCell.getObject(NonStationaryCellObject.class));
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndNotEnoughCharge() {
        cell.setObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));
        robot.move(direction);

        assertEquals(robot, cell.getObject(NonStationaryCellObject.class));
        assertEquals(cell, robot.getPosition());
        assertNull(neighborCell.getObject(NonStationaryCellObject.class));
        assertEquals(0, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellContainsBattery() {
        cell.setObject(robot);
        Battery newBattery = new Battery();
        cell.setObject(newBattery);

        robot.setUnfrozen(true);
        robot.changeBattery();

        assertNull(((Cell) cell).getObject(SmallCellObject.class));
        assertEquals(newBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellNotContainsBattery() {
        cell.setObject(robot);

        Battery robotBattery = new Battery();
        robot.setBattery(robotBattery);

        robot.changeBattery();

        assertEquals(robotBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_robotIsCapable_zeroChargeAndCellWithBattery() {
        Cell cell = new Cell();
        cell.setObject(robot);

        Battery robotBattery = new Battery(0);
        robot.unsetBattery();
        robot.setBattery(robotBattery);

        Battery cellBattery = new Battery();
        cell.setObject(cellBattery);

        assertTrue(robot.isCapable());
    }
}

