package game.model.labyrinths;

import game.model.field.Cell;
import game.model.field.Labyrinth;
import game.model.field.between_cells_objects.BetweenCellsPosition;
import org.jetbrains.annotations.NotNull;
import game.model.*;
import game.model.field.Field;
import game.model.field.cell_objects.Battery;
import game.model.field.cell_objects.Robot;
import game.model.field.between_cells_objects.WallSegment;

import java.util.HashMap;
import java.util.Map;

/**
 * Лабиринт маленького поля.
 */
public class SmallLabyrinth extends Labyrinth {

    /**
     * Высота поля.
     */
    private static final int FIELD_HEIGHT = 4;

    /**
     * Ширина поля.
     */
    private static final int FIELD_WIDTH = 4;

    /**
     * Стандартный заряд батарейки.
     */
    private static final int DEFAULT_BATTERY_CHARGE = 10;


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
        return new Point(2, 0);
    }

    @Override
    protected Map<WallSegment, BetweenCellsPosition> createWalls(@NotNull Field field) {
        Map<WallSegment, BetweenCellsPosition> map = new HashMap<>();

        map.put( // TODO walls aren't being placed properly
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 0)), Direction.SOUTH)
        );

        map.put(
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 2)), Direction.SOUTH)
        );

        map.put(
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 2)), Direction.EAST)
        );

        return map;
    }

    @Override
    protected Map<Robot, Cell> createRobot(@NotNull Field field) {
        Map<Robot, Cell> map = new HashMap<>();

        map.put(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );

        return map;
    }

    @Override
    protected Map<Battery, Cell> createBatteries(@NotNull Field field) {
        Map<Battery, Cell> map = new HashMap<>();

        map.put(
                new Battery(),
                field.getCell(new Point(0, 3))
        );

        map.put(
                new Battery(),
                field.getCell(new Point(1, 2))
        );

        return map;
    }
}
