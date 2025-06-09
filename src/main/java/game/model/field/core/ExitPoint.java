package game.model.field.core;

import game.model.field.cell_objects.SelfActivatingCellObject;
import game.model.field.cell_objects.SmallCellObject;
import org.jetbrains.annotations.NotNull;
import game.model.events.ExitPointActionEvent;
import game.model.events.ExitPointActionListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Ячейка точки выхода.
 */
public class ExitPoint extends SelfActivatingCellObject {

    //region ДЕЙСТВИЯ

    @Override
    public void execute(SmallCellObject object) {
        if (teleportedRobot != null) return;
        teleportRobot(object);
    }

    //endregion

    //region ПОЗИЦИЯ

    @Override
    protected boolean canSetPosition(@NotNull Cell cell) {
        return getPosition() == null;
    }

    //endregion

    //region ТЕЛЕПОРТАЦИЯ

    /**
     * Телепортированный робот.
     */
    private Robot teleportedRobot;

    /**
     * Телепортировать робота.
     */
    private void teleportRobot(SmallCellObject object) {
        teleportedRobot = (Robot) object;
        teleportedRobot.setTeleported();
        fireRobotIsTeleported();
    }

    /**
     * Получить телепортированного робота {@link ExitPoint#teleportedRobot}.
     *
     * @return телепортированный робот.
     */
    public Robot getTeleportedRobot() {
        return teleportedRobot;
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события ячейки выхода.
     */
    private final ArrayList<ExitPointActionListener> exitPointListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями ячейки выхода.
     *
     * @param listener слушатель.
     */
    public void addExitPointActionListener(ExitPointActionListener listener) {
        exitPointListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями ячейки выхода.
     *
     * @param listener слушатель.
     */
    public void removeExitPointActionListener(ExitPointActionListener listener) {
        exitPointListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link ExitPoint#exitPointListListener}, что робот телепортирован.
     */
    private void fireRobotIsTeleported() {
        ExitPointActionEvent event = new ExitPointActionEvent(this);
        event.setTeleport(this);

        for (ExitPointActionListener listener : exitPointListListener) {
            listener.robotIsTeleported(event);
        }
    }

    //endregion

    //region OBJECT

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

        ExitPoint exitPoint = (ExitPoint) o;

        return Objects.equals(teleportedRobot, exitPoint.teleportedRobot) &&
                Objects.equals(exitPointListListener, exitPoint.exitPointListListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "ExitPoint{" + "teleportedRobots=" + teleportedRobot + ", exitCellListListener=" + exitPointListListener + '}';
    }

    //endregion
}
