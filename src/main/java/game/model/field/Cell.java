package game.model.field;

import game.model.field.cell_objects.Robot;
import org.jetbrains.annotations.NotNull;
import game.model.*;
import game.model.field.between_cells_objects.BetweenCellsPosition;

import java.util.*;

/**
 * Ячейка.
 */
public abstract class Cell {
    /*---------- ОБЪЕКТ В ЯЧЕЙКЕ ----------*/
    /**
     * Большой объект, расположенный в ячейке.
     */
    protected Robot bigObject = null;

    /**
     * Добавить большой объект в ячейку {@link Cell#bigObject}.
     * @param bigObject объект, добавляемый в ячейку.
     */
    public boolean setBigObject(@NotNull Robot bigObject) {
        if (this.bigObject != null) throw new RuntimeException("Cell already has a big object.");
        boolean isPositionSetSuccess = bigObject.setPosition(this);
        if(!isPositionSetSuccess) return false;
        this.bigObject = bigObject;
        return true;
    }

    /**
     * Изъять большой объект из ячейки.
     * @return запрашиваемый объект. null - если объект не содержится в ячейке {@link Cell#bigObject}.
     */
    public Robot takeBigObject() {
        Robot result = bigObject;
        bigObject = null;
        return result;
    }

    /**
     * Получить крупный объект.
     * @return Крупный объект.
     */
    public CellObject getBigObject() { return bigObject; }

    /**
     * Может принять большой объект.
     * @return может принять большой объект.
     */
    public boolean canTakeBigObject() {
        return bigObject == null;
    }

    /*---------- СОСЕДНИЕ ЯЧЕЙКИ ----------*/
    /**
     * Соседние ячейки.
     */
    private final Map<Direction, Cell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Получить соседние ячейки {@link Cell#neighborCells}.
     * @return соседние ячейки.
     */
    public final Map<Direction, Cell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     * @param direction направление.
     * @return соседняя ячейка. null, если в заданном направлении нет соседней ячейки.
     */
    public Cell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    /**
     * Установить ячейку сосденей {@link Cell#neighborCells}.
     * @param neighborCell сосденяя ячейка.
     * @param direction направление.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     */
    void setNeighbor(@NotNull Cell neighborCell, @NotNull Direction direction) {
        if (neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell))
            throw new IllegalArgumentException();
        neighborCells.put(direction, neighborCell);
        if (neighborCell.getNeighborCell(direction.getOppositeDirection()) == null) {
            neighborCell.setNeighbor(this, direction.getOppositeDirection());
        }
    }

    /**
     * Получить направление с соседней ячейкой.
     * @param other соседняя ячейка.
     * @return направление.
     */
    public Direction getNeighborDirection(@NotNull Cell other) {
        for (var i : neighborCells.entrySet()) {
            if (i.getValue().equals(other)) return i.getKey();
        }
        return null;
    }

    /**
     * Является ли ячейка соседом.
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull Cell other) {
        return neighborCells.containsValue(other);
    }

    /*---------- ОБЪЕКТЫ МЕЖДУ ЯЧЕЙКАМИ ----------*/
    /**
     * Соседние объекты, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellObject> neighborBetweenCellObjects = new EnumMap<>(Direction.class);

    /**
     * Получить соседние объекты, располагающиеся между ячейками {@link Cell#neighborBetweenCellObjects}.
     * @return соседние объекты, располагающиеся между ячейками.
     */
    public Map<Direction, BetweenCellObject> getNeighborBetweenCellObjects() {
        return Collections.unmodifiableMap(neighborBetweenCellObjects);
    }

    /**
     * Получить соседний объект, располагающийся между ячейками {@link Cell#neighborBetweenCellObjects} в заданном направлении.
     * @param direction направление.
     * @return соседний объект, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellObject getNeighborBetweenCellObject(@NotNull Direction direction) {
        return neighborBetweenCellObjects.get(direction);
    }

    /**
     * Является ли ячейка соседом.
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull BetweenCellObject other) {
        return neighborBetweenCellObjects.containsValue(other);
    }

    /**
     * Установить объект, располагающийся между ячейками в заданном направлении.
     * @param betweenCellObject объект, располагающийся между ячейками.
     * @param direction направление.
     */
    public boolean setBetweenCellObject(@NotNull BetweenCellObject betweenCellObject, @NotNull Direction direction) {
        // Вернуть положительный результат, если уже установлена связь с этим объектом
        if (getNeighborBetweenCellObject(direction) == betweenCellObject) return true;

        // Вернуть отрицательный результат, если
        BetweenCellsPosition position = new BetweenCellsPosition(this, direction);
        if (getNeighborBetweenCellObject(direction) != null || // Уже установлена связь, но не с этим объектом
            // TODO: Попробовать убрать, уже была такая проверка.
            neighborBetweenCellObjects.containsKey(direction) || // Уже есть объект в этом направлении
            //TODO: попробовать убрать, т.к. эта проверка будет совершаться в объекте между ячейками.
            neighborBetweenCellObjects.containsValue(betweenCellObject) || //Этот объект уже имеет связь с этим объектом между ячеек, но в другом направлении
            !betweenCellObject.canSetAtPosition(position) // Проверяем, может ли объект между ячейками занимать позицию
        ) return false;

        // Запоминаем объект между ячейками
        neighborBetweenCellObjects.put(direction, betweenCellObject);

        // Получаем соседа
        Cell neighbor = getNeighborCell(direction);
        if (neighbor != null) {
            neighbor.setBetweenCellObject(betweenCellObject, direction.getOppositeDirection());
        }

        // Задать объекту между ячейками позицию
        return betweenCellObject.setPosition(position);
    }
}
