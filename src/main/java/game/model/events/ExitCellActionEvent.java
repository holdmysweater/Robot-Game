package game.model.events;

import game.model.field.core.ExitCell;

import java.util.EventObject;

/**
 * Объект события класса ячейки выхода {@link ExitCell}.
 */
public class ExitCellActionEvent extends EventObject {

    /**
     * Точка выхода.
     */
    private ExitCell teleport;

    /**
     * Получить точку выхода {@link ExitCellActionEvent#teleport}.
     *
     * @return точка выхода.
     */
    public ExitCell getTeleport() {
        return teleport;
    }

    /**
     * Установить точку выхода {@link ExitCellActionEvent#teleport}.
     *
     * @param teleport точка выхода.
     */
    public void setTeleport(ExitCell teleport) {
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
