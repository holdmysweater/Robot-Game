package game.ui;

import game.model.field.core.*;
import game.model.field.core.Robot;
import game.ui.obstacle.WallWidget;
import org.jetbrains.annotations.NotNull;
import game.ui.obstacle.ObstacleWidget;
import game.ui.cell.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<AbstractCell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellObject, ObstacleWidget> betweenCellObjects = new HashMap<>();

    public CellWidget create(@NotNull AbstractCell abstractCell) {
        if (cells.containsKey(abstractCell)) return cells.get(abstractCell);

        CellWidget item = (abstractCell instanceof ExitCell) ? new ExitWidget() : new CellWidget();

        Robot robot = abstractCell.getBigObject();
        if (robot != null) {
            CellItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        if (abstractCell instanceof NormalCell) {
            Battery battery = ((NormalCell) abstractCell).getSmallObject();

            if (battery != null) {
                CellItemWidget batteryWidget = create(battery);
                item.addItem(batteryWidget);
            }
        }


        cells.put(abstractCell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull AbstractCell abstractCell) {
        return cells.get(abstractCell);
    }

    public void remove(@NotNull AbstractCell abstractCell) {
        cells.remove(abstractCell);
    }

    public CellItemWidget create(@NotNull CellObject cellObject) {
        if (cellObjects.containsKey(cellObject)) return cellObjects.get(cellObject);

        CellItemWidget createdWidget = null;
        if (cellObject instanceof Robot) {
            createdWidget = new RobotWidget((Robot) cellObject, Color.BLUE);
        } else if (cellObject instanceof Battery) {
            createdWidget = new BatteryWidget((Battery) cellObject);
        } else {
            throw new IllegalArgumentException();
        }

        cellObjects.put(cellObject, createdWidget);
        return createdWidget;
    }

    public CellItemWidget getWidget(@NotNull CellObject cellObject) {
        return cellObjects.get(cellObject);
    }

    public void remove(@NotNull CellObject cellObject) {
        cellObjects.remove(cellObject);
    }

    public ObstacleWidget create(@NotNull BetweenCellObject betweenCellObject, Orientation orientation) {
        if (betweenCellObjects.containsKey(betweenCellObject)) return betweenCellObjects.get(betweenCellObject);

        ObstacleWidget createdBlockWidget = new WallWidget(orientation);

        betweenCellObjects.put(betweenCellObject, createdBlockWidget);
        return createdBlockWidget;
    }

    public ObstacleWidget getWidget(@NotNull BetweenCellObject betweenCellObject) {
        return betweenCellObjects.get(betweenCellObject);
    }

    public void remove(@NotNull BetweenCellObject betweenCellObject) {
        betweenCellObjects.remove(betweenCellObject);
    }
}
