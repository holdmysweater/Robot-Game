package game.model.events;

import game.model.field.core.ExitPoint;

import java.util.EventObject;

/**
 * Объект события класса ячейки выхода {@link ExitPoint}.
 */
public class ExitPointActionEvent extends EventObject {

    /**
     * Точка выхода.
     */
    private ExitPoint teleport;

    /**
     * Получить точку выхода {@link ExitPointActionEvent#teleport}.
     *
     * @return точка выхода.
     */
    public ExitPoint getTeleport() {
        return teleport;
    }

    /**
     * Установить точку выхода {@link ExitPointActionEvent#teleport}.
     *
     * @param teleport точка выхода.
     */
    public void setTeleport(ExitPoint teleport) {
        this.teleport = teleport;
    }

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ExitPointActionEvent(Object source) {
        super(source);
    }
}
