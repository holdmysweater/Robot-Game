package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Батарейка
 */
public class Battery extends CellObject {

    /**
     * Конструктор.
     */
    public Battery() {
        charge = maxCharge;
    }

    /**
     * Конструктор.
     */
    public Battery(int charge) {
        this.charge = charge;
    } // TODO проверка

    /**
     * Батарейка уничтожена
     */
    private boolean isDestroy = false;

    public boolean isDestroy() {
        return isDestroy;
    }

    /**
     * Уничтожение батарейки.
     */
    public void destroy() {
        if(isDestroy()) return; // TODO - ничего не делать, если уже разрушена

        disconnect();
        isDestroy = true;
    }
    //TODO Связь с потребителем DONE

    /**
     * Потребитель.
     */
    private Robot user = null;

    /**
     * Подключение к потребителю.
     *
     * @return подключена ли батарейка к потребителю.
     */
    public boolean isUsed() {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        return user != null;
    }

    /**
     * Подключить к пользователю.
     *
     * @param user пользователь.
     * @return успешность подключения.
     */
    public boolean connectTo(Robot user) {
        //TODO TODO Проверки DONE - не должна находиться в ячейке
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (user == this.user) {
            return true;
        }

        if (isUsed()) {
            return false;
        }

        this.user = user; // TODO TODO - позже может что-то пойти не так

        boolean success = user.setBattery(this);

        assert success;
        if (!success) {
            return false;
        }

        return true;
    }

    /**
     * Отключить от пользователя.
     *
     * @return успешность отключения
     */
    public boolean disconnect() {
        //TODO Проверки DONE
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (!isUsed()) {
            return true;
        }

        Robot oldUser = user; // TODO TODO - что-то может пойти не так
        user = null;

        boolean success = oldUser.unsetBattery();

        assert success;
        if (!success) {
            return false;
        }

        isDestroy = false;

        return true;
    }


    /**
     * Заряд.
     */
    private int charge = 0;

    /**
     * Максимальный заряд.
     */
    private static final int maxCharge = 10; // TODO - capacity

    /**
     * Получить заряд {@link Battery#charge}.
     *
     * @return заряд.
     */
    public int getCharge() {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        return charge;
    }

    /**
     * Получить емкость {@link Battery#maxCharge}.
     *
     * @return емкость.
     */
    public int getCapacity() {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        return maxCharge;
    }

    /**
     * Отдать заряд.
     *
     * @param chargeAmount запрашиваемое кол-во заряда.
     * @return отданное кол-во заряда.
     */
    public boolean drainCharge(int chargeAmount) {
        //TODO TODO Название  операции DONE?
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        if (!isUsed()) {
            throw new RuntimeException("Not connected to user");
        }

        if (chargeAmount > charge) {
            return false;
        }

        charge -= chargeAmount;

        return true;
    }


    @Override
    protected boolean canLocateAtPosition(@NotNull AbstractCell cell) {
        //TODO TODO - переделать,  что чисто  позиция
        if (isDestroy || !(cell instanceof NormalCell)) {
            return false;
        }

        assert cell instanceof NormalCell; // TODO TODO
        Battery smallObjectInCell = ((NormalCell) cell).getSmallObject();

        return smallObjectInCell == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Battery battery = (Battery) o;

        if (getPosition() == null || battery.getPosition() == null) return false;

        return Objects.equals(charge, battery.charge);
    }

    @Override
    public String toString() {
        return "Battery{" + "charge=" + charge + '}';
    }
}
