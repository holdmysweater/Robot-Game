package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.field.between_cells_objects.WallSegment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    private Cell cell;

    @BeforeEach
    public void testSetup() {
        cell = new Cell();
    }

    @Test
    public void test_setRobot_InEmptyCell() {
        Robot robot = new Robot(new Battery());

        cell.setObject(NonStationaryCellObject.class, robot);

        assertEquals(robot, cell.getObject(NonStationaryCellObject.class));
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_takeRobot_FromCellWithRobot() {
        Robot robot = new Robot(new Battery());

        cell.setObject(NonStationaryCellObject.class, robot);

        assertEquals(robot, cell.takeObject(NonStationaryCellObject.class));
        assertNull(robot.getPosition());
        assertNull(cell.getObject(NonStationaryCellObject.class));
    }


    @Test
    public void test_setRobot_ToCellWithRobot() {
        Robot robot = new Robot(new Battery());
        Robot newRobot = new Robot(new Battery());

        cell.setObject(NonStationaryCellObject.class, robot);

        assertFalse(cell.setObject(NonStationaryCellObject.class, newRobot));
        assertEquals(robot, cell.getObject(NonStationaryCellObject.class));
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Test
    public void test_setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        cell.setObject(NonStationaryCellObject.class, robot);

        assertFalse(cell.setObject(NonStationaryCellObject.class, robot));
        assertEquals(robot, cell.getObject(NonStationaryCellObject.class));
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_setNeighborCell() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        Map<Direction, Cell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        Map<Direction, Cell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        Map<Direction, Cell> map2 = new HashMap<>();
        cell.setNeighbors(map);
        map2.put(direction.getOppositeDirection(), cell);

        assertTrue(neighborCell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() {
        Cell neighborCell = new Cell();
        Cell anotherCell = new Cell();
        Direction direction = Direction.NORTH;

        Map<Direction, Cell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        cell.setNeighbors(map);

        Map<Direction, Cell> map2 = new HashMap<>();
        anotherCell.setNeighbors(null);
        map2.put(direction, anotherCell);


        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_alreadyNeighborWithAnotherDirection() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        Map<Direction, Cell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        Map<Direction, Cell> map2 = new HashMap<>();
        map2.put(anotherDirection, neighborCell);

        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        Map<Direction, Cell> map = new HashMap<>();
        map.put(direction, cell);

        assertFalse(cell.setNeighbors(map));
        assertNull(cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() {
        Cell neighborCell = new Cell();
        Direction direction = Direction.NORTH;

        Map<Direction, Cell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellNotExists() {
        assertNull(cell.getNeighborCell(Direction.NORTH));
    }

    @Test
    public void test_setWall_inOneSingleCell() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null);
        cell.setNeighborObstacle(direction, wallSegment);
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InSingleWithSameWallAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighbors(null);
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(Direction.SOUTH, wallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InSingleWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighbors(null);
        cell.setNeighborObstacle(direction, wallSegment);
        assertFalse(cell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_inNeighborCells() {
        Direction direction = Direction.NORTH;
        Cell neighborCell = new Cell();
        Map<Direction, Cell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        cell.setNeighborObstacle(direction, wallSegment);

        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell,wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCell(direction));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        Cell neighborCell = new Cell();
        Map<Direction, Cell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighborObstacle(direction, wallSegment);

        assertFalse(cell.setNeighborObstacle(direction, anotherWallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell, wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCell(direction));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        Cell neighborCell = new Cell();
        Map<Direction, Cell> neighborCells = new HashMap<>();
        neighborCells.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(neighborCells);
        WallSegment wallSegment = new WallSegment();
        Direction anotherDirection = direction.getOppositeDirection();

        cell.setNeighborObstacle(direction, wallSegment);

        assertFalse(cell.setNeighborObstacle(anotherDirection, wallSegment));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell,wallSegment.getPosition().getNeighborCell(direction.getOppositeDirection()));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCell(direction));
    }

    @Test
    public void test_neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(cell.getNeighborArea(direction));
    }

    @Test
    public void test_setBattery_inEmptyCell() {
        Battery battery = new Battery();

        cell.setObject(SmallCellObject.class, battery);
        assertEquals(battery, cell.getObject(SmallCellObject.class));
    }

    @Test
    public void test_setBattery_inCell() {
        Battery battery = new Battery();

        cell.setObject(SmallCellObject.class, battery);
        Battery anotherBattery = new Battery();

        assertFalse(cell.setObject(SmallCellObject.class, anotherBattery));
    }

    @Test
    public void test_setBattery_alreadySetBatteryToAnotherCell() {
        Battery battery = new Battery();

        cell.setObject(SmallCellObject.class, battery);

        Cell anotherCell = new Cell();

        assertFalse(anotherCell.setObject(SmallCellObject.class, battery));
    }

    @Test
    public void test_takeBattery_fromCell(){
        Battery battery = new Battery();

        cell.setObject(SmallCellObject.class, battery);

        assertEquals(battery, cell.takeObject(SmallCellObject.class));
        assertNull(cell.getObject(SmallCellObject.class));
        assertNull(battery.getPosition());
    }

    @Test
    public void test_takeBattery_fromCellWithoutBattery() {
        assertNull(cell.takeObject(SmallCellObject.class));
    }

    @Test
    public void test_setExit_inEmptyCell() {
        ExitCell exitCell = new ExitCell();

        cell.setObject(InteractiveCellObject.class, exitCell);
        assertEquals(exitCell, cell.getObject(InteractiveCellObject.class));
    }

    @Test
    public void test_setExit_inCell() {
        ExitCell exitCell = new ExitCell();

        cell.setObject(InteractiveCellObject.class, exitCell);
        ExitCell anotherExitCell = new ExitCell();

        assertFalse(cell.setObject(InteractiveCellObject.class, anotherExitCell));
    }

    @Test
    public void test_setExit_alreadySetExitToAnotherCell() {
        ExitCell exitCell = new ExitCell();

        cell.setObject(InteractiveCellObject.class, exitCell);

        Cell anotherCell = new Cell();

        assertFalse(anotherCell.setObject(InteractiveCellObject.class, exitCell));
    }

    @Test
    public void test_takeExit_fromCell() {
        ExitCell exitCell = new ExitCell();

        cell.setObject(InteractiveCellObject.class, exitCell);

        assertEquals(exitCell, cell.takeObject(InteractiveCellObject.class));
        assertNull(cell.getObject(InteractiveCellObject.class));
        assertNull(exitCell.getPosition());
    }

    @Test
    public void test_takeExit_fromCellWithoutExit() {
        assertNull(cell.takeObject(InteractiveCellObject.class));
    }

    @Test
    public void test_setHole_inEmptyCell() {
        Hole hole = new Hole();

        cell.setObject(NonInteractiveCellObject.class, hole);
        assertEquals(hole, cell.getObject(NonInteractiveCellObject.class));
    }

    @Test
    public void test_setHole_inCell() {
        Hole hole = new Hole();

        cell.setObject(NonInteractiveCellObject.class, hole);
        Hole anotherHole = new Hole();

        assertFalse(cell.setObject(NonInteractiveCellObject.class, anotherHole));
    }

    @Test
    public void test_setHole_alreadySetHoleToAnotherCell() {
        Hole hole = new Hole();

        cell.setObject(NonInteractiveCellObject.class, hole);

        Cell anotherCell = new Cell();

        assertFalse(anotherCell.setObject(NonInteractiveCellObject.class, hole));
    }

    @Test
    public void test_takeHole_fromCell() {
        Hole hole = new Hole();

        cell.setObject(NonInteractiveCellObject.class, hole);

        assertEquals(hole, cell.takeObject(NonInteractiveCellObject.class));
        assertNull(cell.getObject(NonInteractiveCellObject.class));
        assertNull(hole.getPosition());
    }

    @Test
    public void test_takeHole_fromCellWithoutHole() {
        assertNull(cell.takeObject(NonInteractiveCellObject.class));
    }

    // Tests for SmallCellObject
    @Test
    void test_canSetObject_SmallCellObject_withSmallCellObject() {
        Battery smallObject = new Battery();
        cell.setObject(SmallCellObject.class, smallObject);
        assertFalse(cell.canSetObject(SmallCellObject.class));
    }

    @Test
    void test_canSetObject_SmallCellObject_withNonStationaryObject() {
        Robot nonStationaryObject = new Robot(new Battery());
        cell.setObject(NonStationaryCellObject.class, nonStationaryObject);
        assertTrue(cell.canSetObject(SmallCellObject.class));
    }

    @Test
    void test_canSetObject_SmallCellObject_withInteractiveObject() {
        ExitCell interactiveObject = new ExitCell();
        cell.setObject(InteractiveCellObject.class, interactiveObject);
        assertFalse(cell.canSetObject(SmallCellObject.class));
    }

    @Test
    void test_canSetObject_SmallCellObject_withNonInteractiveObject() {
        Hole nonInteractiveObject = new Hole();
        cell.setObject(NonInteractiveCellObject.class, nonInteractiveObject);
        assertFalse(cell.canSetObject(SmallCellObject.class));
    }

    // Tests for NonStationaryCellObject
    @Test
    void test_canSetObject_NonStationaryObject_withSmallCellObject() {
        Battery smallObject = new Battery();
        cell.setObject(SmallCellObject.class, smallObject);
        assertTrue(cell.canSetObject(NonStationaryCellObject.class));
    }

    @Test
    void test_canSetObject_NonStationaryObject_withNonStationaryObject() {
        Robot nonStationaryObject = new Robot(new Battery());
        cell.setObject(NonStationaryCellObject.class, nonStationaryObject);
        assertFalse(cell.canSetObject(NonStationaryCellObject.class));
    }

    @Test
    void test_canSetObject_NonStationaryObject_withInteractiveObject() {
        ExitCell interactiveObject = new ExitCell();
        cell.setObject(InteractiveCellObject.class, interactiveObject);
        assertTrue(cell.canSetObject(NonStationaryCellObject.class));
    }

    @Test
    void test_canSetObject_NonStationaryObject_withNonInteractiveObject() {
        Hole nonInteractiveObject = new Hole();
        cell.setObject(NonInteractiveCellObject.class, nonInteractiveObject);
        assertFalse(cell.canSetObject(NonStationaryCellObject.class));
    }

    // Tests for InteractiveCellObject
    @Test
    void test_canSetObject_InteractiveObject_withSmallCellObject() {
        Battery smallObject = new Battery();
        cell.setObject(SmallCellObject.class, smallObject);
        assertFalse(cell.canSetObject(InteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_InteractiveObject_withNonStationaryObject() {
        Robot nonStationaryObject = new Robot(new Battery());
        cell.setObject(NonStationaryCellObject.class, nonStationaryObject);
        assertTrue(cell.canSetObject(InteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_InteractiveObject_withInteractiveObject() {
        ExitCell interactiveObject = new ExitCell();
        cell.setObject(InteractiveCellObject.class, interactiveObject);
        assertFalse(cell.canSetObject(InteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_InteractiveObject_withNonInteractiveObject() {
        Hole nonInteractiveObject = new Hole();
        cell.setObject(NonInteractiveCellObject.class, nonInteractiveObject);
        assertFalse(cell.canSetObject(InteractiveCellObject.class));
    }

    // Tests for NonInteractiveCellObject
    @Test
    void test_canSetObject_NonInteractiveObject_withSmallCellObject() {
        Battery smallObject = new Battery();
        cell.setObject(SmallCellObject.class, smallObject);
        assertFalse(cell.canSetObject(NonInteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_NonInteractiveObject_withNonStationaryObject() {
        Robot nonStationaryObject = new Robot(new Battery());
        cell.setObject(NonStationaryCellObject.class, nonStationaryObject);
        assertFalse(cell.canSetObject(NonInteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_NonInteractiveObject_withInteractiveObject() {
        ExitCell interactiveObject = new ExitCell();
        cell.setObject(InteractiveCellObject.class, interactiveObject);
        assertFalse(cell.canSetObject(NonInteractiveCellObject.class));
    }

    @Test
    void test_canSetObject_NonInteractiveObject_withNonInteractiveObject() {
        Hole nonInteractiveObject = new Hole();
        cell.setObject(NonInteractiveCellObject.class, nonInteractiveObject);
        assertFalse(cell.canSetObject(NonInteractiveCellObject.class));
    }
}