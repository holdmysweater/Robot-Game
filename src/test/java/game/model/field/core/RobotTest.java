package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.between_cells_objects.WallSegment;

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

    private AbstractCell abstractCell;
    private AbstractCell neighborAbstractCell;
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
        abstractCell = new NormalCell();
        neighborAbstractCell = new NormalCell();
        abstractCell.setNeighbor(neighborAbstractCell, direction);
    }

    @Test
    public void test_setUnfrozenAndIsActive() {
        robot.setUnfrozen(true);

        assertTrue(robot.isUnfrozen());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_emptyCell() {
        assertTrue(robot.canSetPosition(abstractCell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithRobot() {
        abstractCell.setBigObject(robot);

        assertFalse(robot.canSetPosition(abstractCell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_canStayAtPosition_cellWithBattery() {
        ((NormalCell) abstractCell).setSmallObject(new Battery());

        assertTrue(robot.canSetPosition(abstractCell));
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndEnoughCharge() {
        abstractCell.setBigObject(robot);

        robot.move(direction);

        expectedEvents.add(EVENT.ROBOT_MOVED);

        assertEquals(robot, neighborAbstractCell.getBigObject());
        assertEquals(neighborAbstractCell, robot.getPosition());
        assertNull(abstractCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE - AMOUNT_OF_CHARGE_FOR_MOVE, robot.getCharge());
        assertEquals(expectedEvents, events);
    }

    @Test
    public void test_move_noCellInDirectionAndRobotActiveAndEnoughCharge() {
        neighborAbstractCell.setBigObject(robot);

        robot.move(Direction.NORTH);

        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertEquals(neighborAbstractCell, robot.getPosition());
        assertEquals(robot, neighborAbstractCell.getBigObject());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionWithWallAndRobotActiveAndEnoughCharge() {
        abstractCell.setBigObject(robot);
        abstractCell.setNeighborObstacle(new WallSegment(), abstractCell.getNeighborDirection(neighborAbstractCell));

        robot.setBattery(new Battery());
        robot.move(direction);

        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
        assertNull(neighborAbstractCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotNotActiveAndEnoughCharge() {
        abstractCell.setBigObject(robot);

        robot.setUnfrozen(false);
        robot.move(direction);

        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
        assertNull(neighborAbstractCell.getBigObject());
        assertEquals(DEFAULT_TEST_BATTERY_CHARGE, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_move_emptyCellInDirectionAndRobotActiveAndNotEnoughCharge() {
        abstractCell.setBigObject(robot);

        robot.unsetBattery();
        robot.setBattery(new Battery(0));
        robot.move(direction);

        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
        assertNull(neighborAbstractCell.getBigObject());
        assertEquals(0, robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellContainsBattery() {
        abstractCell.setBigObject(robot);
        Battery newBattery = new Battery();
        ((NormalCell) abstractCell).setSmallObject(newBattery);

        robot.changeBattery();

        assertNull(((NormalCell) abstractCell).getSmallObject());
        assertEquals(newBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsNotActiveCellContainsBattery() {
        abstractCell.setBigObject(robot);
        Battery newBattery = new Battery();
        ((NormalCell) abstractCell).setSmallObject(newBattery);

        Battery robotBattery = new Battery();
        robot.setBattery(robotBattery);
        robot.setUnfrozen(false);

        robot.changeBattery();

        assertEquals(newBattery, ((NormalCell) abstractCell).getSmallObject());
        assertEquals(robotBattery.getCharge(), robot.getCharge());
        assertTrue(events.isEmpty());
    }

    @Test
    public void test_changeBattery_robotIsActiveCellNotContainsBattery() {
        abstractCell.setBigObject(robot);

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

