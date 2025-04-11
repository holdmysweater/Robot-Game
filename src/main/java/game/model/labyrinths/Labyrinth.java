package game.model.labyrinths;

import game.model.field.core.*;
import game.model.field.core.NormalCell;
import game.model.field.between_cells_objects.WallSegment;
import org.jetbrains.annotations.NotNull;

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
        Map<WallSegment, BetweenCellsPosition> walls = createWalls(field);

        for (WallSegment wall : walls.keySet()) {
            Direction direction = walls.get(wall).getNeighborCells().keySet().iterator().next();
            AbstractCell abstractCell = walls.get(wall).getNeighborCells().get(direction);

            if (!abstractCell.setBetweenCellObject(wall, direction)) {
                throw new RuntimeException("Wall segment " + wall + " not set");
            }
        }
    }

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    protected void populateRobot(@NotNull Field field) {
        Map<Robot, AbstractCell> robot = createRobot(field);

        if (robot.keySet().size() != 1) {
            throw new RuntimeException("Only one robot can exist");
        }

        for (Robot r : robot.keySet()) {
            if (!robot.get(r).setBigObject(r)) {
                throw new RuntimeException("Robot " + r + " not set");
            }
        }
    }

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    protected void populateBatteries(@NotNull Field field) {
        Map<Battery, AbstractCell> batteries = createBatteries(field);

        for (Battery b : batteries.keySet()) {
            if (!((NormalCell) batteries.get(b)).setSmallObject(b)) {
                throw new RuntimeException("Battery " + b + " not set");
            }
        }
    }

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    protected abstract Map<WallSegment, BetweenCellsPosition> createWalls(@NotNull Field field);

    /**
     * Добавить роботов на поле.
     *
     * @param field поле.
     */
    protected abstract Map<Robot, AbstractCell> createRobot(@NotNull Field field);

    /**
     * Добавить источники питания на поле.
     *
     * @param field поле.
     */
    protected abstract Map<Battery, AbstractCell> createBatteries(@NotNull Field field);
}
