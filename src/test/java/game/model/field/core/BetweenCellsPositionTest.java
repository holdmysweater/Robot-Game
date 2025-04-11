package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetweenCellsPositionTest {

    private final Direction direction = Direction.NORTH;

    private AbstractCell abstractCell;
    private AbstractCell neighborAbstractCell;

    @BeforeEach
    public void testSetup() {
        abstractCell = new NormalCell();
        neighborAbstractCell = new NormalCell();
    }

    @Test
    public void test_createAndGetNeighborCells_withCellAndDirectionOnSingleCell() {
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, direction);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(direction));
    }

    @Test
    public void test_createAndGetNeighborCells_withCellAndDirectionOnTwoNeighborCells() {
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, direction);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(direction));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_withTwoNeighborsCells() {
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(direction.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(direction));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnSingleCell() {
        Direction north = Direction.NORTH;
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, north);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(north));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnSingleCell() {
        Direction south = Direction.SOUTH;
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, south);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(south));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnSingleCell() {
        Direction east = Direction.EAST;
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, east);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(east));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnSingleCell() {
        Direction west = Direction.WEST;
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, west);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(west));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnTwoNeighborCells() {
        Direction north = Direction.NORTH;
        abstractCell.setNeighbor(neighborAbstractCell, north);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(north.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(north));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnTwoNeighborCells() {
        Direction south = Direction.SOUTH;
        abstractCell.setNeighbor(neighborAbstractCell, south);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(south.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(south));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnTwoNeighborCells() {
        Direction east = Direction.EAST;
        abstractCell.setNeighbor(neighborAbstractCell, east);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(east.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(east));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnTwoNeighborCells() {
        Direction west = Direction.WEST;
        abstractCell.setNeighbor(neighborAbstractCell, west);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(west.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(west));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnTwoNeighborCellsByCellAndDirection() {
        Direction north = Direction.NORTH;
        abstractCell.setNeighbor(neighborAbstractCell, north);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, north);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(north));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(north.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnTwoNeighborCellsByCellAndDirection() {
        Direction south = Direction.SOUTH;
        abstractCell.setNeighbor(neighborAbstractCell, south);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, south);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(south));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(south.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnTwoNeighborCellsByCellAndDirection() {
        Direction east = Direction.EAST;
        abstractCell.setNeighbor(neighborAbstractCell, east);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, east);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(east));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(east.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnTwoNeighborCellsByCellAndDirection() {
        Direction west = Direction.WEST;
        abstractCell.setNeighbor(neighborAbstractCell, west);
        BetweenCellsPosition betweenCellsPosition = new BetweenCellsPosition(abstractCell, west);

        assertEquals(abstractCell, betweenCellsPosition.getNeighborCells().get(west));
        assertEquals(neighborAbstractCell, betweenCellsPosition.getNeighborCells().get(west.getOppositeDirection()));
    }
}