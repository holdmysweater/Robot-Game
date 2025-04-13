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
        //TODO Проверки DONE
        if (this.bigObject == bigObject) return true;

        boolean hasBigObject = this.bigObject != null;
        assert !hasBigObject;
        if (hasBigObject) {
            return false;
        }

        boolean isPositionSetSuccess = bigObject.setPosition(this);
        assert isPositionSetSuccess;
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
    // TODO TODO - принять, а не изъять (canPutObject)

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
     * @return успешность.
     */
    boolean setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        //TODO TODO - если повторно, то true
        assert !(neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell));
        if (neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell)) {
            return false;
        }

        neighborCells.put(direction, neighborCell);

        if (!neighborCell.isNeighbor(this)) { //TODO TODO Нужно ли проверять - если только для assert
            boolean success = neighborCell.setNeighbor(this, direction.getOppositeDirection());
            assert success;
        }

        return true;
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
     * @param obstacle объект, располагающийся между ячейками.
     * @param direction         направление.
     */
    public boolean setNeighborObstacle(@NotNull BetweenCellObject obstacle, @NotNull Direction direction) {//TODO obstacle - поменять везде параметры на obstacle как тyт
        //TODO Проверки DONE
        if (getNeighborObstacle(direction) == obstacle) return true;

        if (getNeighborObstacle(direction) != null) return false;

        neighborObstacles.put(direction, obstacle);

        BetweenCellsPosition position = new BetweenCellsPosition(this, direction);
        if (!obstacle.canLocateAtPosition(position)) {// TODO TODO  - область между ячейками решает, может ли находиться препятствие + препятсвие решает, может ли оно находиться в этой области
            return false;
        }

        AbstractCell neighbor = getNeighborCell(direction);
        if (neighbor != null) {
            neighbor.setNeighborObstacle(obstacle, direction.getOppositeDirection());
        }

        return obstacle.setPosition(position);
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
