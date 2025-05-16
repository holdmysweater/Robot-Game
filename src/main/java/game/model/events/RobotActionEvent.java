package game.model.events;

import game.model.field.core.Battery;
import game.model.field.core.Cell;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Robot;

import java.util.EventObject;

/**
 * Объект события класса робот {@link Robot}.
 */
public class RobotActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public RobotActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region РОБОТ

    /**
     * Робот.
     */
    private Robot robot;

    /**
     * Установить робота {@link RobotActionEvent#robot}.
     *
     * @param robot робот.
     */
    public void setRobot(@NotNull Robot robot) {
        this.robot = robot;
    }

    /**
     * Получить робота {@link RobotActionEvent#robot}.
     *
     * @return робот.
     */
    public Robot getRobot() {
        return robot;
    }

    //endregion

    //region СТАРАЯ ПОЗИЦИЯ

    /**
     * Ячейка, откуда переместился робот {@link RobotActionEvent#robot}.
     */
    private Cell fromCell;

    /**
     * Установить ячейку {@link RobotActionEvent#fromCell}, откуда переместился робот {@link RobotActionEvent#robot}.
     *
     * @param fromCell ячейка, откуда переместился робот.
     */
    public void setFromCell(Cell fromCell) {
        this.fromCell = fromCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#fromCell}, откуда переместился робот {@link RobotActionEvent#robot}.
     *
     * @return ячейка, откуда переместился робот.
     */
    public Cell getFromCell() {
        return fromCell;
    }

    //endregion

    //region НОВАЯ ПОЗИЦИЯ

    /**
     * Ячейка, куда переместился робот {@link RobotActionEvent#robot}.
     */
    private Cell toCell;

    /**
     * Установить ячейку {@link RobotActionEvent#toCell}, куда переместился робот {@link RobotActionEvent#robot}.
     *
     * @param toCell ячейка, куда переместился робот.
     */
    public void setToCell(Cell toCell) {
        this.toCell = toCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#toCell}, куда переместился робот {@link RobotActionEvent#robot}.
     *
     * @return ячейка, куда переместился робот.
     */
    public Cell getToCell() {
        return toCell;
    }

    //endregion

    //region БАТАРЕЙКА

    /**
     * Источник питания.
     */
    private Battery battery;

    /**
     * Установить источник питания {@link RobotActionEvent#battery}.
     *
     * @param battery источник питания.
     */
    public void setBattery(@NotNull Battery battery) {
        this.battery = battery;
    }

    /**
     * Получить источник питания {@link RobotActionEvent#battery}.
     *
     * @return источник питания.
     */
    public Battery getBattery() {
        return battery;
    }

    //endregion
}
