package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetweenCellsAreaTest {

    private Cell cell;
    private Cell neighborCell;

    @BeforeEach
    public void testSetup() {
        cell = new NormalCell();
        cell.setNeighbors(null);

        neighborCell = new NormalCell();
    }

    @Test
    public void test_setHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.EAST);

        assertTrue(betweenCellsArea.setHorizontalNeighbors(cell, neighborCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(cell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertEquals(neighborCell, betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.SOUTH);

        assertTrue(betweenCellsArea.setVerticalNeighbors(cell, neighborCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(cell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertEquals(neighborCell, betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }

    @Test
    public void test_setHorizontalNeighbors_alreadyHasVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.EAST);

        assertFalse(betweenCellsArea.setVerticalNeighbors(cell, neighborCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(cell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertNull(betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors_alreadyHasHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.SOUTH);

        assertFalse(betweenCellsArea.setHorizontalNeighbors(cell, neighborCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(cell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertNull(betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }
}