package game.model.events;

import game.model.field.core.AbstractCell;
import game.model.field.core.Battery;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Robot;

import java.util.EventObject;

/**
 * Объект события класса робот {@link Robot}.
 */
public class RobotActionEvent extends EventObject {

    /**
     * Робот.
     */
    private Robot robot;

    /**
     * Ячейка откуда переместился робот {@link RobotActionEvent#robot}.
     */
    private AbstractCell fromAbstractCell;

    /**
     * Ячейка куда переместился робот {@link RobotActionEvent#robot}.
     */
    private AbstractCell toAbstractCell;

    /**
     * Источник питания.
     */
    private Battery battery;

    /**
     * Установить ячейку {@link RobotActionEvent#fromAbstractCell} откуда переместился робот {@link RobotActionEvent#robot}.
     *
     * @param fromAbstractCell ячейка откуда переместился робот.
     */
    public void setFromCell(AbstractCell fromAbstractCell) {
        this.fromAbstractCell = fromAbstractCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#fromAbstractCell} откуда переместился робот {@link RobotActionEvent#robot}.
     *
     * @return ячейка откуда переместился робот.
     */
    public AbstractCell getFromCell() {
        return fromAbstractCell;
    }

    /**
     * Установить ячейку {@link RobotActionEvent#toAbstractCell} куда переместился робот {@link RobotActionEvent#robot}.
     *
     * @param toAbstractCell ячейка куда переместился робот.
     */
    public void setToCell(AbstractCell toAbstractCell) {
        this.toAbstractCell = toAbstractCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#toAbstractCell} куда переместился робот {@link RobotActionEvent#robot}.
     *
     * @return ячейка куда переместился робот.
     */
    public AbstractCell getToCell() {
        return toAbstractCell;
    }

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

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public RobotActionEvent(Object source) {
        super(source);
    }
}
