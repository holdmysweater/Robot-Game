package game.model.field.cell_objects;

import game.model.field.CellObject;
import org.jetbrains.annotations.NotNull;
import game.model.field.Cell;

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
     * Заряд.
     */
    private int charge = 0;

    /**
     * Максимальный заряд.
     */
    private static final int maxCharge = 10;

    /**
     * Потребитель.
     */
    private Robot user = null;

    /**
     * Батарейка функциональна (не уничтожена) // TODO подумать над формулировкой
     */
    private boolean isFunctional = true;

    /**
     * Получить заряд {@link Battery#charge}.
     * @return заряд.
     */
    public int getCharge() {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        return charge;
    }

    /**
     * Получить максимальный заряд {@link Battery#maxCharge}.
     * @return максимальный заряд.
     */
    public int getMaxCharge() {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        return maxCharge;
    }

    /**
     * Отдать заряд.
     * @param chargeAmount запрашиваемое кол-во заряда.
     * @return отданное кол-во заряда.
     */
    public boolean releaseCharge(int chargeAmount) {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        if (!isConnectedToUser()) throw new RuntimeException("Not connected to user");

        if(chargeAmount > charge) return false;
        charge -= chargeAmount;
        return true;
    }

    /**
     * Подключение к потребителю.
     * @return подключена ли батарейка к потребителю.
     */
    public boolean isConnectedToUser() {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        return user != null;
    }

    /**
     * Подключить к пользователю.
     * @param user пользователь.
     * @return успешность подключения.
     */
    public boolean connect(Robot user) {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        if (user == this.user) return true;

        if (isConnectedToUser()) return false;

        this.user = user;

        if (!user.setBattery(this)) {
            throw new RuntimeException("Can't connect to user");
        }

        return true;
    }

    /**
     * Отключить от пользователя.
     * @return успешность отключения
     */
    public boolean disconnect() {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        if (!isConnectedToUser()) return true;

        Robot user = this.user;
        this.user = null;

        if (!user.unsetBattery()) {
            throw new RuntimeException("Can't disconnect from user");
        }

        isFunctional = false;

        return true;
    }

    @Override
    public boolean canLocateAtPosition(@NotNull Cell cell) {
        if (!isFunctional) throw new RuntimeException("Battery is destroyed");

        return false;  // TODO Battery canLocateAtPosition()
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Battery battery = (Battery) o;
        return Objects.equals(charge, battery.charge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charge);
    }

    @Override
    public String toString() {
        return "Battery{" + "charge=" + charge + '}';
    }
}
