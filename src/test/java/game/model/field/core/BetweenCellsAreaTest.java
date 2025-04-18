package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetweenCellsAreaTest {

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
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, direction);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(direction));
    }

    @Test
    public void test_createAndGetNeighborCells_withCellAndDirectionOnTwoNeighborCells() {
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, direction);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(direction));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(direction.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_withTwoNeighborsCells() {
        abstractCell.setNeighbor(neighborAbstractCell, direction);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(direction.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(direction));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnSingleCell() {
        Direction north = Direction.NORTH;
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, north);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(north));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnSingleCell() {
        Direction south = Direction.SOUTH;
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, south);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(south));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnSingleCell() {
        Direction east = Direction.EAST;
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, east);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(east));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnSingleCell() {
        Direction west = Direction.WEST;
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, west);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(west));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnTwoNeighborCells() {
        Direction north = Direction.NORTH;
        abstractCell.setNeighbor(neighborAbstractCell, north);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(north.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(north));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnTwoNeighborCells() {
        Direction south = Direction.SOUTH;
        abstractCell.setNeighbor(neighborAbstractCell, south);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(south.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(south));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnTwoNeighborCells() {
        Direction east = Direction.EAST;
        abstractCell.setNeighbor(neighborAbstractCell, east);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(east.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(east));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnTwoNeighborCells() {
        Direction west = Direction.WEST;
        abstractCell.setNeighbor(neighborAbstractCell, west);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, neighborAbstractCell);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(west.getOppositeDirection()));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(west));
    }

    @Test
    public void test_createAndGetNeighborCells_toNorthOnTwoNeighborCellsByCellAndDirection() {
        Direction north = Direction.NORTH;
        abstractCell.setNeighbor(neighborAbstractCell, north);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, north);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(north));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(north.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toSouthOnTwoNeighborCellsByCellAndDirection() {
        Direction south = Direction.SOUTH;
        abstractCell.setNeighbor(neighborAbstractCell, south);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, south);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(south));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(south.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toEastOnTwoNeighborCellsByCellAndDirection() {
        Direction east = Direction.EAST;
        abstractCell.setNeighbor(neighborAbstractCell, east);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, east);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(east));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(east.getOppositeDirection()));
    }

    @Test
    public void test_createAndGetNeighborCells_toWestOnTwoNeighborCellsByCellAndDirection() {
        Direction west = Direction.WEST;
        abstractCell.setNeighbor(neighborAbstractCell, west);
        BetweenCellsArea betweenCellsArea = new BetweenCellsArea(abstractCell, west);

        assertEquals(abstractCell, betweenCellsArea.getNeighborCells().get(west));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCells().get(west.getOppositeDirection()));
    }
}