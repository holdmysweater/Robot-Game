package game.model.events;

import game.model.field.core.ExitPoint;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса ячейки выхода {@link ExitPoint}.
 */
public interface ExitPointActionListener extends EventListener {

    /**
     * Робот телепортировался.
     *
     * @param event объект события класса ячейки выхода.
     */
    void robotIsTeleported(@NotNull ExitPointActionEvent event);
}
