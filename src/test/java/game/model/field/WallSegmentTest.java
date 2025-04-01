package game.model.field;

import game.model.Direction;
import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class WallSegmentTest {

    private Cell cell1;
    private Cell neighbourCell1;
    private WallSegment wallSegment1;

    private Cell cell2;
    private Cell neighbourCell2;
    private WallSegment wallSegment2;

    @BeforeEach
    public void testSetup() {
        cell1 = new CellTestModel();
        neighbourCell1 = new CellTestModel();
        wallSegment1 = new WallSegment();
        cell1.setNeighbor(neighbourCell1, Direction.WEST);


        cell2 = new CellTestModel();
        neighbourCell2 = new CellTestModel();
        wallSegment2 = new WallSegment();
        cell2.setNeighbor(neighbourCell2, Direction.WEST);
    }

    @Test
    public void test_equalsForWallsWithoutPosition() {
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithDifferentPosition() {
        cell1.setBetweenCellObject(wallSegment1, Direction.WEST);
        cell2.setBetweenCellObject(wallSegment2, Direction.WEST);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    /*TODO {Не получается создать тест с одинаковыми позициями,
       т.к. клетки не позволяют задать стенам одинаковые позиции.
       Задать позицию стене напрямую через метод setPosition не получилось,
       т.к. метод имеет пакетный уровень.}*/
    /*@Test
    public void test_equalsForWallWithSamePosition() {
        Assertions.assertEquals(wallSegment1, wallSegment2);
    }*/

    @Test
    public void test_equalsForWallWithPositionAndWallWithoutPosition() {
        cell1.setBetweenCellObject(wallSegment1, Direction.WEST);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

}
