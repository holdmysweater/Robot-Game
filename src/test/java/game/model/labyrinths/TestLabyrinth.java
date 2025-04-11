package game.model.labyrinths;

import game.model.field.core.*;
import game.model.field.core.AbstractCell;
import org.jetbrains.annotations.NotNull;
import game.model.field.between_cells_objects.WallSegment;

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
    protected Map<Robot, AbstractCell> createRobot(@NotNull Field field) {
        Map<Robot, AbstractCell> robotMap = new HashMap<>();

        robotMap.put(
                new Robot(new Battery()),
                field.getCell(new Point(0, 2))
        );

        return robotMap;
    }

    @Override
    protected Map<Battery, AbstractCell> createBatteries(@NotNull Field field) {
        Map<Battery, AbstractCell> batteryMap = new HashMap<>();

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
