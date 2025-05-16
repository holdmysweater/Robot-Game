package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.GameStatus;
import game.model.field.core.Robot;

import java.util.EventObject;

/**
 * Объект события класса игры {@link game.model.Game}.
 */
public class GameActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public GameActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region РОБОТ

    /**
     * Робот.
     */
    private Robot robot;

    /**
     * Установить робота {@link GameActionEvent#robot}.
     *
     * @param robot робот.
     */
    public void setRobot(@NotNull Robot robot) {
        this.robot = robot;
    }

    /**
     * Получить робота {@link GameActionEvent#robot}.
     *
     * @return робот.
     */
    public Robot getRobot() {
        return robot;
    }

    //endregion

    //region СТАТУС ИГРЫ

    /**
     * Статус игры.
     */
    private GameStatus status;

    /**
     * Получить статус игры {@link GameActionEvent#status}.
     *
     * @return статус игры.
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Установить статус игры {@link GameActionEvent#status}.
     *
     * @param status статус игры.
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    //endregion
}