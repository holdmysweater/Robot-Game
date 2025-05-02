package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.field.between_cells_objects.WallSegment;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCellTest {

    private AbstractCell cell;

    public AbstractCellTest() {
    }


    @BeforeEach
    public void testSetup() {

        cell = new NormalCell();
    }

    @Test
    public void test_setRobot_InEmptyCell() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_takeRobot_FromCellWithRobot() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertEquals(robot, cell.takeBigObject());
        assertNull(robot.getPosition());
        assertNull(cell.getBigObject());
    }


    @Test
    public void test_setRobot_ToCellWithRobot() {
        Robot robot = new Robot(new Battery());
        Robot newRobot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(newRobot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Test
    public void test_setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertFalse(cell.setBigObject(robot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_setNeighborCell() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        cell.setNeighbors(map);
        map2.put(direction.getOppositeDirection(), cell);

        assertTrue(neighborCell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() {
        AbstractCell neighborCell = new NormalCell();
        AbstractCell anotherCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        neighborCell.setNeighbors(null);
        map.put(direction, neighborCell);

        cell.setNeighbors(map);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        anotherCell.setNeighbors(null);
        map2.put(direction, anotherCell);


        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_alreadyNeighborWithAnotherDirection() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, neighborCell);

        neighborCell.setNeighbors(null);
        cell.setNeighbors(map);

        Map<Direction, AbstractCell> map2 = new HashMap<>();
        map2.put(anotherDirection, neighborCell);

        assertFalse(cell.setNeighbors(map2));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
        map.put(direction, cell);

        assertFalse(cell.setNeighbors(map));
        assertNull(cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        Map<Direction, AbstractCell> map = new HashMap<>();
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
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
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
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
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
        AbstractCell neighborCell = new NormalCell();
        Map<Direction, AbstractCell> neighborCells = new HashMap<>();
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
}