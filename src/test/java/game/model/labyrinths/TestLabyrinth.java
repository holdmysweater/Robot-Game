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
    protected Point exitPoint() {
        return new Point(2,2);
    }

    @Override
    protected AbstractMap.SimpleEntry<Robot, Cell> createRobot(@NotNull Field field) {
        return new AbstractMap.SimpleEntry<>(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );
    }

    @Override
    protected Map<Battery, Cell> createBatteries(@NotNull Field field) {
        Map<Battery, Cell> batteryMap = new HashMap<>();

        batteryMap.put(
                new Battery(),
                field.getCell(new Point(1, 2))
        );

        return batteryMap;
    }

    @Override
    protected Map<WallSegment, AbstractMap.SimpleEntry<Cell, Direction>> createWalls(@NotNull Field field) {
        Map<WallSegment, AbstractMap.SimpleEntry<Cell, Direction>> wallMap = new HashMap<>();

        wallMap.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 0)), Direction.SOUTH)
        );

        wallMap.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.SOUTH)
        );

        wallMap.put(
                new WallSegment(),
                new AbstractMap.SimpleEntry<>(field.getCell(new Point(2, 2)), Direction.EAST)
        );

        return wallMap;
    }
}
