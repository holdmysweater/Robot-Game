package game.model.labyrinths;

import game.model.field.core.Direction;
import game.model.field.core.NormalCell;
import game.model.field.core.BetweenCellsArea;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.core.Field;
import game.model.field.core.Robot;
import game.model.field.core.Battery;
import game.model.field.core.AbstractCell;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Point;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Лабиринт.
 */
public abstract class Labyrinth {

    /**
     * Построить поле.
     *
     * @return поле.
     */
    public Field createField() {

        Field field = new Field(fieldWidth(), fieldHeight(), exitPoint());

        populateField(field);

        return field;
    }

    /**
     * Высота поля.
     *
     * @return высота поля.
     */
    protected abstract int fieldHeight();

    /**
     * Ширина поля.
     *
     * @return ширина поля.
     */
    protected abstract int fieldWidth();

    /**
     * Координаты ячейки выхода.
     *
     * @return координаты ячейки выхода.
     */
    protected abstract Point exitPoint();

    /**
     * Заселить поле.
     *
     * @param field поле.
     */
    protected void populateField(@NotNull Field field) {
        populateWalls(field);
        populateRobot(field);
        populateBatteries(field);
    }

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    protected void populateWalls(@NotNull Field field) {
        Map<WallSegment, BetweenCellsArea> walls = createWalls(field);

        for (WallSegment wall : walls.keySet()) {
            BetweenCellsArea betweenCellsArea = walls.get(wall);
            //TODO изменить объявление 
            //boolean result = betweenCellsArea.setObstacle(wall);
            //assert result: "Wall segment " + wall + " not set at " + betweenCellsArea;
        }
    }

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    protected void populateRobot(@NotNull Field field) {
        // Get information about single robot on field
        AbstractMap.SimpleEntry<Robot, AbstractCell> robotInfo = createRobot(field);
        Robot robot = robotInfo.getKey();
        AbstractCell robotCell = robotInfo.getValue();

        // Establish connection between the cell and the robot
        boolean correct = robotCell.setBigObject(robot);
        assert correct; // Check connection status
    }

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    protected void populateBatteries(@NotNull Field field) {
        Map<Battery, AbstractCell> batteries = createBatteries(field);

        for (Battery battery : batteries.keySet()) {
            AbstractCell cell = batteries.get(battery);

            // Check cell class
            boolean isNormalCell = cell instanceof NormalCell;
            assert !isNormalCell : "Battery can't set at cell that is not NormalCell";
            if (!isNormalCell) { continue; }

            // Set battery
            NormalCell normalCell = (NormalCell) cell;
            boolean correct = normalCell.setSmallObject(battery);
            assert correct : "Battery can't set at cell";
        }
    }

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    protected abstract Map<WallSegment, BetweenCellsArea> createWalls(@NotNull Field field);

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    protected abstract AbstractMap.SimpleEntry<Robot, AbstractCell> createRobot(@NotNull Field field);

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    protected abstract Map<Battery, AbstractCell> createBatteries(@NotNull Field field);
}
