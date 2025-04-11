package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Ячейка.
 */
public abstract class AbstractCell {
    /*---------- ОБЪЕКТ В ЯЧЕЙКЕ ----------*/
    /**
     * Большой объект, расположенный в ячейке.
     */
    private Robot bigObject = null;

    /**
     * Добавить большой объект в ячейку {@link AbstractCell#bigObject}.
     *
     * @param bigObject объект, добавляемый в ячейку.
     */
    public boolean setBigObject(@NotNull Robot bigObject) {
        //TODO Проверки
        if (this.bigObject != null) {
            throw new IllegalArgumentException("Cell already has a big object.");
        }

        boolean isPositionSetSuccess = bigObject.setPosition(this);
        if (!isPositionSetSuccess) {
            return false;
        }

        this.bigObject = bigObject;
        return true;
    }

    /**
     * Изъять большой объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link AbstractCell#bigObject}.
     */
    public Robot takeBigObject() {
        Robot result = bigObject;

        if (result != null) {
            result.setPosition(null);
            bigObject = null;
        }

        return result;
    }

    /**
     * Получить большой объект.
     *
     * @return большой объект.
     */
    public Robot getBigObject() {
        return bigObject;
    }

    /**
     * Может принять большой объект.
     *
     * @return может принять большой объект.
     */
    public boolean canTakeBigObject() {
        return bigObject == null;
    }
    //TODO Общий доступ???



    /*---------- СОСЕДНИЕ ЯЧЕЙКИ ----------*/
    /**
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Получить соседние ячейки {@link AbstractCell#neighborCells}.
     *
     * @return соседние ячейки.
     */
    public final Map<Direction, AbstractCell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    /**
     * Установить ячейку соседней {@link AbstractCell#neighborCells}.
     *
     * @param neighborCell соседняя ячейка.
     * @param direction    направление.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     */
    void setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        //TODO Проверки
        if (neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell)) {
            throw new IllegalArgumentException();
        }

        neighborCells.put(direction, neighborCell);

        if (!neighborCell.isNeighbor(this)) {
            neighborCell.setNeighbor(this, direction.getOppositeDirection());
        }
    }

    /**
     * Получить направление с соседней ячейкой.
     *
     * @param other соседняя ячейка.
     * @return направление.
     */
    public Direction getNeighborDirection(@NotNull AbstractCell other) {
        for (var cell : neighborCells.entrySet()) {
            if (cell.getValue().equals(other)) return cell.getKey();
        }
        return null;
    }

    /**
     * Является ли ячейка соседом.
     *
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull AbstractCell other) {
        return neighborCells.containsValue(other);
    }



    /*---------- ОБЪЕКТЫ МЕЖДУ ЯЧЕЙКАМИ ----------*/
    /**
     * Соседние объекты, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellObject> neighborObstacles = new EnumMap<>(Direction.class);

    /**
     * Получить соседние объекты, располагающиеся между ячейками {@link AbstractCell#neighborObstacles}.
     *
     * @return соседние объекты, располагающиеся между ячейками.
     */
    public Map<Direction, BetweenCellObject> getNeighborObstacles() {
        return Collections.unmodifiableMap(neighborObstacles);
    }

    /**
     * Установить объект, располагающийся между ячейками в заданном направлении.
     *
     * @param betweenCellObject объект, располагающийся между ячейками.
     * @param direction         направление.
     */
    public boolean setNeighborObstacle(@NotNull BetweenCellObject betweenCellObject, @NotNull Direction direction) {
        //TODO Проверки
        if (getNeighborObstacle(direction) == betweenCellObject) return true;

        if (getNeighborObstacle(direction) != null) return false;

        neighborObstacles.put(direction, betweenCellObject);

        BetweenCellsPosition position = new BetweenCellsPosition(this, direction);
        if (!betweenCellObject.canSetAtPosition(position)) {
            return false;
        }

        AbstractCell neighbor = getNeighborCell(direction);
        if (neighbor != null) {
            neighbor.setNeighborObstacle(betweenCellObject, direction.getOppositeDirection());
        }

        return betweenCellObject.setPosition(position);
    }

    /**
     * Получить соседний объект, располагающийся между ячейками {@link AbstractCell#neighborObstacles} в заданном направлении.
     *
     * @param direction направление.
     * @return соседний объект, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellObject getNeighborObstacle(@NotNull Direction direction) {
        return neighborObstacles.get(direction);
    }

    /**
     * Является ли ячейка соседом.
     *
     * @param obstacle соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull BetweenCellObject obstacle) {
        return neighborObstacles.containsValue(obstacle);
    }
}
