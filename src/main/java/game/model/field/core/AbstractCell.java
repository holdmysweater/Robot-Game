package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Ячейка.
 */
public abstract class AbstractCell {

    //region ОБЪЕКТ В ЯЧЕЙКЕ

    /**
     * Крупный объект, расположенный в ячейке.
     */
    private Robot bigObject = null;

    /**
     * Получить крупный объект.
     *
     * @return крупный объект.
     */
    public Robot getBigObject() {
        return bigObject;
    }

    /**
     * Поместить крупный объект в ячейку {@link AbstractCell#bigObject}.
     *
     * @param bigObject объект, добавляемый в ячейку.
     */
    public boolean setBigObject(@NotNull Robot bigObject) {
        if (!this.canSetBigObject()) {
            return false;
        }

        boolean success = bigObject.setPosition(this);
        if (!success) {
            return false;
        }

        this.bigObject = bigObject;

        return true;
    }

    /**
     * Может принять крупный объект.
     *
     * @return может принять крупный объект.
     */
    public boolean canSetBigObject() {
        return getBigObject() == null;
    }

    /**
     * Изъять крупный объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link AbstractCell#bigObject}.
     */
    public Robot takeBigObject() {
        Robot result = bigObject;

        if (result != null) {
            result.unsetPosition();
            bigObject = null;
        }

        return result;
    }

    //endregion

    //region СОСЕДНИЕ ЯЧЕЙКИ

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area == null) {
            return null;
        } else {
            return area.getNeighborCell(direction);
        }
    }

    /**
     * Установить соседние ячейки.
     *
     * @param neighborCells список ячеек с соответствующими направлениями соседства.
     * @return успешность.
     */
    boolean setNeighbors(Map<Direction, AbstractCell> neighborCells) {
        if (neighborCells != null){
            for (Direction direction : neighborCells.keySet()) {
                if (!setNeighbor(neighborCells.get(direction), direction)) return false;
            }
        }

        surroundSelfWithBetweenCellsAreas();
        return true;
    }

    /**
     * Установить ячейку соседней.
     * Взять её область между ячейками.
     *
     * @param neighborCell соседняя ячейка.
     * @param direction    направление.
     * @return успешность.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     */
    private boolean setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        if (this == neighborCell) { return false; }
        BetweenCellsArea area = neighborCell.getNeighborArea(direction.getOppositeDirection());
        return switch (direction) {
            case NORTH -> area.setVerticalNeighbors(neighborCell, this);
            case SOUTH -> area.setVerticalNeighbors(this, neighborCell);
            case EAST -> area.setHorizontalNeighbors(this, neighborCell);
            case WEST -> area.setHorizontalNeighbors(neighborCell, this);
        };
    }

    //endregion

    //region ОБЛАСТИ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Области, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellsArea> neighborAreas = new EnumMap<>(Direction.class);

    /**
     * Получить соседнюю область, располагающуюся между ячейками {@link AbstractCell#neighborAreas} в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя область, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellsArea getNeighborArea(@NotNull Direction direction) {
        return neighborAreas.get(direction);
    }

    /**
     * Задать соседнюю область между ячейками.
     *
     * @param direction направление.
     * @param neighborArea соседняя область между ячейками.
     * @return успешность.
     */
    boolean setNeighborArea(@NotNull Direction direction, @NotNull BetweenCellsArea neighborArea) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area != null && !area.equals(neighborArea)) return false;
        neighborAreas.put(direction, neighborArea);
        return true;
    }

    /**
     * Окружить себя промежуточными областями.
     */
    private void surroundSelfWithBetweenCellsAreas() {
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.removeAll(neighborAreas.keySet());

        for (Direction direction : directions) {
            BetweenCellsArea area = new BetweenCellsArea();
            switch (direction) {
                case NORTH -> area.setVerticalNeighbors(null, this);
                case SOUTH -> area.setVerticalNeighbors(this, null);
                case EAST -> area.setHorizontalNeighbors(this, null);
                case WEST -> area.setHorizontalNeighbors(null, this);
            }
        }
    }

    //endregion

    //region ПРЕПЯТСТВИЯ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Получить соседнее препятствие.
     *
     * @param direction направление.
     * @return соседнее препятствие, располагающееся между ячейками в заданном направлении.
     */
    public BetweenCellObject getNeighborObstacle(@NotNull Direction direction) {
        return getNeighborArea(direction).getObstacle();
    }

    /**
     * Установить соседнее препятствие.
     *
     * @param direction направление.
     * @param obstacle соседнее препятствие.
     * @return успешность.
     */
    public boolean setNeighborObstacle(@NotNull Direction direction, @NotNull BetweenCellObject obstacle) {
        return getNeighborArea(direction).setObstacle(obstacle);
    }

    //endregion
}
