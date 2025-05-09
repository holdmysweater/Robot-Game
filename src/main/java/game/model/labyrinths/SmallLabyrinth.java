package game.model.labyrinths;

import game.model.field.core.*;
import org.jetbrains.annotations.NotNull;
import game.model.field.between_cells_objects.WallSegment;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Лабиринт маленького поля.
 */
public class SmallLabyrinth extends Labyrinth {

    //region СВОЙСТВА ПОЛЯ

    @Override
    protected int fieldHeight() {
        return 4;
    }

    @Override
    protected int fieldWidth() {
        return 4;
    }

    //endregion

    //region СОЗДАНИЕ ОБЪЕКТОВ

    @Override
    protected Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> createObstacles(@NotNull Field field) {
        Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> map = new HashMap<>();

        map.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 0)), Direction.SOUTH)
        );

        map.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.SOUTH)
        );

        map.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.EAST)
        );

        return map;
    }

    @Override
    protected Map<CellObject, Cell> createObjects(@NotNull Field field) {
        Map<CellObject, Cell> map = new HashMap<>();

        map.put(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );

        map.put(
                new ExitCell(),
                field.getCell(new Point(2, 0))
        );

        map.put(
                new Battery(),
                field.getCell(new Point(0, 3))
        );

        map.put(
                new Battery(),
                field.getCell(new Point(1, 2))
        );

        map.put(
                new Battery(0),
                field.getCell(new Point(1, 3))
        );

        return map;
    }

    //endregion
}
