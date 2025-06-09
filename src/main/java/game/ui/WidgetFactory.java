package game.ui;

import game.model.field.cell_objects.SelfActivatingCellObject;
import game.model.field.cell_objects.NonInteractiveCellObject;
import game.model.field.cell_objects.NonStationaryCellObject;
import game.model.field.cell_objects.SmallCellObject;
import game.model.field.core.ExitPoint;
import game.model.field.core.*;
import game.model.field.core.Robot;
import game.ui.obstacle.BetweenCellsWidget;
import org.jetbrains.annotations.NotNull;
import game.ui.cell.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<CellObject, CellItemWidget> cellObjects = new HashMap<>();
    private final Map<BetweenCellsArea, BetweenCellsWidget> betweenCellsAreas = new HashMap<>();

    /*---------- Cell ----------*/
    public CellWidget create(@NotNull Cell cell) {
        if (cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = new CellWidget();

        Robot robot = (Robot) cell.getObject(NonStationaryCellObject.class);
        if (robot != null) {
            CellItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        Battery battery = (Battery) cell.getObject(SmallCellObject.class);
        if (battery != null) {
            CellItemWidget batteryWidget = create(battery);
            item.addItem(batteryWidget);
        }

        ExitPoint exitPoint = (ExitPoint) cell.getObject(SelfActivatingCellObject.class);
        if (exitPoint != null) {
            CellItemWidget exitWidget = create(exitPoint);
            item.addItem(exitWidget);
        }

        Hole hole = (Hole) cell.getObject(NonInteractiveCellObject.class);
        if (hole != null) {
            CellItemWidget holeWidget = create(hole);
            item.addItem(holeWidget);
        }

        cells.put(cell, item);
        return item;
    }

    public CellWidget getWidget(@NotNull Cell cell) {
        return cells.get(cell);
    }

    public void remove(@NotNull Cell cell) {
        cells.remove(cell);
    }

    /*---------- CellObject ----------*/
    public CellItemWidget create(@NotNull CellObject cellObject) {
        if (cellObjects.containsKey(cellObject)) return cellObjects.get(cellObject);

        CellItemWidget createdWidget = null;
        if (cellObject instanceof Robot) {
            createdWidget = new RobotWidget((Robot) cellObject, Color.BLUE);
        } else if (cellObject instanceof Battery) {
            createdWidget = new BatteryWidget((Battery) cellObject);
        } else if (cellObject instanceof ExitPoint) {
            createdWidget = new ExitWidget();
        } else if (cellObject instanceof Hole) {
            createdWidget = new HoleWidget();
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

    /*---------- BetweenCellArea ----------*/
    public BetweenCellsWidget create(@NotNull BetweenCellsArea betweenCellsArea) {
        if (betweenCellsAreas.containsKey(betweenCellsArea)) return betweenCellsAreas.get(betweenCellsArea);

        BetweenCellsWidget createdWidget = new BetweenCellsWidget(betweenCellsArea);

        betweenCellsAreas.put(betweenCellsArea, createdWidget);
        return createdWidget;
    }

    public BetweenCellsWidget getWidget(@NotNull BetweenCellsArea betweenCellsArea) {
        return betweenCellsAreas.get(betweenCellsArea);
    }

    public void remove(@NotNull BetweenCellsArea betweenCellsArea) {
        betweenCellsAreas.remove(betweenCellsArea);
    }
}
