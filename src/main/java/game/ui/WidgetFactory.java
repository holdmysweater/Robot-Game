package game.ui;

import game.model.field.*;
import game.ui.obstacle.WallWidget;
import org.jetbrains.annotations.NotNull;
import game.model.*;
import game.model.field.cell_objects.Robot;
import game.model.field.cell_objects.Battery;
import game.ui.obstacle.ObstacleWidget;
import game.ui.cell.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellObject, ObstacleWidget> betweenCellObjects = new HashMap<>();

    public CellWidget create(@NotNull Cell cell) {
        if(cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = (cell instanceof ExitCell) ? new ExitWidget() : new CellWidget();

        Robot robot = cell.getBigObject();
        if(robot != null) {
            CellItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        if (cell instanceof NormalCell) {
            Battery battery = ((NormalCell) cell).getSmallObject();

            if (battery != null) {
                CellItemWidget batteryWidget = create(battery);
                item.addItem(batteryWidget);
            }
        }


        cells.put(cell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull Cell cell) {
        return cells.get(cell);
    }

    public void remove(@NotNull Cell cell) { cells.remove(cell); }

    public CellItemWidget create(@NotNull CellObject cellObject) {
        if(cellObjects.containsKey(cellObject)) return cellObjects.get(cellObject);

        CellItemWidget createdWidget = null;
        if(cellObject instanceof Robot) {
            createdWidget = new RobotWidget((Robot) cellObject, Color.BLUE);
        } else if(cellObject instanceof Battery) {
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

    public void remove(@NotNull BetweenCellObject betweenCellObject) { betweenCellObjects.remove(betweenCellObject); }
}
