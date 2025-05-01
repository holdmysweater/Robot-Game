package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Класс позиции между ячейками {@link AbstractCell}
 */
public class BetweenCellsArea {

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
        if (!canSetHorizontalNeighbors(leftCell, rightCell)) return false;

        orientation = Orientation.VERTICAL;

        boolean success = true;

        if (leftCell != null){
            if (leftCell.setNeighborArea(Direction.EAST, this)) {
                this.neighborCells.put(Direction.WEST, leftCell);
            }
            else {
                success = false;
            }
        }

        if (rightCell != null){
            if (rightCell.setNeighborArea(Direction.WEST, this)) {
                this.neighborCells.put(Direction.EAST, rightCell);
            }
            else {
                success = false;
            }
        }

        return success;
    }

    /**
     * Установить соседей сверху и снизу.
     *
     * @param topCell    верхняя ячейка.
     * @param bottomCell нижняя ячейка.
     * @return успешность.
     */
    boolean setVerticalNeighbors(AbstractCell topCell, AbstractCell bottomCell) {
        if (!canSetVerticalNeighbors(topCell, bottomCell)) return false;

        orientation = Orientation.HORIZONTAL;

        boolean success = true;

        if (topCell != null){
            if (topCell.setNeighborArea(Direction.SOUTH, this)) {
                this.neighborCells.put(Direction.NORTH, topCell);
            }
            else {
                success = false;
            }
        }

        if (bottomCell != null){
            if (bottomCell.setNeighborArea(Direction.NORTH, this)) {
                this.neighborCells.put(Direction.SOUTH, bottomCell);
            }
            else {
                success = false;
            }
        }

        return success;
    }

    /**
     * Может ли установить соседей по горизонтали.
     *
     * @param leftCell  ячейка слева.
     * @param rightCell ячейка справа.
     * @return возможность соседства.
     */
    private boolean canSetHorizontalNeighbors(AbstractCell leftCell, AbstractCell rightCell) {
        AbstractCell currentLeftCell = this.neighborCells.get(Direction.WEST);
        AbstractCell currentRightCell = this.neighborCells.get(Direction.EAST);

        if (currentLeftCell != null && !currentLeftCell.equals(leftCell)) return false;
        if (currentRightCell != null && !currentRightCell.equals(rightCell)) return false;
        return this.neighborCells.get(Direction.NORTH) == null && this.neighborCells.get(Direction.SOUTH) == null;
    }

    /**
     * Может ли установить соседей по вертикали.
     *
     * @param topCell    верхняя ячейка.
     * @param bottomCell нижняя ячейка.
     * @return возможность соседства.
     */
    private boolean canSetVerticalNeighbors(AbstractCell topCell, AbstractCell bottomCell) {
        AbstractCell currentTopCell = this.neighborCells.get(Direction.NORTH);
        AbstractCell currantBottomCell = this.neighborCells.get(Direction.SOUTH);

        if (currentTopCell != null && !currentTopCell.equals(topCell)) return false;
        if (currantBottomCell != null && !currantBottomCell.equals(bottomCell)) return false;
        return this.neighborCells.get(Direction.WEST) == null && this.neighborCells.get(Direction.EAST) == null;
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
