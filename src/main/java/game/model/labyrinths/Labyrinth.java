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
        populateMoles(field);
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
            assert result : "Wall segment " + obstacle + " not set at " + cell + " with direction " + direction;
        }
    }

    /**
     * Добавить объекты на поле.
     *
     * @param field поле.
     */
    private void populateObjects(@NotNull Field field) {
        Map<CellObject, Point> objects = createObjects(field);

        for (Map.Entry<CellObject, Point> entry : objects.entrySet()) {
            CellObject object = entry.getKey();
            Point point = entry.getValue();

            field.addObjectToCell(object, point);
        }
    }

    /**
     * Заселить кротов на поле.
     *
     * @param field поле.
     */
    private void populateMoles(@NotNull Field field) {
        for (int i = 0; i < this.moleCount(); i++) {
            Mole mole = new Mole();
            boolean success = field.addMole(mole);
            assert success : "Mole " + mole + " not set in field " + field;
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

    /**
     * Количество кротов на поле.
     *
     * @return количество кротов.
     */
    protected abstract int moleCount();

    //endregion

    //region СОЗДАНИЕ ОБЪЕКТОВ

    /**
     * Создать объекты между ячейками.
     *
     * @param field поле.
     */
    protected abstract Map<BetweenCellObject, AbstractMap.SimpleEntry<Cell, Direction>> createObstacles(@NotNull Field field);

    /**
     * Создать объекты в ячейках.
     *
     * @param field поле.
     */
    protected abstract Map<CellObject, Point> createObjects(@NotNull Field field);

    //endregion
}
