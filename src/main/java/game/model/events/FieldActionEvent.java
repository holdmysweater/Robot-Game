package game.model.events;

import game.model.field.core.ExitPoint;
import game.model.field.core.Field;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Robot;

import java.util.EventObject;

/**
 * Объект события класса поля {@link Field}.
 */
public class FieldActionEvent extends EventObject {

    /**
     * Робот.
     */
    private Robot robot;

    /**
     * Установить робота {@link FieldActionEvent#robot}.
     *
     * @param robot робот.
     */
    public void setRobot(@NotNull Robot robot) {
        this.robot = robot;
    }

    /**
     * Получить робота {@link FieldActionEvent#robot}.
     *
     * @return робот
     */
    public Robot getRobot() {
        return robot;
    }

    /**
     * Точка выхода.
     */
    private ExitPoint teleport;

    /**
     * Получить точку выхода {@link FieldActionEvent#teleport}.
     *
     * @return точка выхода.
     */
    public ExitPoint getTeleport() {
        return teleport;
    }

    /**
     * Установить точка выхода {@link FieldActionEvent#teleport}.
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
    public FieldActionEvent(Object source) {
        super(source);
    }
}
