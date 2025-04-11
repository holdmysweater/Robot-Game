package game.model.field.core;

import game.model.field.between_cells_objects.WallSegment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WallSegmentTest {

    private AbstractCell abstractCell1;
    private AbstractCell neighbourAbstractCell1;
    private WallSegment wallSegment1;

    private AbstractCell abstractCell2;
    private AbstractCell neighbourAbstractCell2;
    private WallSegment wallSegment2;

    @BeforeEach
    public void testSetup() {
        abstractCell1 = new AbstractCellTestModel();
        neighbourAbstractCell1 = new AbstractCellTestModel();
        wallSegment1 = new WallSegment();
        abstractCell1.setNeighbor(neighbourAbstractCell1, Direction.WEST);


        abstractCell2 = new AbstractCellTestModel();
        neighbourAbstractCell2 = new AbstractCellTestModel();
        wallSegment2 = new WallSegment();
        abstractCell2.setNeighbor(neighbourAbstractCell2, Direction.WEST);
    }

    @Test
    public void test_equalsForWallsWithoutPosition() {
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

    @Test
    public void test_equalsForWallWithDifferentPosition() {
        abstractCell1.setBetweenCellObject(wallSegment1, Direction.WEST);
        abstractCell2.setBetweenCellObject(wallSegment2, Direction.WEST);
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
        abstractCell1.setBetweenCellObject(wallSegment1, Direction.WEST);
        Assertions.assertNotEquals(wallSegment1, wallSegment2);
    }

}
