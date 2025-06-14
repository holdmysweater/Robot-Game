package game.model.events;

import game.model.field.core.Field;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса поля {@link Field}.
 */
public interface FieldActionListener extends EventListener {

    /**
     * Робот телепортировался.
     *
     * @param event объект события класса поля.
     */
    void robotIsTeleported(@NotNull FieldActionEvent event);

    /**
     * Создан объект.
     *
     * @param event объект события класса поля.
     */
    void objectWasCreated(@NotNull FieldActionEvent event);

    /**
     * Удалён объект.
     *
     * @param event объект события класса поля.
     */
    void objectWasDestroyed(@NotNull FieldActionEvent event);
}
