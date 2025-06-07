package game.model.field.core;

import org.jetbrains.annotations.NotNull;

public class ApproximatingRectangle {

    //region КОНСТРУКТОРЫ

    ApproximatingRectangle(Point center, int width, int height) {
        this.centerPoint = center;
        this.width = width;
        this.height = height;
    }

    //endregion

    //region ГЕТТЕРЫ

    /**
     * Центральная точка аппроксимирующего прямоугольника.
     */
    Point centerPoint;

    /**
     * Ширина аппроксимирующего прямоугольника.
     */
    int width;

    /**
     * Высота аппроксимирующего прямоугольника.
     */
    int height;

    /**
     * Получить центральную точку аппроксимирующего прямоугольника.
     *
     * @return центральная точка.
     */
    Point getCenter() {
        return centerPoint;
    }

    /**
     * Получить ширину прямоугольника.
     *
     * @return ширина.
     */
    int getWidth() {
        return width;
    }

    /**
     * Получить высоту прямоугольника.
     *
     * @return высота.
     */
    int getHeight() {
        return height;
    }

    /**
     * Получить северо-западную точку аппроксимирующего прямоугольника.
     *
     * @return точка прямоугольника.
     */
    Point getPointNorthWest() {
        return new Point(centerPoint.getX() - width / 2, centerPoint.getY() - height / 2);
    }

    /**
     * Получить юго-восточную точку аппроксимирующего прямоугольника.
     *
     * @return точка прямоугольника.
     */
    Point getPointSouthEast() {
        return new Point(centerPoint.getX() + width / 2, centerPoint.getY() + height / 2);
    }

    //endregion

    //region ДЕЙСТВИЯ

    //region ПЕРЕСЕЧЕНИЕ

    /**
     * Пересекается ли с другим аппроксимирующим прямоугольником.
     *
     * @param approximatingRectangle аппроксимирующий прямоугольник.
     * @return пресекаются ли прямоугольники.
     */
    boolean intersects(ApproximatingRectangle approximatingRectangle) {
        int dx = Math.abs(centerPoint.getX() - approximatingRectangle.centerPoint.getX());
        int dy = Math.abs(centerPoint.getY() - approximatingRectangle.centerPoint.getY());
        int intersectX = width / 2 + approximatingRectangle.width / 2;
        int intersectY = height / 2 + approximatingRectangle.height / 2;
        return (dx <= intersectX) && (dy <= intersectY);
    }

    //endregion

    //region ПЕРЕМЕЩЕНИЕ

    /**
     * Переместить аппроксимирующий прямоугольник.
     *
     * @param dx изменение координаты по ширине.
     * @param dy изменение координаты по высоте.
     */

    void move(int dx, int dy) {
        centerPoint = new Point(centerPoint.getX() + dx, centerPoint.getY() + dy);
    }

    /**
     * Переместить аппроксимирующий прямоугольник.
     *
     * @param direction направление.
     * @param delta     смещение.
     */
    void move(@NotNull Direction direction, int delta) {
        centerPoint = centerPoint.to(direction, delta);
    }

    //endregion

    //endregion
}
