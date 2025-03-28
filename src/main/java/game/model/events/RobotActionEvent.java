package game.model.events;

import game.model.field.cell_objects.Battery;
import org.jetbrains.annotations.NotNull;
import game.model.field.Cell;
import game.model.field.cell_objects.Robot;

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
    private Cell fromCell;

    /**
     * Ячейка куда переместился робот {@link RobotActionEvent#robot}.
     */
    private Cell toCell;

    /**
     * Источник питания.
     */
    private Battery battery;

    /**
     * Установить ячейку {@link RobotActionEvent#fromCell} откуда переместился робот {@link RobotActionEvent#robot}.
     * @param fromCell ячейка откуда переместился робот.
     */
    public void setFromCell(Cell fromCell) {
        this.fromCell = fromCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#fromCell} откуда переместился робот {@link RobotActionEvent#robot}.
     * @return ячейка откуда переместился робот.
     */
    public Cell getFromCell() {
        return fromCell;
    }

    /**
     * Установить ячейку {@link RobotActionEvent#toCell} куда переместился робот {@link RobotActionEvent#robot}.
     * @param toCell ячейка куда переместился робот.
     */
    public void setToCell(Cell toCell) {
        this.toCell = toCell;
    }

    /**
     * Получить ячейку {@link RobotActionEvent#toCell} куда переместился робот {@link RobotActionEvent#robot}.
     * @return ячейка куда переместился робот.
     */
    public Cell getToCell() {
        return toCell;
    }

    /**
     * Установить источник питания {@link RobotActionEvent#battery}.
     * @param battery источник питания.
     */
    public void setBattery(@NotNull Battery battery) {
        this.battery = battery;
    }

    /**
     * Получить источник питания {@link RobotActionEvent#battery}.
     * @return источник питания.
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * Установить робота {@link RobotActionEvent#robot}.
     * @param robot робот.
     */
    public void setRobot(@NotNull Robot robot) {
        this.robot = robot;
    }

    /**
     * Получить робота {@link RobotActionEvent#robot}.
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
