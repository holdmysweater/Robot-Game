package game.model.field;

import game.model.field.cell_objects.Battery;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.Direction;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.cell_objects.Robot;

import java.util.ArrayList;
import java.util.List;

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
            // Not implemented yet
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
        robot.setUnfrozen(true);
        robot.addRobotActionListener(new EventsListener());

        // create field
        cell = new NormalCell();
        neighborCell = new NormalCell();
        cell.setNeighbor(neighborCell, direction);
    }

    @Test
    public void test_setUnfrozenAndIsActive() {
        robot.setUnfrozen(true);

        assertTrue(robot.isUnfrozen());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_emptyCell() {
        assertTrue(robot.canLocateAtPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithRobot() {
        cell.setBigObject(robot);

        assertFalse(robot.canLocateAtPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithBattery() {
        ((NormalCell) cell).setSmallObject(new Battery());

        assertTrue(robot.canLocateAtPosition(cell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndEnoughCharge() {
        cell.setBigObject(robot);

        robot.move(direction);

        expectedEvents.add(EVENT.ROBOT_MOVED);

        assertEquals(robot, neighborCell.getBigObject());
        assertEquals(neighborCell, robot.getPosition());
        assertNull(cell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - AMOUNT_OF_CHARGE_FOR_MOVE, robot.getCharge());
        assertEquals(expectedEvents, events);
    }

    @Test
    public void test_move_noCellInDirectionAndRobotActiveAndEnoughCharge() {
        neighborCell.setBigObject(robot);

        robot.move(Direction.NORTH);

        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertEquals(neighborCell, robot.getPosition());
        assertEquals(robot, neighborCell.getBigObject());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionWithWallAndRobotActiveAndEnoughCharge() {
        cell.setBigObject(robot);
        cell.setBetweenCellObject(new WallSegment(), cell.getNeighborDirection(neighborCell));

        robot.setBattery(new Battery());
        robot.move(direction);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(neighborCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotNotActiveAndEnoughCharge() {
        cell.setBigObject(robot);

        robot.setUnfrozen(false);
        robot.move(direction);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(neighborCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndNotEnoughCharge() {
        cell.setBigObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));
        robot.move(direction);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(neighborCell.getBigObject());
        assertEquals(0, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellContainsBattery() {
        cell.setBigObject(robot);
        Battery newBattery = new Battery();
        ((NormalCell) cell).setSmallObject(newBattery);

        robot.changeBattery();

        assertNull(((NormalCell) cell).getSmallObject());
        assertEquals(newBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsNotActiveCellContainsBattery() {
        cell.setBigObject(robot);
        Battery newBattery = new Battery();
        ((NormalCell) cell).setSmallObject(newBattery);

        Battery robotBattery = new Battery();
        robot.setBattery(robotBattery);
        robot.setUnfrozen(false);

        robot.changeBattery();

        assertEquals(newBattery, ((NormalCell) cell).getSmallObject());
        assertEquals(robotBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellNotContainsBattery() {
        cell.setBigObject(robot);

        Battery robotBattery = new Battery();
        robot.setBattery(robotBattery);

        robot.changeBattery();

        assertEquals(robotBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_robotIsCapable_zeroChargeAndCellWithBattery() {
        NormalCell cell = new NormalCell();
        cell.setBigObject(robot);

        Battery robotBattery = new Battery(0);
        robot.unsetBattery();
        robot.setBattery(robotBattery);

        Battery cellBattery = new Battery();
        cell.setSmallObject(cellBattery);

        assertTrue(robot.isCapable());
    }
}

