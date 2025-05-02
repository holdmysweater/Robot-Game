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

    @Override
    protected Point exitPoint() {
        return new Point(2, 0);
    }

    //endregion

    //region СОЗДАНИЕ ОБЪЕКТОВ

    @Override
    protected Map<WallSegment, AbstractMap.SimpleEntry<AbstractCell, Direction>> createWalls(@NotNull Field field) {
        Map<WallSegment, AbstractMap.SimpleEntry<AbstractCell, Direction>> map = new HashMap<>();

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
    protected AbstractMap.SimpleEntry<Robot, AbstractCell> createRobot(@NotNull Field field) {
        return new AbstractMap.SimpleEntry<>(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );
    }

    @Override
    protected Map<Battery, AbstractCell> createBatteries(@NotNull Field field) {
        Map<Battery, AbstractCell> map = new HashMap<>();

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
