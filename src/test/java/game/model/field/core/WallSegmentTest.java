package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class WallSegmentTest {

    private Cell cell1;
    private Cell neighbourCell1;
    private WallSegment wallSegment1;

    private Cell cell2;
    private Cell neighbourCell2;
    private WallSegment wallSegment2;

    @BeforeEach
    public void testSetup() {
        cell1 = new NormalCell();
        neighbourCell1 = new NormalCell();
        wallSegment1 = new WallSegment();
        Map<Direction, Cell> map1 = new HashMap<>();
        map1.put(Direction.WEST, neighbourCell1);
        neighbourCell1.setNeighbors(null);
        cell1.setNeighbors(map1);


        cell2 = new NormalCell();
        neighbourCell2 = new NormalCell();
        wallSegment2 = new WallSegment();
        Map<Direction, Cell> map2 = new HashMap<>();
        map2.put(Direction.WEST, neighbourCell2);
        neighbourCell2.setNeighbors(null);
        cell2.setNeighbors(map2);
    }

    @Test
    public void test_equalsForWallsWithoutPosition() {
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithDifferentPosition() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        cell2.setNeighborObstacle(Direction.WEST, wallSegment2);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithPositionAndWallWithoutPosition() {
        cell1.setNeighborObstacle(Direction.WEST, wallSegment1);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }
}
