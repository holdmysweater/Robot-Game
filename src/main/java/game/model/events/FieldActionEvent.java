package game.model.events;

import game.model.field.core.Field;
import game.model.field.core.FieldObject;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * Объект события класса поля {@link Field}.
 */
public class FieldActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public FieldActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region ОБЪЕКТ ПОЛЯ

    /**
     * Объект в ячейке.
     */
    private FieldObject fieldObject;

    /**
     * Установить объект {@link FieldActionEvent#fieldObject}.
     *
     * @param fieldObject объект.
     */
    public void setFieldObject(@NotNull FieldObject fieldObject) {
        this.fieldObject = fieldObject;
    }

    /**
     * Получить объект {@link FieldActionEvent#fieldObject}.
     *
     * @return объект.
     */
    public FieldObject getFieldObject() {
        return fieldObject;
    }

    //endregion
}
