package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Класс позиции между ячейками {@link Cell}
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
    public boolean setObstacle(@NotNull BetweenCellObject obstacle) {
        // Вернуть true, если этот объект уже задан
        if (this.obstacle == obstacle) return true;

        // Вернуть false, если этот объект не может быть размещён в этой области
        if (!canSetObstacle()) return false;

        // Вернуть false, если объект между ячеек не смог принять позицию
        if (!obstacle.setPosition(this)) return false;

        // Запомнить объект
        this.obstacle = obstacle;
        return true;
    }

    /**
     * Может принять препятствие.
     *
     * @return может приять препятствие.
     */
    private boolean canSetObstacle() {
        return this.obstacle == null;
    }

    //endregion

    //region СОСЕДИ

    /**
     * Соседние ячейки.
     */
    private final Map<Direction, Cell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка в заданном направлении.
     */
    public Cell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }

    /**
     * Установить соседей слева и справа.
     *
     * @param leftCell  ячейка слева.
     * @param rightCell ячейка справа.
     * @return успешность.
     */
    boolean setHorizontalNeighbors(Cell leftCell, Cell rightCell) {
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
    boolean setVerticalNeighbors(Cell topCell, Cell bottomCell) {
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
    private boolean canSetHorizontalNeighbors(Cell leftCell, Cell rightCell) {
        Cell currentLeftCell = this.neighborCells.get(Direction.WEST);
        Cell currentRightCell = this.neighborCells.get(Direction.EAST);

        if (currentLeftCell != null && !currentLeftCell.equals(leftCell)) return false;
        if (currentRightCell != null && !currentRightCell.equals(rightCell)) return false;
        return orientation == null || orientation == Orientation.VERTICAL;
    }

    /**
     * Может ли установить соседей по вертикали.
     *
     * @param topCell    верхняя ячейка.
     * @param bottomCell нижняя ячейка.
     * @return возможность соседства.
     */
    private boolean canSetVerticalNeighbors(Cell topCell, Cell bottomCell) {
        Cell currentTopCell = this.neighborCells.get(Direction.NORTH);
        Cell currantBottomCell = this.neighborCells.get(Direction.SOUTH);

        if (currentTopCell != null && !currentTopCell.equals(topCell)) return false;
        if (currantBottomCell != null && !currantBottomCell.equals(bottomCell)) return false;
        return orientation == null || orientation == Orientation.HORIZONTAL;
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
