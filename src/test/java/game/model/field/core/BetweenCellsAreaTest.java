package game.model.field.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BetweenCellsAreaTest {

    private AbstractCell abstractCell;
    private AbstractCell neighborAbstractCell;

    @BeforeEach
    public void testSetup() {
        abstractCell = new NormalCell();
        abstractCell.setNeighbors(null);

        neighborAbstractCell = new NormalCell();
    }

    @Test
    public void test_setHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.EAST);

        assertTrue(betweenCellsArea.setHorizontalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.SOUTH);

        assertTrue(betweenCellsArea.setVerticalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertEquals(neighborAbstractCell, betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }

    @Test
    public void test_setHorizontalNeighbors_alreadyHasVerticalNeighbors() {
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.EAST);

        assertFalse(betweenCellsArea.setVerticalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.VERTICAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.WEST));
        assertNull(betweenCellsArea.getNeighborCell(Direction.EAST));
    }

    @Test
    public void test_setVerticalNeighbors_alreadyHasHorizontalNeighbors() {
        BetweenCellsArea betweenCellsArea = abstractCell.getNeighborArea(Direction.SOUTH);

        assertFalse(betweenCellsArea.setHorizontalNeighbors(abstractCell, neighborAbstractCell));
        assertEquals(Orientation.HORIZONTAL, betweenCellsArea.getOrientation());
        assertEquals(abstractCell, betweenCellsArea.getNeighborCell(Direction.NORTH));
        assertNull(betweenCellsArea.getNeighborCell(Direction.SOUTH));
    }
}