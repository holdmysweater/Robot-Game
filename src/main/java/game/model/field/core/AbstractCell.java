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
        // Perform all my checks before initiate connection TODO translate

        // Return TRUE because connection already exist with THIS big object
        if (this.bigObject == bigObject) return true;

        // Return FALSE if cell can't take big object
        if (!this.canSetBigObject()) {
            return false;
        }

        // Established connection
        boolean success = bigObject.setPosition(this);

        // Return FALSE if connection was failed
        if (!success) {
            return false;
        }

        // Remember big object
        this.bigObject = bigObject;
        // Connection is established correctly
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
     * Получить соседние ячейки.
     *
     * @return соседние ячейки.
     */
    @Deprecated
    public final Map<Direction, AbstractCell> getNeighborCells() {
        Map<Direction, AbstractCell> neighborCells = new HashMap<Direction, AbstractCell>();
        for (Direction direction : Direction.values()) {
            AbstractCell cell = getNeighborCell(direction);
            if (cell != null) {
                neighborCells.put(direction, cell);
            }
        }
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    // TODO геттер с Cell, а сеттер без Cell в наименовании, привести к единообразию
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
    boolean setNeighbors(@NotNull Map<Direction, AbstractCell> neighborCells) {
        for (Direction direction : neighborCells.keySet()) {
            if (!setNeighbor(neighborCells.get(direction), direction)) return false;
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
    @Deprecated
    private boolean setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        // Вернуть true если у объекта уже установлена связь с этой ячейкой в этом направлении
        if (getNeighborCell(direction) == neighborCell) return true;

        // Вернуть false если у любой из клеток нет возможности установить соседство
        if (!canSetNeighbor(direction, neighborCell)) return false;

        // Получить соседнюю область от нового соседа
        BetweenCellsArea neighborArea = neighborCell.getNeighborArea(direction.getOppositeDirection());

        // Проверка случая, при котором обе ячейки имеют свои области.
        // Запрещённая операция. Попытка соединить друг с другом сегменты из клеток.
        if (neighborArea != null && getNeighborCell(direction) != null) {
            throw new RuntimeException("Try connecting two cells with their own neighbor area. Cells:"
                    + this + neighborArea + ".");
        }

        // Успешно завершить установку связи с соседом.
        return true;
    }

    /**
     * Может установить соседство с ячейкой в заданном направлении.
     *
     * @param neighborCell клетка сосед.
     * @param direction    направление соседства.
     * @return возможность соседства.
     */
    boolean canSetNeighbor(@NotNull Direction direction, @NotNull AbstractCell neighborCell) {
        // Вернуть true если уже установлена связь с этим объектом в этом направлении
        if (getNeighborCell(direction) == neighborCell) return true;

        // Вернуть false если связь по этому направлению уже установлена с другим объектом
        if (getNeighborCell(direction) != null) return false;

        // Вернуть false если связь устанавливается с самим собой
        if (neighborCell == this) return false;

        // Может быть соседом
        return true;
    }

    /**
     * Является ли ячейка соседом.
     *
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    // TODO возможно, deprecated
    public boolean isNeighbor(@NotNull AbstractCell other) {
        return getNeighborCells().containsValue(other);
    }

    //endregion

    //region ОБЛАСТИ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Области, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellsArea> neighborAreas = new EnumMap<>(Direction.class);

    /**
     * Получить соседние области, располагающиеся между ячейками {@link AbstractCell#neighborAreas}.
     *
     * @return соседние области, располагающиеся между ячейками.
     */
    @Deprecated
    public Map<Direction, BetweenCellsArea> getNeighborAreas() {
        return Collections.unmodifiableMap(neighborAreas);
    }

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
        //TODO нужно реализовать
        return false;
    }

    /**
     * Окружить себя промежуточными областями.
     */
    private void surroundSelfWithBetweenCellsAreas() {
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.removeAll(neighborAreas.keySet());

        for (Direction direction : directions) {
            neighborAreas.put(direction, new BetweenCellsArea(this, direction.getOppositeDirection()));
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
        // TODO нужно реализовать
        return null;
    }

    /**
     * Установить соседнее препятствие.
     *
     * @param direction направление.
     * @param obstacle соседнее препятствие.
     * @return успешность.
     */
    public boolean setNeighborObstacle(@NotNull Direction direction, @NotNull BetweenCellObject obstacle) {
        // TODO нужно реализовать
        return false;
    }

    /**
     * Является ли препятствие соседом.
     *
     * @param obstacle препятствие.
     * @return сосед.
     */
    public Direction isNeighbor(@NotNull BetweenCellObject obstacle) {
        // TODO нужно реализовать
        return null;
    }

    //endregion
}
