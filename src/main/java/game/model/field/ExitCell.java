package game.model.field;

import org.jetbrains.annotations.NotNull;
import game.model.events.ExitCellActionEvent;
import game.model.events.ExitCellActionListener;
import game.model.field.cell_objects.Robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Ячейка точки выхода.
 */
public class ExitCell extends Cell {

    /*---------- ТЕЛЕПОРТАЦИЯ----------*/
    /**
     * Время ожидания перед телепортацией.
     */
    private static final int SLEEP_TIME = 1000;

    /**
     * Список телепортированных роботов.
     */
    private final List<Robot> teleportedRobots = new ArrayList<>();

    /**
     * Получить телепортированных роботов {@link ExitCell#teleportedRobots}.
     * @return список телепортированных роботов.
     */
    public List<Robot> getTeleportedRobots() {
        return Collections.unmodifiableList(teleportedRobots);
    }

    @Override
    public boolean setBigObject(@NotNull CellObject cellObject)  {
        // Может принять в себя только робота
        if(!(cellObject instanceof Robot)) return false;
        // Задать робота как в родительском классе
        if (super.setBigObject(cellObject)) {
            // TODO: использовался раньше BuildConfig, нужно наладить работу с ним.
            //if (BuildConfig.buildType == BuildConfig.BuildType.RELEASE) {
            //    Timer timer = new Timer(SLEEP_TIME, e -> teleportRobot());
            //    timer.setRepeats(false);
            //    timer.start();
            //} else {
                teleportRobot();
            //}
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Телепортировать робота.
     */
    private void teleportRobot() {
        Robot robot = (Robot) takeBigObject();
        teleportedRobots.add(robot);
        fireRobotIsTeleported();
    }

    /*---------- СЛУШАТЕЛИ ----------*/
    /**
     * Список слушателей, подписанных на события ячейки выхода.
     */
    private final ArrayList<ExitCellActionListener> exitCellListListener = new ArrayList<>();

    /**
     * Добавить нвоого слушателя за событиями ячейки выхода.
     * @param listener слушатель.
     */
    public void addExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями ячейки выхода.
     * @param listener слушатель.
     */
    public void removeExitCellActionListener(ExitCellActionListener listener) {
        exitCellListListener.remove(listener);
    }

    /**
     * Оповестить сулшателей {@link ExitCell#exitCellListListener}, что робот телепортирован.
     */
    private void fireRobotIsTeleported() {
        /* WARN{ Раньше было реализовано создание ивента для каждого слушателя и в контруктор передавался слушатель
        Теперь ивент создаётся один раз и в конструктор передаётся сам объект.}
        TODO: Перепроверить логику создания события и если необходимо доработать класс События.
        */
        ExitCellActionEvent event = new ExitCellActionEvent(this);
        event.setTeleport(this);
        for (ExitCellActionListener listener : exitCellListListener) {

            listener.robotIsTeleported(event);
        }
    }

    /*---------- МЕТОДЫ ОБЪЕКТОВ ----------*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExitCell exitCell = (ExitCell) o;
        return Objects.equals(teleportedRobots, exitCell.teleportedRobots) &&
                Objects.equals(exitCellListListener, exitCell.exitCellListListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return "ExitCell{" +
                "teleportedRobots=" + teleportedRobots +
                ", exitCellListListener=" + exitCellListListener +
                '}';
    }
}
