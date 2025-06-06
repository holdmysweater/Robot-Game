package game.model.labyrinths;

import game.model.field.core.*;
import game.model.field.core.Cell;
import org.jetbrains.annotations.NotNull;
import game.model.field.between_cells_objects.WallSegment;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TestLabyrinth extends Labyrinth {

    private static final int FIELD_HEIGHT = 3;
    private static final int FIELD_WIDTH = 3;

    @Override
    protected int fieldHeight() {
        return FIELD_HEIGHT;
    }

    @Override
    protected int fieldWidth() {
        return FIELD_WIDTH;
    }

    @Override
    protected int moleCount() {
        return 1;
    }

    @Override
    protected Map<CellObject, Point> createObjects(@NotNull Field field) {
        Map<CellObject, Point> objects = new HashMap<>();

        objects.put(
                new Robot(new Battery()),
                new Point(0, 2)
        );

        objects.put(
                new ExitPoint(),
                new Point(2, 2)
        );

        objects.put(
                new Battery(),
                new Point(1, 2)
        );

        return objects;
    }

    @Override
    protected Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> createObstacles(@NotNull Field field) {
        Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> obstacles = new HashMap<>();

        obstacles.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 0)), Direction.SOUTH)
        );

        obstacles.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.SOUTH)
        );

        obstacles.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.EAST)
        );

        return obstacles;
    }
}
