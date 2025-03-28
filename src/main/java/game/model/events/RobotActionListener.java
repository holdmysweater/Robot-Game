package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.field.cell_objects.Robot;

import java.util.EventListener;

/**
 * Интерфейс слушателя события класса робот {@link game.model.field.cell_objects.Robot}.
 */
public interface RobotActionListener extends EventListener {

    /**
     * Робот переместился.
     * @param event объект события класса робот.
     */
    void robotIsMoved(@NotNull RobotActionEvent event);

    /**
     * Робот пропустил ход.
     * @param event объект события класса робот.
     */
    void robotSkippedStep(@NotNull RobotActionEvent event);

    /**
     * Состояние активности робота {@link Robot#isActive()} изменилось.
     * @param event объект события класса робот.
     */
    void robotActivityChanged(@NotNull RobotActionEvent event);

    /**
     * Робот заменил источник питания.
     * @param event объект события класса робот.
     */
    void robotChangedPowerSupply(@NotNull RobotActionEvent event);

    /**
     * Робот зарядил источник питания.
     * @param event объект события класса робот.
     */
    void robotChargedPowerSupply(@NotNull RobotActionEvent event);
}
