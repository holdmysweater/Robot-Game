package game.model.field.core;

import game.Debug;
import game.model.field.cell_objects.ICollidingObject;
import game.model.field.cell_objects.LowProfileCellObject;
import game.model.field.cell_objects.SelfActivatingCellObject;
import game.model.field.cell_objects.SmallCellObject;
import org.jetbrains.annotations.NotNull;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;

import java.util.ArrayList;

/**
 * Робот.
 */
public class Robot extends SmallCellObject implements ICollidingObject {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     *
     * @param battery внутренний источник питания.
     */
    public Robot(@NotNull Battery battery) {
        setBattery(battery);
        isUnfrozen = false;
    }

    //endregion

    //region РАЗМЕРЫ АПРОКСИМИРУЮЩЕГО ПРЯМОУГОЛЬНИКА

    static {
        DEFAULT_WIDTH = 60;
        DEFAULT_HEIGHT = 60;
    }

    //endregion

    //region ЗАМОРОЗКА

    /**
     * Состояние заморозки робота.
     */
    private boolean isUnfrozen;

    /**
     * Разморозить робота {@link Robot#isUnfrozen}.
     *
     * @param value состояние разморозки.
     */
    public void setUnfrozen(boolean value) {
        isUnfrozen = value;
        fireRobotChangeUnfrozen();
    }

    /**
     * Получить состояние заморозки робота {@link Robot#isUnfrozen}.
     *
     * @return состояние заморозки робота.
     */
    public boolean isUnfrozen() {
        return isUnfrozen;
    }

    //endregion

    //region ПЕРЕМЕЩЕНИЕ

    private static final int SPEED = 1;

    private Cell oldPosition, newPosition;

    @Override
    public boolean move() {
        if (!isUnfrozen() || !isCapable() || _mobility.getMovementDirection() == null) {
            return false;
        }

        if (getDepartCellPosition() != null && !getDepartCellPosition().intersects(this)) {
            CellObject object = getDepartCellPosition().takeObject(SmallCellObject.class);

            if (object != this) {
                throw new RuntimeException("Couldn't take object from departing cell");
            }
        }

        if (getArrivalCellPosition() != null && getArrivalCellPosition().hasSameCenter(this)) {
            _mobility.stopMoving();

            boolean success = newPosition.updateMobileObjectStatus(this, CellObjectStatus.IDLE);
            if (!success) {
                throw new RuntimeException("Couldn't update robot status to IDLE");
            }

            fireRobotIsMoved(oldPosition, newPosition);
            oldPosition = newPosition = null;

            // TODO move this logic into a manager
            if (getIdleCellPosition().getObject(SelfActivatingCellObject.class) != null) {
                SelfActivatingCellObject object = (SelfActivatingCellObject) getIdleCellPosition().getObject(SelfActivatingCellObject.class);
                object.execute(this);
            }

            Debug.log(Debug.Options.RobotMoveFinished, "Robot arrived in cell and stopped moving");

            return true;
        }

        return _mobility.move();
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        return this._mobility.startMoving(direction, speed);
    }

    /**
     * Переместить объект в заданном направлении.
     *
     * @param direction направление.
     */
    public boolean move(@NotNull Direction direction) {
        if (!isUnfrozen() || !isCapable() || getIdleCellPosition() == null) {
            return false;
        }

        newPosition = getIdleCellPosition().getNeighborCell(direction);

        if (newPosition == null || !newPosition.canSetObject(this.getClass())) {
            return false;
        }

        boolean success = battery.drainCharge(AMOUNT_OF_CHARGE_FOR_MOVE);

        if (!success) {
            return false;
        }

        oldPosition = getIdleCellPosition();

        success = oldPosition.updateMobileObjectStatus(this, CellObjectStatus.DEPARTING);

        if (!success) {
            throw new RuntimeException("Could not update mobile object status");
        }

        success = newPosition.setObject(this, CellObjectStatus.ARRIVING);

        if (!success) {
            throw new RuntimeException("Robot can't move to the " + newPosition);
        }

        success = startMoving(direction, SPEED);

        if (!success) {
            throw new RuntimeException("Could not start moving to " + newPosition);
        }

        Debug.log(Debug.Options.RobotMoveStarted, "Robot started moving to " + direction);

        return true;
    }

