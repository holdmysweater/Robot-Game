package game.model.events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса поля {@link game.model.field.Field}.
 */
public interface FieldActionListener extends EventListener {
    /**
     * Робот телепортировался.
     * @param event объект события класса поля.
     */
    void robotIsTeleported(@NotNull FieldActionEvent event);
}
