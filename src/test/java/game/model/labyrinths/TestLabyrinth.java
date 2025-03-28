package game.model.labyrinths;

import game.model.Direction;
import game.model.field.Cell;
import game.model.field.Labyrinth;
import game.model.field.between_cells_objects.BetweenCellsPosition;
import org.jetbrains.annotations.NotNull;
import game.model.Point;
import game.model.field.Field;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.cell_objects.Robot;
import game.model.field.cell_objects.Battery;

import java.util.HashMap;
import java.util.Map;

public class TestLabyrinth extends Labyrinth {

    private static final int FIELD_HEIGHT = 3;
    private static final int FIELD_WIDTH = 3;
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
        return new Point(2,2);
    }

    @Override
    protected Map<Robot, Cell> createRobot(@NotNull Field field) {
        Map<Robot, Cell> robotMap = new HashMap<>();

        robotMap.put(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );

//        robotMap.put(
//                new Robot(new Battery()),
//                field.getCell(new Point(2, 0))
//        );

        return robotMap;
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
    protected Map<WallSegment, BetweenCellsPosition> createWalls(@NotNull Field field) {
        Map<WallSegment, BetweenCellsPosition> wallMap = new HashMap<>();

        wallMap.put(
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 0)), Direction.SOUTH)
        );

        wallMap.put(
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 2)), Direction.SOUTH)
        );

        wallMap.put(
                new WallSegment(),
                new BetweenCellsPosition(field.getCell(new Point(2, 2)), Direction.EAST)
        );

        return wallMap;
    }
}
