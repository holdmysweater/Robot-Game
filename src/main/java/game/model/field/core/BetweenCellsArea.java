package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс позиции между ячейками {@link AbstractCell}
 */
public class BetweenCellsArea {

    /*---------- ОБЪЕКТ МЕЖДУ ЯЧЕЙКАМИ ----------*/
    /**
     * Объект между ячейками, расположенный в ячейке.
     */
    private BetweenCellObject obstacle = null;

    /**
     * Добавить объект между ячейками в ячейку {@link BetweenCellsArea#obstacle}.
     *
     * @param obstacle объект, добавляемый в область между ячейками.
     */
    public boolean setObstacle(@NotNull BetweenCellObject obstacle) {
        // Вернуть true если этот объект уже задан
        if (this.obstacle == obstacle) return true;

        // Вернуть false если этот объект не может быть размещён в этой области
        if (!canTakeObstacle(obstacle)) return false;

        // Вернуть false если объект между ячеек не смог принять позицию
        if (!obstacle.setPosition(this)) return false;

        // Запомнить объект
        this.obstacle = obstacle;
        return true;
    }

    /**
     * Изъять объект между ячейками из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link BetweenCellsArea#obstacle}.
     */
    public BetweenCellObject takeObstacle() {
        BetweenCellObject result = obstacle;

        if (result != null) {
            result.setPosition(null);
            obstacle = null;
        }

        return result;
    }

    /**
     * Получить объект между ячейками.
     *
     * @return объект между ячейками.
     */
    public BetweenCellObject getObstacle() {
        return obstacle;
    }

    /**
     * Может приять объект между ячейками.
     *
     * @param obstacle объект между ячейками.
     * @return может приять объект.
     */
    protected boolean canTakeObstacle(BetweenCellObject obstacle) {
        return this.obstacle == null;
    }

    /*---------- РАСПОЛОЖЕНИЕ ОБЛАСТИ МЕЖДУ ЯЧЕЙЦКАМИ ----------*/
    /**
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Ориентация области на поле.
     */
    private Orientation orientation = null;

    /**
     * Конструктор класса позиции между ячейками.
     *
     * @param cell      ячейка.
     * @param direction направление.
     */
    public BetweenCellsArea(@NotNull AbstractCell cell, @NotNull Direction direction) {
        neighborCells.put(direction, cell);
        orientation = calcOrientation(direction);
    }

    boolean addNeighbor(@NotNull AbstractCell cell, @NotNull Direction direction) {
        // Вернуть false если новая клетка не соседствует с уже имеющейся
        for (AbstractCell neighborCell : neighborCells.values())
        {
            if (!cell.isNeighbor(neighborCell)) return false;
        }

        // Сохранить клетку
        neighborCells.put(direction, cell);
        return true;
    }

    /**
     * Получить соседние ячейки {@link BetweenCellsArea#neighborCells}.
     *
     * @return соседние ячейки.
     */
    public Map<Direction, AbstractCell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    public boolean isNeighbor(@NotNull AbstractCell cell) {
        return neighborCells.containsValue(cell);
    }

    /**
     * Рассчитывает ориентацию области по направлению соседских ячеек.
     *
     * @param direction направление.
     * @return ориентация.
     */
    private static Orientation calcOrientation(@NotNull Direction direction) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return Orientation.HORIZONTAL;
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            return Orientation.VERTICAL;
        } else {
            assert true: "Unknown orientation type: " + direction;
            return null;
        }
    }

    /**
     * Вернуть ориентацию поля.
     *
     * @return ориентация.
     */
    public Orientation getOrientation() {
        return orientation;
    }

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
}
