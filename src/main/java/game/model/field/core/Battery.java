package game.model.field.core;

import game.model.field.cell_objects.SmallCellObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Батарейка
 */
public class Battery extends SmallCellObject {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     */
    public Battery() {
        charge = maxCapacity;
    }

    /**
     * Конструктор.
     *
     * @throws IllegalArgumentException введено некорректное значение заряда.
     */
    public Battery(int charge) {
        if (charge > maxCapacity || charge < 0) {
            throw new IllegalArgumentException("Charge must be greater than or equal to 0 and less than or equal to " + maxCapacity);
        }
        this.charge = charge;
    }

    //endregion

    //region ЗАРЯД

    /**
     * Заряд.
     */
    private int charge = 0;

    /**
     * Максимальный заряд.
     */
    private static final int maxCapacity = 10;

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
     * Получить емкость {@link Battery#maxCapacity}.
     *
     * @return емкость.
     */
    public int getCapacity() {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        return maxCapacity;
    }

    /**
     * Отдать заряд.
     *
     * @param chargeAmount запрашиваемое кол-во заряда.
     * @return отданное кол-во заряда.
     */
    boolean drainCharge(int chargeAmount) {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        if (!isConnected()) {
            throw new RuntimeException("Not connected to user");
        }

        if (chargeAmount > charge) {
            return false;
        }

        charge -= chargeAmount;

        return true;
    }

    //endregion

    //region ПОЛЬЗОВАТЕЛЬ

    /**
     * Потребитель.
     */
    private Robot user = null;

    /**
     * Подключить к пользователю.
     *
     * @param user пользователь.
     * @return успешность подключения.
     */
    boolean connectTo(Robot user) {
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (user == this.user) {
            return true;
        }

        if (isConnected() || getPosition() != null) {
            return false;
        }

        this.user = user;

        boolean success = user.setBattery(this);

        assert success;
        if (!success) {
            this.user = null;
            return false;
        }

        return true;
    }

    /**
     * Отключить от пользователя.
     *
     * @return успешность отключения
     */
    boolean disconnect() {
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (!isConnected()) {
            return true;
        }

        Robot oldUser = user;
        user = null;

        boolean success = oldUser.unsetBattery();

        assert success;
        if (!success) {
            user = oldUser;
            return false;
        }

        isDestroy = false;

        return true;
    }

    /**
     * Подключение к потребителю.
     *
     * @return подключена ли батарейка к потребителю.
     */
    public boolean isConnected() {
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        return user != null;
    }

    //endregion

    //region ПОЗИЦИЯ

    @Override
    protected boolean canSetPosition(@NotNull Cell cell) {
        return !this.isConnected() && getPosition() == null;
    }

    //endregion

    //region УНИЧТОЖЕНИЕ

    /**
     * Батарейка уничтожена
     */
    private boolean isDestroy = false;

    /**
     * Уничтожение батарейки.
     */
    void destroy() {
        if (isDestroy()) return;

        disconnect();
        isDestroy = true;
    }

    /**
     * Является ли батарейка уничтоженной.
     *
     * @return является ли батарейка уничтоженной.
     */
    public boolean isDestroy() {
        return isDestroy;
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

        Battery battery = (Battery) o;

        if (getPosition() == null || battery.getPosition() == null) return false;

        return Objects.equals(charge, battery.charge);
    }

    @Override
    public String toString() {
        return "Battery{" + "charge=" + charge + '}';
    }

    //endregion
}
