package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * Класс позиции между ячейками {@link Cell}
 */
public class BetweenCellsArea extends VisibleFieldObject {

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

        obstacle.setApproximatingRectangle(this.getApproximatingRectangle());

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

        success = success && this.setApproximatingRectangleForHorizontalNeighbors(leftCell, rightCell);

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

        success = success && this.setApproximatingRectangleForVerticalNeighbors(topCell, bottomCell);

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

    /**
     * Установить аппроксимирующий прямоугольник для области между левой и правой ячейками.
     *
     * @param leftCell  левая ячейка.
     * @param rightCell правая ячейка.
     * @return успешность установки.
     */
    private boolean setApproximatingRectangleForHorizontalNeighbors(Cell leftCell, Cell rightCell) {
        // Рассчитать значения аппроксимирующего прямоугольника
        int areaWidth;
        int areaHeight;
        int areaCenterX;
        int areaCenterY;

        // Задана левая и правая клетка
        if (rightCell != null && leftCell != null) {
            ApproximatingRectangle leftApproximationRectangle = leftCell.getApproximatingRectangle();
            ApproximatingRectangle rightApproximationRectangle = rightCell.getApproximatingRectangle();
            Point leftCellCenter = leftApproximationRectangle.getCenter();
            Point rightCellCenter = rightApproximationRectangle.getCenter();

            areaWidth = abs(leftCellCenter.getX() - rightCellCenter.getX()) -
                    (leftApproximationRectangle.getWidth() / 2 + rightApproximationRectangle.getWidth() / 2);
            areaHeight = max(leftApproximationRectangle.getHeight(), rightApproximationRectangle.getHeight());
            areaCenterX = (leftCellCenter.getX() + rightCellCenter.getX()) / 2;
            areaCenterY = (leftCellCenter.getY() + rightCellCenter.getY()) / 2;
        }

        // Задана только левая клетка
        else if (leftCell != null) {
            ApproximatingRectangle leftApproximationRectangle = leftCell.getApproximatingRectangle();
            Point leftCellCenter = leftApproximationRectangle.getCenter();
            areaWidth = 0;
            areaHeight = leftApproximationRectangle.getHeight();
            areaCenterX = leftCellCenter.getX() + (leftApproximationRectangle.getWidth() / 2);
            areaCenterY = leftCellCenter.getY();
        }

        // Задана только правая клетка
        else if (rightCell != null) {
            ApproximatingRectangle rightApproximationRectangle = rightCell.getApproximatingRectangle();
            Point rightCellCenter = rightApproximationRectangle.getCenter();
            areaWidth = rightApproximationRectangle.getWidth();
            areaHeight = 0;
            areaCenterX = rightCellCenter.getX() - (rightApproximationRectangle.getWidth() / 2);
            areaCenterY = rightCellCenter.getY();
        }

        // Не задана ни одна клетка
        else {
            return false;
        }

        // Установить рассчитанные значения
        return setApproximatingRectangle(areaCenterX, areaCenterY, areaWidth, areaHeight);
    }

    /**
     * Установить аппроксимирующий прямоугольник для области между верхней и нижней ячейками.
     *
     * @param topCell    верхняя ячейка.
     * @param bottomCell нижняя ячейка.
     * @return успешность установки.
     */
    private boolean setApproximatingRectangleForVerticalNeighbors(Cell topCell, Cell bottomCell) {
        // Рассчитать значения аппроксимирующего прямоугольника
        int areaWidth;
        int areaHeight;
        int areaCenterX;
        int areaCenterY;

        // Задана верхняя и нижняя клетка
        if (topCell != null && bottomCell != null) {
            ApproximatingRectangle topApproximationRectangle = bottomCell.getApproximatingRectangle();
            ApproximatingRectangle bottomApproximationRectangle = topCell.getApproximatingRectangle();
            Point topCellCenter = topApproximationRectangle.getCenter();
            Point bottomCellCenter = bottomApproximationRectangle.getCenter();

            areaWidth = max(topApproximationRectangle.getWidth(), bottomApproximationRectangle.getWidth());
            areaHeight = abs(topCellCenter.getY() - bottomCellCenter.getY()) -
                    (topApproximationRectangle.getHeight() / 2 + bottomApproximationRectangle.getHeight() / 2);
            areaCenterX = (topCellCenter.getX() + bottomCellCenter.getX()) / 2;
            areaCenterY = (topCellCenter.getY() + bottomCellCenter.getY()) / 2;
        }

        // Задана только нижняя клетка
        else if (bottomCell != null) {
            ApproximatingRectangle bottomApproximationRectangle = bottomCell.getApproximatingRectangle();
            Point bottomCellCenter = bottomApproximationRectangle.getCenter();
            areaWidth = bottomApproximationRectangle.getWidth();
            areaHeight = 0;
            areaCenterX = bottomCellCenter.getX();
            areaCenterY = bottomCellCenter.getY() - (bottomApproximationRectangle.getHeight() / 2);
        }

        // Задана только верхняя клетка
        else if (topCell != null) {
            ApproximatingRectangle topApproximationRectangle = topCell.getApproximatingRectangle();
            Point topCellCenter = topApproximationRectangle.getCenter();
            areaWidth = topApproximationRectangle.getWidth();
            areaHeight = 0;
            areaCenterX = topCellCenter.getX();
            areaCenterY = topCellCenter.getY() + (topApproximationRectangle.getHeight() / 2);
        }

        // Не задана ни одна клетка
        else {
            return false;
        }

        // Установить рассчитанные значения
        return setApproximatingRectangle(areaCenterX, areaCenterY, areaWidth, areaHeight);
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
