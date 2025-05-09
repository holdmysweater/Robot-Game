package game.model.events;

import game.model.field.core.Cell;
import game.model.field.core.ExitCell;

import java.util.EventObject;

/**
 * Объект события класса ячейки выхода {@link ExitCell}.
 */
public class ExitCellActionEvent extends EventObject {

    /**
     * Ячейка выхода.
     */
    private Cell teleport;

    /**
     * Получить ячейку выхода {@link ExitCellActionEvent#teleport}.
     *
     * @return ячейка выхода.
     */
    public Cell getTeleport() {
        return teleport;
    }

    /**
     * Установить ячейку выхода {@link ExitCellActionEvent#teleport}.
     *
     * @param teleport ячейка выхода.
     */
    public void setTeleport(Cell teleport) {
        this.teleport = teleport;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ExitCellActionEvent(Object source) {
        super(source);
    }
}
