package game.model.field.core;

import game.model.field.core.AbstractCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import game.model.field.core.Direction;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.core.Robot;
import game.model.field.core.Battery;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCellTest {

    private AbstractCell abstractCell;

    public AbstractCellTest() {
    }


    @BeforeEach
    public void testSetup() {

        abstractCell = new AbstractCellTestModel();
    }

    @Test
    public void test_setRobot_InEmptyCell() {
        Robot robot = new Robot(new Battery());

        abstractCell.setBigObject(robot);

        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
    }

    @Test
    public void test_takeRobot_FromCellWithRobot() {
        Robot robot = new Robot(new Battery());

        abstractCell.setBigObject(robot);

        assertEquals(robot, abstractCell.takeBigObject());
        assertNull(robot.getPosition());
        assertNull(abstractCell.getBigObject());
    }


    @Test
    public void test_setRobot_ToCellWithRobot() {
        Robot robot = new Robot(new Battery());
        Robot newRobot = new Robot(new Battery());

        abstractCell.setBigObject(robot);

        assertThrows(IllegalArgumentException.class, () -> abstractCell.setBigObject(newRobot));
        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
        assertNull(newRobot.getPosition());
    }

    @Test
    public void test_setRobot_ToCellAgain() {
        Robot robot = new Robot(new Battery());

        abstractCell.setBigObject(robot);

        assertThrows(IllegalArgumentException.class, () -> abstractCell.setBigObject(robot));
        assertEquals(robot, abstractCell.getBigObject());
        assertEquals(abstractCell, robot.getPosition());
    }

    @Test
    public void test_setNeighborCell() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        Direction direction = Direction.NORTH;

        abstractCell.setNeighbor(neighborAbstractCell, direction);

        assertEquals(neighborAbstractCell, abstractCell.getNeighborCell(direction));
        assertEquals(abstractCell, neighborAbstractCell.getNeighborCell(direction.getOppositeDirection()));
    }


    @Test
    public void test_setNeighborCell_doubleSided() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        Direction direction = Direction.NORTH;

        abstractCell.setNeighbor(neighborAbstractCell, direction);
        assertThrows(IllegalArgumentException.class, () -> neighborAbstractCell.setNeighbor(abstractCell, direction.getOppositeDirection()));
        assertEquals(neighborAbstractCell, abstractCell.getNeighborCell(direction));
        assertEquals(abstractCell, neighborAbstractCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_twoTimesInOneDirection() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        AbstractCell anotherAbstractCell = new AbstractCellTestModel();
        Direction direction = Direction.NORTH;

        abstractCell.setNeighbor(neighborAbstractCell, direction);
        assertThrows(IllegalArgumentException.class, () -> abstractCell.setNeighbor(anotherAbstractCell, direction));
        assertEquals(neighborAbstractCell, abstractCell.getNeighborCell(direction));
        assertEquals(abstractCell, neighborAbstractCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_alreadyNeighborWithAnotherDirection() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        Direction direction = Direction.NORTH;
        Direction anotherDirection = Direction.SOUTH;

        abstractCell.setNeighbor(neighborAbstractCell, direction);
        assertThrows(IllegalArgumentException.class, () -> abstractCell.setNeighbor(neighborAbstractCell, anotherDirection));
        assertEquals(neighborAbstractCell, abstractCell.getNeighborCell(direction));
        assertEquals(abstractCell, neighborAbstractCell.getNeighborCell(direction.getOppositeDirection()));
    }

    @Test
    public void test_setNeighborCell_setSelfAsNeighbor() {
        Direction direction = Direction.NORTH;

        assertThrows(IllegalArgumentException.class, () -> abstractCell.setNeighbor(abstractCell, direction));
        assertNull(abstractCell.getNeighborCell(direction));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellExists() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        Direction direction = Direction.NORTH;

        abstractCell.setNeighbor(neighborAbstractCell, direction);
        assertEquals(direction, abstractCell.getNeighborDirection(neighborAbstractCell));
    }

    @Test
    public void test_isNeighbor_WhenNeighborCellNotExists() {
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();

        assertNull(abstractCell.getNeighborDirection(neighborAbstractCell));
    }

    @Test
    public void test_setWall_inOneSingleCell() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        abstractCell.setBetweenCellObject(wallSegment, direction);
        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_InSingleWithSameWallAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();

        abstractCell.setBetweenCellObject(wallSegment, direction);
        assertFalse(abstractCell.setBetweenCellObject(wallSegment, Direction.SOUTH));
        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_InSingleWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        abstractCell.setBetweenCellObject(wallSegment, direction);
        assertFalse(abstractCell.setBetweenCellObject(anotherWallSegment, direction));
        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
    }

    @Test
    public void test_setWall_inNeighborCells() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        WallSegment wallSegment = new WallSegment();
        abstractCell.setNeighbor(neighborAbstractCell, direction);

        abstractCell.setBetweenCellObject(wallSegment, direction);

        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborAbstractCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborAbstractCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameDirectionAndAnotherWallSegment() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        WallSegment wallSegment = new WallSegment();
        WallSegment anotherWallSegment = new WallSegment();

        abstractCell.setBetweenCellObject(wallSegment, direction);

        assertFalse(abstractCell.setBetweenCellObject(anotherWallSegment, direction));
        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborAbstractCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborAbstractCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_setWall_InNeighborCellsWithSameWallSegmentAndAnotherDirection() {
        Direction direction = Direction.NORTH;
        AbstractCell neighborAbstractCell = new AbstractCellTestModel();
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        WallSegment wallSegment = new WallSegment();
        Direction anotherDirection = direction.getOppositeDirection();

        abstractCell.setBetweenCellObject(wallSegment, direction);

        assertFalse(abstractCell.setBetweenCellObject(wallSegment, anotherDirection));
        assertEquals(wallSegment, abstractCell.getNeighborObstacle(direction));
        assertEquals(wallSegment, neighborAbstractCell.getNeighborObstacle(direction.getOppositeDirection()));
        assertEquals(abstractCell,wallSegment.getPosition().getNeighborCells().get(direction));
        assertEquals(neighborAbstractCell,wallSegment.getPosition().getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_neighborWall_wallNotExists() {
        Direction direction = Direction.NORTH;

        assertNull(abstractCell.getNeighborObstacle(direction));
    }
}