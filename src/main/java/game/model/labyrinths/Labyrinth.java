package game.model.labyrinths;

import game.model.field.cell_objects.InteractiveCellObject;
import game.model.field.cell_objects.NonInteractiveCellObject;
import game.model.field.cell_objects.NonStationaryCellObject;
import game.model.field.cell_objects.SmallCellObject;
import game.model.field.core.*;
import game.model.field.core.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Лабиринт.
 */
public abstract class Labyrinth {

    //region СБОРЩИК ПОЛЯ

    /**
     * Построить поле.
     *
     * @return поле.
     */
    public Field createField() {

        Field field = new Field(fieldWidth(), fieldHeight());

        populateField(field);

        return field;
    }

    //endregion

    //region ЗАСЕЛЕНИЕ ПОЛЯ

    /**
     * Заселить поле.
     *
     * @param field поле.
     */
    private void populateField(@NotNull Field field) {
        populateWalls(field);
        populateObjects(field);
    }

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    private void populateWalls(@NotNull Field field) {
        Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> obstacles = createObstacles(field);

        for (BetweenCellObject obstacle : obstacles.keySet()) {
            Cell cell = obstacles.get(obstacle).getKey();
            Direction direction = obstacles.get(obstacle).getValue();
            boolean result = cell.setNeighborObstacle(direction, obstacle);
            assert result: "Wall segment " + obstacle + " not set at " + cell + " with direction " + direction;
        }
    }

    /**
     * Добавить объекты на поле.
     *
     * @param field поле.
     */
    private void populateObjects(@NotNull Field field) {
        Map<CellObject, Cell> objects = createObjects(field);

        for (CellObject object : objects.keySet()) {

            boolean correct = false;

            if (object instanceof SmallCellObject) {
                correct = objects.get(object).setObject(SmallCellObject.class, object);
            }
            else if (object instanceof NonStationaryCellObject) {
                correct = objects.get(object).setObject(NonStationaryCellObject.class, object);
            }
            else if (object instanceof InteractiveCellObject) {
                correct = objects.get(object).setObject(InteractiveCellObject.class, object);
            }
            else if (object instanceof NonInteractiveCellObject) {
                correct = objects.get(object).setObject(NonInteractiveCellObject.class, object);
            }

            assert correct : "Object can't set at cell";
        }
    }

    //endregion

    //region СВОЙСТВА ПОЛЯ

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

    //endregion

    //region СОЗДАНИЕ ОБЪЕКТОВ

    /**
     * Добавить объекты между ячейками на поле.
     *
     * @param field поле.
     */
    protected abstract Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> createObstacles(@NotNull Field field);

    /**
     * Добавить объекты на поле.
     *
     * @param field поле.
     */
    protected abstract Map<CellObject, Cell> createObjects(@NotNull Field field);

    //endregion
}
