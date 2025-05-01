package game.model.field.core;

import org.jetbrains.annotations.NotNull;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;

import java.util.ArrayList;

//TODO Полная валидация робота (перепроверить проверки и тд, я глянула, но глянь еще раз?)

/**
 * Робот.
 */
public class Robot extends CellObject {

    /**
     * Количество заряда для перемещения.
     */
    private static final int AMOUNT_OF_CHARGE_FOR_MOVE = 1;

    /**
     * Внутренний источник питания робота.
     */
    private Battery battery;

    /**
     * Робот телепортирован.
     */
    private boolean isTeleported = false;

    /**
     * Конструктор.
     *
     * @param battery внутренний источник питания.
     */
    public Robot(@NotNull Battery battery) {
        setBattery(battery);
    }

    /**
     * Переместить объект в заданном направлении.
     *
     * @param direction направление.
     */
    public boolean move(@NotNull Direction direction) {
        if (getPosition().getNeighborArea(direction).getObstacle() != null) {
            System.out.println("Wall");
            return false;
        }

        AbstractCell newPosition = getPosition().getNeighborCell(direction);

        if (newPosition == null || !newPosition.canSetBigObject()) {
            return false;
        }

        boolean success = battery.drainCharge(AMOUNT_OF_CHARGE_FOR_MOVE);

        if (!success) {
            return false;
        }

        AbstractCell oldPosition = getPosition();

        oldPosition.takeBigObject();
        if (!newPosition.setBigObject(this)) {
            throw new RuntimeException("Robot can't move to the " + newPosition);
        }

        fireRobotIsMoved(oldPosition, newPosition);

        return true;
    }

    @Override
    protected boolean canLocateAtPosition(@NotNull AbstractCell newPosition) {
        return getPosition() == null;
    }

    /**
     * Заменить источник питания {@link Robot#battery}.
     */
    public boolean changeBattery() {
        if (getPosition() instanceof ExitCell) {
            return false;
        }

        Battery battery = ((NormalCell) getPosition()).takeSmallObject();

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

    /**
     * Получить дееспособность робота
     *
     * @return дееспособен ли робот
     */
    public boolean isCapable() {
        if (getPosition() instanceof NormalCell) {
            NormalCell cell = (NormalCell) getPosition();
            if (cell.getSmallObject() != null) {
                return true;
            }
        }
        return !isTeleported() && getCharge() > 0;
    }

    /**
     * Телепортирован ли робот
     *
     * @return телепортирован ли робот
     */
    public boolean isTeleported() {
        return isTeleported;
    }

    /**
     * Считать, что робот телепортирован.
     */
    void setTeleported() { //TODO Плохо - может быть детелепортирован?? DONE
        isTeleported = true;
    }

    /**
     * Установить источник питания {@link Robot#battery}
     *
     * @param battery источник питания.
     */
    public boolean setBattery(@NotNull Battery battery) {
        // TODO TODO - можно сделать private, что будет при создании робота?
        if (battery == this.battery) return true;

        if (getBattery() != null) return false;

        this.battery = battery; // TODO TODO дальше что-то может пойти не так DONE

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
    public boolean unsetBattery() {
        // TODO TODO - можно сделать private
        if (getBattery() == null) return true;

        Battery battery = this.battery; // TODO TODO что-то может пойти не так
        this.battery = null;

        if (!battery.disconnect()) {
            throw new RuntimeException("Can't disconnect from user"); // TODO  TODO  почему исключение
        }

        return true;
    }

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

    /**
     * Получить источник питания {@link Robot#battery}.
     *
     * @return источник питания.
     */
    public Battery getBattery() {
        return this.battery;
    }
    //TODO Зачем??? (Ответ: тесты. Сделать пакетным?)
    //TODO TODO - переписать тесты так, чтобы рассматривать робота как черный ящик, использовать только getCharge()

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
    private void fireRobotIsMoved(@NotNull AbstractCell oldPosition, @NotNull AbstractCell newPosition) {
        RobotActionEvent event = new RobotActionEvent(this);
        event.setRobot(this);
        event.setFromCell(oldPosition);
        event.setToCell(newPosition);

        for (RobotActionListener listener : robotListListener) {
            listener.robotIsMoved(event);
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
}
