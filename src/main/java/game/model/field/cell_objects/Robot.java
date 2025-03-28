package game.model.field.cell_objects;

import game.model.field.*;
import org.jetbrains.annotations.NotNull;
import game.model.Direction;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;

import java.util.ArrayList;

/**
 * Робот.
 */
public class Robot extends CellObject {

    /**
     * Количество заряда для перемещения.
     */
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;

    /**
     * Количество заряда для пропуска хода.
     */
    private static final int AMOUNT_OF_CHARGE_FOR_SKIP_STEP = 2;

    /**
     * Внутренний источник питания робота.
     */
    private Battery battery;

    /**
     * Состояние замороженности робота.
     */
    private boolean isUnfrozen;

    /**
     * Констрктор.
     *
     * @param battery внутренний источник питания.
     */
    public Robot(@NotNull Battery battery) {
        this.battery = battery;
    }

    /**
     * Переместить объект в заданном направлении.
     *
     * @param direction направление.
     */
    public boolean move(@NotNull Direction direction) {
        if (!isUnfrozen()) {
            return false;
        }

        if (position.getNeighborObstacle(direction) != null) {
            return false;
        }

        Cell newPosition = position.getNeighborCell(direction);

        if (!newPosition.canTakeBigObject()) {
            return false;
        }

        boolean success = battery.releaseCharge(AMOUNT_OF_CHARGE_FOR_MOVE);

        if (!success) {
            return false;
        }

        Cell oldPosition = position;
        position = null;

        oldPosition.takeBigObject();

        success = newPosition.setBigObject(this);

        if (!success) {
            return false;
        }

        fireRobotIsMoved(oldPosition, newPosition);
        return true;
    }

    @Override
    public boolean canLocateAtPosition(@NotNull Cell newPosition) {
        if ((newPosition instanceof ExitCell) && (((ExitCell) newPosition).getTeleportedRobots().contains(this))) {
            return false;
        }

        return newPosition.getBigObject() == null;
    }

    /**
     * Заменить источник питания {@link Robot#battery}.
     */
    public boolean changeBattery() {
        if (!isUnfrozen()) {
            return false;
        }

        if (position instanceof ExitCell) {
            return false;
        }

        Battery battery = ((NormalCell) position).takeSmallObject();

        if (battery == null) {
            return false;
        }

        Battery oldBattery = this.battery;
        this.battery = null;

        boolean success = battery.connect(this);

        if (!success) {
            this.battery = oldBattery;
            return false;
        }

        oldBattery.disconnect();
        oldBattery.destroy();

        fireRobotChangeBattery(battery);

        return success;
    }

    /**
     * Разморозить робота {@link Robot#isUnfrozen}.
     *
     * @param value состояние разморозки.
     */
    public void setUnfrozen(boolean value) {
        isUnfrozen = value;
        fireRobotChangeActive();
    }

    /**
     * Получить состояние активности робота {@link Robot#isUnfrozen}.
     *
     * @return состояние размороженности робота.
     */
    public boolean isUnfrozen() {
        return isUnfrozen;
    }

    /**
     * Получить дееспособность робота
     *
     * @return дееспособен ли робот
     */
    public boolean isCapable() {
        return !isTeleported() && getCharge() > 0;
    }

    public boolean isTeleported() {
        return position != null && position instanceof ExitCell;
    }

    /**
     * Установить источник питания {@link Robot#battery}
     *
     * @param battery источник питания.
     */
    public boolean setBattery(@NotNull Battery battery) {
        if (battery == this.battery) return true;

        if (getBattery() != null) return false;

        this.battery = battery;

        if (!battery.connect(this)) {
            throw new RuntimeException("Can't connect to battery");
        }

        return true;
    }

    /**
     * Изъять источник питания {@link Robot#battery}
     *
     * @return успешность изъятия
     */
    public boolean unsetBattery() {
        if (getBattery() == null) return true;

        Battery battery = this.battery;
        this.battery = null;

        if (!battery.disconnect()) {
            throw new RuntimeException("Can't disconnect from user");
        }

        return true;
    }

    /**
     * Получить заряд {@link Battery#getCharge()}.
     *
     * @return заряд.
     */
    public Integer getCharge() {
        return battery.getCharge();
    }

    /**
     * Получить максимальный заряд {@link Battery#getMaxCharge()}.
     *
     * @return максимальный заряд.
     */
    public Integer getMaxCharge() {
        return battery.getMaxCharge();
    }

    /**
     * Получить источник питания {@link Robot#battery}.
     *
     * @return источник питания.
     */
    public Battery getBattery() {
        return this.battery;
    }

    /**
     * Список слушателей, подписанных на события игры.
     */
    private final ArrayList<RobotActionListener> robotListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void addRobotActionListener(RobotActionListener listener) {
        robotListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void removeRobotActionListener(RobotActionListener listener) {
        robotListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что робот переместился.
     *
     * @param oldPosition ячейка откуда переместился робот.
     * @param newPosition ячейка куда переместился робот.
     */
    private void fireRobotIsMoved(@NotNull Cell oldPosition, @NotNull Cell newPosition) {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            event.setFromCell(oldPosition);
            event.setToCell(newPosition);
            listener.robotIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что состояние активности робота изменилось.
     */
    private void fireRobotChangeActive() {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            listener.robotActivityChanged(event);
        }
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что робот сменил источник питания.
     *
     * @param battery новый источник питания.
     */
    private void fireRobotChangeBattery(Battery battery) {
        for (RobotActionListener listener : robotListListener) {
            RobotActionEvent event = new RobotActionEvent(listener);
            event.setRobot(this);
            event.setBattery(battery);
            listener.robotChangedPowerSupply(event);
        }
    }
}
