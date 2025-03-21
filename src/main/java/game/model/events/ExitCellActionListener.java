package game.model.events;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса ячейки выхода {@link game.model.field.cells.ExitCell}.
 */
public interface ExitCellActionListener extends EventListener {

    /**
     * Робот телепортировался.
     * @param event объект собтыия класса ячейки выхода.
     */
    void robotIsTeleported(@NotNull ExitCellActionEvent event);
}
