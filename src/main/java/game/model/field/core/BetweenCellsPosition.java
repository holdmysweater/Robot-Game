package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс позиции между ячейками {@link AbstractCell}
 */
public class BetweenCellsPosition { // TODO TODO сделать как область поля

    /**
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param cell         ячейка.
     * @param neighborCell соседняя ячейка.
     * @throws IllegalArgumentException если ячейки не являются соседними.
     */
    public BetweenCellsPosition(@NotNull AbstractCell cell, @NotNull AbstractCell neighborCell) {
        //TODO Проверки DONE
        Direction neighborDirection = cell.getNeighborDirection(neighborCell);

        if (neighborDirection == null) {
            throw new IllegalArgumentException();
        }

        neighborCells.put(neighborDirection, neighborCell);
        neighborCells.put(neighborDirection.getOppositeDirection(), cell);
    }

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param cell      ячейка.
     * @param direction направление.
     */
    public BetweenCellsPosition(@NotNull AbstractCell cell, @NotNull Direction direction) {
        //TODO Проверки DONE
        neighborCells.put(direction, cell);

        AbstractCell neighborCell = cell.getNeighborCell(direction);

        if (neighborCell != null) {
            neighborCells.put(direction.getOppositeDirection(), neighborCell);
        }
    }

    /**
     * Получить соседние ячейки {@link BetweenCellsPosition#neighborCells}.
     *
     * @return соседние ячейки.
     */
    public Map<Direction, AbstractCell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BetweenCellsPosition that = (BetweenCellsPosition) o;

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
}
