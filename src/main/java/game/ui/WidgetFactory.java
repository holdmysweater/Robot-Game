package game.ui;

import game.model.field.*;
import org.jetbrains.annotations.NotNull;
import game.model.*;
import game.model.field.cell_objects.Robot;
import game.model.field.between_cells_objects.WallSegment;
import game.model.field.cell_objects.Battery;
import game.ui.block.BlockWidget;
import game.ui.block.WallWidget;
import game.ui.cell.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellObject, BlockWidget> betweenCellObjects = new HashMap<>();

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

    public BlockWidget create(@NotNull BetweenCellObject betweenCellObject, Orientation orientation) {
        if (betweenCellObjects.containsKey(betweenCellObject)) return betweenCellObjects.get(betweenCellObject);

        BlockWidget createdBlockWidget = null;

        if (betweenCellObject instanceof WallSegment) {
            createdBlockWidget = new WallWidget(orientation);
        } else {
            throw new IllegalArgumentException();
        }

        betweenCellObjects.put(betweenCellObject, createdBlockWidget);
        return createdBlockWidget;
    }

    public BlockWidget getWidget(@NotNull BetweenCellObject betweenCellObject) {
        return betweenCellObjects.get(betweenCellObject);
    }

    public void remove(@NotNull BetweenCellObject betweenCellObject) { betweenCellObjects.remove(betweenCellObject); }
}
