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

}
