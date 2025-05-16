package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.field.core.Robot;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса робот {@link Robot}.
 */
public interface RobotActionListener extends EventListener {

    /**
     * Робот переместился.
     *
     * @param event объект события класса робот.
     */
    void robotIsMoved(@NotNull RobotActionEvent event);

    /**
     * Состояние заморозки робота {@link Robot#isUnfrozen()} изменилось.
     *
     * @param event объект события класса робот.
     */
    void robotUnfrozenChanged(@NotNull RobotActionEvent event);

    /**
     * Робот заменил источник питания.
     *
     * @param event объект события класса робот.
     */
    void robotChangedBattery(@NotNull RobotActionEvent event);
}
