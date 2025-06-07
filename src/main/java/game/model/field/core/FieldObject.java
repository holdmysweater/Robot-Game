package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Объект поля.
 */
public abstract class FieldObject {

    //region ПОЛЕ

    /**
     * Привязать объект к полю.
     * @param filed поле.
     */
    void setField(@NotNull Field filed) {
        this.field = filed;
    }

    /**
     * Поле на котором находится объект.
     */
    Field field;
    
    //endregion

    //region АППРОКСИМИРУЮЩИЙ ПРЯМОУГОЛЬНИК
    
    /**
     * Аппроксимирующий прямоугольник объекта.
     */
    ApproximatingRectangle approximatingRectangle = null;

    /**
     * Пересекается ли с другим объектом поля.
     *
     * @param other объект поля.
     * @return пересекаются ли объекты.
     */
    boolean intersects(FieldObject other) {
        if (this.approximatingRectangle == null || other.approximatingRectangle == null) {
            return false;
        }
        return this.approximatingRectangle.intersects(other.approximatingRectangle);
    }

    //endregion
}
