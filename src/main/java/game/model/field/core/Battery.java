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
        isDestroy = true;
    }
    //TODO Связь с потребителем

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
        //TODO Проверки
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        if (user == this.user) {
            return true;
        }

        if (isUsed()) {
            return false;
        }

        this.user = user;

        if (!user.setBattery(this)) {
            throw new RuntimeException("Can't connect to user");
        }

        return true;
    }

    /**
     * Отключить от пользователя.
     *
     * @return успешность отключения
     */
    public boolean disconnect() {
        //TODO Проверки
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        if (!isUsed()) {
            return true;
        }

        Robot oldUser = user;
        user = null;

        if (!oldUser.unsetBattery()) {
            throw new RuntimeException("Can't disconnect from user");
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
    private static final int maxCharge = 10;

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
    public boolean releaseCharge(int chargeAmount) {
        //TODO Название операции
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
    public boolean canLocateAtPosition(@NotNull AbstractCell cell) {
        //TODO Уровень доступа
        if (isDestroy) {
            throw new RuntimeException("Battery is destroyed");
        }

        if (!(cell instanceof NormalCell)) {
            return false;
        }

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
