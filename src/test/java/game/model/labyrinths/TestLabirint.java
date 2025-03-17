package game.model.labyrinths;

import org.jetbrains.annotations.NotNull;
import game.model.field.Field;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.cell_objects.Robot;
import game.model.field.cell_objects.power_supplies.Battery;
import game.model.labirints.Labirint;

public class TestLabirint extends Labirint {

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
    protected void addRobots(@NotNull Field field) {
        Robot firstRobot = new Robot(new Battery(10));
        Robot secondRobot = new Robot(new Battery(10));

        field.getCell(new Point(0,2)).addObject(firstRobot);
        field.getCell(new Point(2,0)).addObject(secondRobot);
    }

    @Override
    protected void addPowerSupplies(@NotNull Field field) {
        Battery battery = new Battery(DEFAULT_BATTERY_CHARGE);

        field.getCell(new Point(1, 2)).addObject(battery);
    }

    @Override
    protected void addBetweenCellObjects(@NotNull Field field) {
        field.getCell(new Point(2, 0)).setBetweenCellObject(new WallSegment(), Direction.SOUTH);
    }
}
