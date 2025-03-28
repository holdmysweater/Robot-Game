package game.model.events;

import game.model.field.ExitCell;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса ячейки выхода {@link ExitCell}.
 */
public interface ExitCellActionListener extends EventListener {

    /**
     * Робот телепортировался.
     * @param event объект события класса ячейки выхода.
     */
    void robotIsTeleported(@NotNull ExitCellActionEvent event);
}
