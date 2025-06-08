package game.model.field.core;

/**
 * Видимый объект на поле.
 */
public class VisibleFieldObject extends FieldObject {

    //region АППРОКСИМИРУЮЩИЙ ПРЯМОУГОЛЬНИК

    /**
     * Аппроксимирующий прямоугольник объекта.
     */
    private ApproximatingRectangle approximatingRectangle = null;

    /**
     * Получить аппроксимирующий прямоугольник объект.
     * @return аппроксимирующий прямоугольник.
     */
    public ApproximatingRectangle getApproximatingRectangle() {
        return approximatingRectangle;
    }

    /**
     * Пересекается ли с другим объектом поля.
     *
     * @param other объект поля.
     * @return пересекаются ли объекты.
     */
    public boolean intersects(VisibleFieldObject other) {
        if (this.approximatingRectangle == null || other.approximatingRectangle == null) {
            return false;
        }
        return this.approximatingRectangle.intersects(other.approximatingRectangle);
    }

    //endregion

}
