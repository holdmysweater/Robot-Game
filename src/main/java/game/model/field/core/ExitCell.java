package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import game.model.events.ExitCellActionEvent;
import game.model.events.ExitCellActionListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Ячейка точки выхода.
 */
public class ExitCell extends AbstractCell {

    /*---------- ТЕЛЕПОРТАЦИЯ----------*/
    /**
     * Список телепортированных роботов.
     */
    private Robot teleportedRobot;

    /**
     * Получить телепортированного робота {@link ExitCell#teleportedRobot}.
     *
     * @return список телепортированного робота.
     */
    public Robot getTeleportedRobot() {
        return teleportedRobot;
    }

    @Override
    public boolean setBigObject(@NotNull Robot cellObject) {
        if (super.setBigObject(cellObject)) {
            teleportRobot();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Телепортировать робота.
     */
    private void teleportRobot() {
        teleportedRobot = takeBigObject();
        teleportedRobot.setTeleported(true);
        fireRobotIsTeleported();
    }

    /*---------- СЛУШАТЕЛИ ----------*/
    /**
     * Список слушателей, подписанных на события ячейки выхода.
     */
    private final ArrayList<ExitCellActionListener> exitCellListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями ячейки выхода.
     *
     * @param listener слушатель.
     */
    public void addExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями ячейки выхода.
     *
     * @param listener слушатель.
     */
    public void removeExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link ExitCell#exitCellListListener}, что робот телепортирован.
     */
    private void fireRobotIsTeleported() {
        ExitCellActionEvent event = new ExitCellActionEvent(this);
        event.setTeleport(this);

        for (ExitCellActionListener listener : exitCellListListener) {
            listener.robotIsTeleported(event);
        }
    }

    /*---------- МЕТОДЫ ОБЪЕКТОВ ----------*/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        ExitCell exitCell = (ExitCell) o;

        return Objects.equals(teleportedRobot, exitCell.teleportedRobot) &&
                Objects.equals(exitCellListListener, exitCell.exitCellListListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "ExitCell{" + "teleportedRobots=" + teleportedRobot + ", exitCellListListener=" + exitCellListListener + '}';
    }
}
