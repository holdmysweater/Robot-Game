package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс позиции между ячейками {@link AbstractCell}
 */
public class BetweenCellsPosition {

    /**
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param abstractCell         ячейка.
     * @param neighborAbstractCell соседняя ячейка.
     * @throws IllegalArgumentException если ячейки не являются соседними.
     */
    public BetweenCellsPosition(@NotNull AbstractCell abstractCell, @NotNull AbstractCell neighborAbstractCell) {
        Direction neighborDirection = abstractCell.getNeighborDirection(neighborAbstractCell);

        if (neighborDirection == null) {
            throw new IllegalArgumentException();
        }

        neighborCells.put(neighborDirection, neighborAbstractCell);
        neighborCells.put(neighborDirection.getOppositeDirection(), abstractCell);
    }

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param abstractCell      ячейка.
     * @param direction направление.
     */
    public BetweenCellsPosition(@NotNull AbstractCell abstractCell, @NotNull Direction direction) {
        neighborCells.put(direction, abstractCell);

        AbstractCell neighborAbstractCell = abstractCell.getNeighborCell(direction);

        if (neighborAbstractCell != null) {
            neighborCells.put(direction.getOppositeDirection(), neighborAbstractCell);
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
