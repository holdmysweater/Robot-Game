package game.model.events;

import game.model.field.core.ExitPoint;

import java.util.EventObject;

/**
 * Объект события класса ячейки выхода {@link ExitPoint}.
 */
public class ExitPointActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public ExitPointActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region ТОЧКА ВЫХОДА

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

    //endregion
}