    /**
     * Получить дееспособность робота
     *
     * @return дееспособен ли робот
     */
    public boolean isCapable() {
        return (getIdleCellPosition() != null || getArrivalCellPosition() != null) && !isTeleported() && getCharge() > 0;
    }

    //endregion

    //region БАТАРЕЙКА

    /**
     * Количество заряда для перемещения.
     */
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;

    /**
     * Внутренний источник питания робота.
     */
    private Battery battery;

    /**
     * Установить источник питания {@link Robot#battery}
     *
     * @param battery источник питания.
     */
    boolean setBattery(@NotNull Battery battery) {
        if (battery == this.battery) return true;

        if (this.battery != null) return false;

        this.battery = battery;

        boolean success = battery.connectTo(this);

        assert success;
        if (!success) {
            this.battery = null;
            return false;
        }

        return true;
    }

    /**
     * Изъять источник питания {@link Robot#battery}
     *
     * @return успешность изъятия
     */
    boolean unsetBattery() {
        if (this.battery == null) return true;

        Battery battery = this.battery;
        this.battery = null;

        boolean success = battery.disconnect();

        assert success: "Disconnect failed";
        if (!success) {
            this.battery = battery;
            return false;
        }

        return true;
    }

    /**
     * Заменить источник питания {@link Robot#battery}.
     */
    public boolean changeBattery() {
        if (!isUnfrozen() || isTeleported() || getIdleCellPosition() == null) {
            return false;
        }

        Battery battery;

        try {
            battery = (Battery) getIdleCellPosition().takeObject(LowProfileCellObject.class);
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }

        if (battery == null) {
            return false;
        }

        Battery oldBattery = this.battery;
        unsetBattery();

        boolean success = setBattery(battery);

        if (!success) {
            setBattery(oldBattery);
            return false;
        }

        oldBattery.destroy();

        fireRobotChangeBattery(battery);

        return true;
    }

    //region ЁМКОСТЬ

    /**
     * Получить заряд {@link Battery#getCharge()}.
     *
     * @return заряд.
     */
    public int getCharge() {
        return battery.getCharge();
    }

    /**
     * Получить емкость заряда {@link Battery#getCapacity()}.
     *
     * @return емкость заряда.
     */
    public int getChargeCapacity() {
        return battery.getCapacity();
    }

    //endregion

    //endregion

    //region ОБЪЕКТ С КОЛЛИЗИЕЙ

    @Override
    public void processCollisionWith(@NotNull ICollidingObject object) {
        // not implemented
    }

    //endregion

    //region ТЕЛЕПОРТАЦИЯ

    /**
     * Робот телепортирован.
     */
    private boolean isTeleported = false;

    /**
     * Считать, что робот телепортирован.
     */
    void setTeleported() {
        isTeleported = true;
    }

    /**
     * Телепортирован ли робот
     *
     * @return телепортирован ли робот
     */
    public boolean isTeleported() {
        return isTeleported;
    }

    //endregion

    //region СИГНАЛЫ

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
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);
        event.setFromCell(oldPosition);
        event.setToCell(newPosition);

        for (RobotActionListener listener : robotListListener) {
            listener.robotIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что состояние заморозки робота изменилось.
     */
    private void fireRobotChangeUnfrozen() {
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);

        for (RobotActionListener listener : robotListListener) {
            listener.robotUnfrozenChanged(event);
        }
    }

    /**
     * Оповестить слушателей {@link Robot#robotListListener}, что робот сменил источник питания.
     *
     * @param battery новый источник питания.
     */
    private void fireRobotChangeBattery(Battery battery) {
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);
        event.setBattery(battery);

        for (RobotActionListener listener : robotListListener) {
            listener.robotChangedBattery(event);
        }
    }

    //endregion
}