package game.ui;

import game.model.field.core.*;
import game.model.field.core.Robot;
import game.ui.cell.*;
import game.ui.obstacle.BetweenCellsWidget;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WidgetFactory {

    private final Map<Cell, CellWidget> cells = new HashMap<>();
    private final Map<VisibleFieldObject, FieldItemWidget> fieldObjects = new HashMap<>();
    private final Map<BetweenCellsArea, BetweenCellsWidget> betweenCellsAreas = new HashMap<>();

    /*---------- Cell ----------*/
    public CellWidget create(@NotNull Cell cell) {
        if (cells.containsKey(cell)) return cells.get(cell);

        CellWidget item = new CellWidget();

        Robot robot = (Robot) cell.getObject(Robot.class);
        if (robot != null) {
            FieldItemWidget robotWidget = create(robot);
            item.addItem(robotWidget);
        }

        Battery battery = (Battery) cell.getObject(Battery.class);
        if (battery != null) {
            FieldItemWidget batteryWidget = create(battery);
            item.addItem(batteryWidget);
        }

        ExitPoint exitPoint = (ExitPoint) cell.getObject(ExitPoint.class);
        if (exitPoint != null) {
            FieldItemWidget exitWidget = create(exitPoint);
            item.addItem(exitWidget);
        }

        Hole hole = (Hole) cell.getObject(Hole.class);
        if (hole != null) {
            FieldItemWidget holeWidget = create(hole);
            item.addItem(holeWidget);
        }

        Turret turret = (Turret) cell.getObject(Turret.class);
        if (turret != null) {
            FieldItemWidget turretWidget = create(turret);
            item.addItem(turretWidget);
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
    public FieldItemWidget create(@NotNull VisibleFieldObject cellObject) {
        if (fieldObjects.containsKey(cellObject)) return fieldObjects.get(cellObject);

        FieldItemWidget createdWidget = switch (cellObject) {
            case Robot robot -> new RobotWidget(robot, Color.BLUE);
            case Battery battery -> new BatteryWidget(battery);
            case ExitPoint exitPoint -> new ExitWidget();
            case Hole hole -> new HoleWidget();
            case Turret turret -> new TurretWidget(turret.getDirection());
            default -> throw new IllegalArgumentException();
        };

        fieldObjects.put(cellObject, createdWidget);
        return createdWidget;
    }

    public FieldItemWidget getWidget(@NotNull VisibleFieldObject fieldObject) {
        return fieldObjects.get(fieldObject);
    }

    public void remove(@NotNull VisibleFieldObject fieldObject) {
        fieldObjects.remove(fieldObject);
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
