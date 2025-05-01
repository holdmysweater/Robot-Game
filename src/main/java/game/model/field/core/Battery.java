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
        charge = maxCapacity;
    }

    /**
     * Конструктор.
     * @throws IllegalArgumentException Введено некорректное значение заряда.
     */
    public Battery(int charge) {
        if (charge > maxCapacity || charge < 0) {
            throw new IllegalArgumentException("Charge must be greater than or equal to 0 and less than or equal to " + maxCapacity);
        }
        this.charge = charge;
    }

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
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (user == this.user) {
            return true;
        }

        if (isUsed() || getPosition() != null) {
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
    public boolean disconnect() {
        //TODO Проверки DONE
        assert !isDestroy;
        if (isDestroy) {
            return false;
        }

        if (!isUsed()) {
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
    protected boolean canSetPosition(@NotNull AbstractCell cell) {
        return !this.isUsed() && getPosition() == null;
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
