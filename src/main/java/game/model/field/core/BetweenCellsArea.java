package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Класс позиции между ячейками {@link AbstractCell}
 */
public class BetweenCellsArea {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param cell      ячейка.
     * @param direction направление.
     */
    public BetweenCellsArea(@NotNull AbstractCell cell, @NotNull Direction direction) {
        neighborCells.put(direction, cell);
        orientation = calculateOrientation(direction);
    }

    //endregion

    //region ПРЕПЯТСТВИЕ

    /**
     * Препятствие, расположенный в ячейке.
     */
    private BetweenCellObject obstacle = null;

    /**
     * Получить препятствие.
     *
     * @return препятствие.
     */
    public BetweenCellObject getObstacle() {
        return obstacle;
    }

    /**
     * Поместить препятствие в область {@link BetweenCellsArea#obstacle}.
     *
     * @param obstacle объект, добавляемый в область между ячейками.
     */
    boolean setObstacle(@NotNull BetweenCellObject obstacle) {
        // Вернуть true, если этот объект уже задан
        if (this.obstacle == obstacle) return true;

        // Вернуть false, если этот объект не может быть размещён в этой области
        if (!canSetObstacle(obstacle)) return false;

        // Вернуть false, если объект между ячеек не смог принять позицию
        if (!obstacle.setPosition(this)) return false;

        // Запомнить объект
        this.obstacle = obstacle;
        return true;
    }

    /**
     * Может принять препятствие.
     *
     * @param obstacle препятствие.
     * @return может приять препятствие.
     */
    private boolean canSetObstacle(BetweenCellObject obstacle) {
        return this.obstacle == null;
    }

    /**
     * Изъять препятствие из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link BetweenCellsArea#obstacle}.
     */
    @Deprecated
    public BetweenCellObject takeObstacle() {
        BetweenCellObject result = obstacle;

        if (result != null) {
            result.setPosition(null);
            obstacle = null;
        }

        return result;
    }

    //endregion

    //region СОСЕДИ

    /**
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Получить соседние ячейки {@link BetweenCellsArea#neighborCells}.
     *
     * @return соседние ячейки.
     */
    @Deprecated
    public Map<Direction, AbstractCell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка в заданном направлении.
     */
    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    /**
     * Установить соседей слева и справа.
     *
     * @param leftCell  ячейка слева.
     * @param rightCell ячейка справа.
     * @return успешность.
     */
    boolean setHorizontalNeighbors(AbstractCell leftCell, AbstractCell rightCell) {
        return true; // TODO реализация
    }

    /**
     * Установить соседей сверху и снизу.
     *
     * @param topCell    верхняя ячейка.
     * @param bottomCell нижняя ячейка.
     * @return успешность.
     */
    boolean setVerticalNeighbors(AbstractCell topCell, AbstractCell bottomCell) {
        return true; // TODO реализация
    }

    /**
     * Может ли установить соседей.
     *
     * @param cells соседние ячейки.
     * @return возможность соседства.
     */
    private boolean canSetNeighbors(Map<Direction, AbstractCell> cells) {
        return true; // TODO реализация
    }

    @Deprecated
    boolean addNeighbor(@NotNull AbstractCell cell, @NotNull Direction direction) {
        // Вернуть true если текущая клетка уже является соседом
        if (this.getNeighborCell(direction) == cell) return true;

        // Вернуть false, если ячейки не могут быть соседями друг друга
        AbstractCell neighborCell = neighborCells.get(direction.getOppositeDirection()); // Получить ячейку уже соседствующую с областью
        if (!neighborCell.canSetNeighbor(direction, cell) ||
                !cell.canSetNeighbor(direction.getOppositeDirection(), neighborCell)) return false;

        // Сохранить клетку
        neighborCells.put(direction, cell);

        // Установить соседство для новой клетки
        Map<Direction, AbstractCell> neighbor = new HashMap<>();
        neighbor.put(direction.getOppositeDirection(), neighborCell);
        boolean success = true; // TODO = cell.setNeighbor(neighbor);

        // Если установить соседство не удалось, значит метод 'canEstablishNeighbor' работает некорректно.
        assert !success : "Can't add neighbor. Check correctness of 'canEstablishNeighbor' method.";

        // Вернуть результат установления соседства.
        return success;
    }

    @Deprecated
    public boolean isNeighbor(@NotNull AbstractCell cell) {
        return neighborCells.containsValue(cell);
    }

    //endregion

    //region ОРИЕНТАЦИЯ

    /**
     * Ориентация области на поле.
     */
    private Orientation orientation = null;

    /**
     * Вернуть ориентацию поля.
     *
     * @return ориентация.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Рассчитывает ориентацию области по направлению соседних ячеек.
     *
     * @param direction направление.
     * @return ориентация.
     */
    private static Orientation calculateOrientation(@NotNull Direction direction) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return Orientation.HORIZONTAL;
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            return Orientation.VERTICAL;
        } else {
            assert true : "Unknown orientation type: " + direction;
            return null;
        }
    }

    //endregion

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BetweenCellsArea that = (BetweenCellsArea) o;

        return Objects.equals(neighborCells, that.neighborCells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(neighborCells);
    }

    @Override
    public String toString() {
        return "WallPosition{" + "neighborCells=" + neighborCells + '}';
    }

    //endregion
}
