package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.field.between_cells_objects.WallSegment;

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

        assertThrows(IllegalArgumentException.class, () -> cell.setBigObject(newRobot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Test
    public void test_setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        cell.setBigObject(robot);

        assertThrows(IllegalArgumentException.class, () -> cell.setBigObject(robot));
        assertEquals(robot, cell.getBigObject());
        assertEquals(cell, robot.getPosition());
    }

    @Test
    public void test_setNeighborCell() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);

        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        assertThrows(IllegalArgumentException.class, () -> neighborCell.setNeighbor(cell, direction.getOppositeDirection()));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() {
        AbstractCell neighborCell = new NormalCell();
        AbstractCell anotherCell = new NormalCell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        assertThrows(IllegalArgumentException.class, () -> cell.setNeighbor(anotherCell, direction));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_alreadyNeighborWithAnotherDirection() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        cell.setNeighbor(neighborCell, direction);
        assertThrows(IllegalArgumentException.class, () -> cell.setNeighbor(neighborCell, anotherDirection));
        assertEquals(neighborCell, cell.getNeighborCell(direction));
        assertEquals(cell, neighborCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        assertThrows(IllegalArgumentException.class, () -> cell.setNeighbor(cell, direction));
        assertNull(cell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() {
        AbstractCell neighborCell = new NormalCell();
        Direction direction = Direction.NORTH;

        cell.setNeighbor(neighborCell, direction);
        assertEquals(direction, cell.getNeighborDirection(neighborCell));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellNotExists() {
        AbstractCell neighborCell = new NormalCell();

        assertNull(cell.getNeighborDirection(neighborCell));
    }

    @Test
    public void test_setWall_inOneSingleCell() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighborObstacle(wallSegment, direction);
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_InSingleWithSameWallAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        cell.setNeighborObstacle(wallSegment, direction);
        assertFalse(cell.setNeighborObstacle(wallSegment, Direction.SOUTH));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_InSingleWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighborObstacle(wallSegment, direction);
        assertFalse(cell.setNeighborObstacle(anotherWallSegment, direction));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_inNeighborCells() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        WallSegment wallSegment = new WallSegment();
        cell.setNeighbor(neighborCell, direction);

        cell.setNeighborObstacle(wallSegment, direction);

        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        cell.setNeighbor(neighborCell, direction);
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        cell.setNeighborObstacle(wallSegment, direction);

        assertFalse(cell.setNeighborObstacle(anotherWallSegment, direction));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborCell = new NormalCell();
        cell.setNeighbor(neighborCell, direction);
        WallSegment wallSegment = new WallSegment();
        Direction anotherDirection = direction.getOppositeDirection();

        cell.setNeighborObstacle(wallSegment, direction);

        assertFalse(cell.setNeighborObstacle(wallSegment, anotherDirection));
        assertEquals(wallSegment, cell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(cell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(cell.getNeighborObstacle(direction));
    }
}