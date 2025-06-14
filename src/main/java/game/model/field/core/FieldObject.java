package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Объект поля.
 */
public abstract class FieldObject {

    //region ПОЛЕ

    /**
     * Поле на котором находится объект.
     */
    private Field field;

    /**
     * Привязать объект к полю.
     * @param filed поле.
     */
    void setField(@NotNull Field filed) {
        this.field = filed;
    }

    /**
     * Убрать объект с поля.
     */
    void removeItselfFromField() {
        field.removeObject(this);
        this.field = null;
    }

    /**
     * Получить поле.
     * @return поле.
     */
    protected Field getField() {
        return field;
    }
    
    //endregion

}
