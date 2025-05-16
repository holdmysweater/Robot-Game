package game.model.events;

import game.model.field.core.CellObject;
import game.model.field.core.Field;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Robot;

import java.util.EventObject;

/**
 * Объект события класса поля {@link Field}.
 */
public class FieldActionEvent extends EventObject {

    /**
     * Объект в ячейке.
     */
    private CellObject cellObject;

    /**
     * Установить объект {@link FieldActionEvent#cellObject}.
     *
     * @param cellObject объект.
     */
    public void setCellObject(@NotNull CellObject cellObject) {
        this.cellObject = cellObject;
    }

    /**
     * Получить объект {@link FieldActionEvent#cellObject}.
     *
     * @return объект.
     */
    public CellObject getCellObject() {
        return cellObject;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public FieldActionEvent(Object source) {
        super(source);
    }
}
