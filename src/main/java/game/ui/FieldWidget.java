package game.ui;

import org.jetbrains.annotations.NotNull;
import game.model.*;
import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.model.field.BetweenCellObject;
import game.model.field.Cell;
import game.model.field.Field;
import game.model.field.cell_objects.Robot;
import game.ui.block.BetweenCellsWidget;
import game.ui.block.BlockWidget;
import game.ui.cell.*;

import javax.swing.*;

public class FieldWidget extends JPanel {

    private final Field field;
    private final WidgetFactory widgetFactory;

    public FieldWidget(@NotNull Field field, @NotNull  WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        fillField();
        subscribeOnRobots();
        field.addFieldActionListener(new FieldController());
    }

    private void fillField() {
        if(field.getHeight() > 0) {
            JPanel startRowWalls = createRowWalls(0, Direction.NORTH);
            add(startRowWalls);
        }

        for (int i = 0; i < field.getHeight(); ++i) {
            JPanel row = createRow(i);
            add(row);
            JPanel rowWalls = createRowWalls(i, Direction.SOUTH);
            add(rowWalls);
        }
    }

    private JPanel createRow(int rowIndex) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);
            CellWidget cellWidget = widgetFactory.create(cell);

            if(i == 0)  {
                BetweenCellsWidget westCellWidget = new BetweenCellsWidget(Orientation.VERTICAL);
                BetweenCellObject wallSegment = cell.getNeighborObstacle(Direction.WEST);
                if( wallSegment != null) {
                    BlockWidget wallWidget = widgetFactory.create(wallSegment, Orientation.VERTICAL);
                    westCellWidget.setItem(wallWidget);
                }
                row.add(westCellWidget);
            }

            row.add(cellWidget);

            BetweenCellsWidget eastCellWidget = new BetweenCellsWidget(Orientation.VERTICAL);
            BetweenCellObject eastWallSegment = cell.getNeighborObstacle(Direction.EAST);
            if(eastWallSegment != null) {
                BlockWidget wallWidget = widgetFactory.create(eastWallSegment, Orientation.VERTICAL);
                eastCellWidget.setItem(wallWidget);
            }

            row.add(eastCellWidget);
        }
        return row;
    }

    private JPanel createRowWalls(int rowIndex, Direction direction) {
        if(direction == Direction.EAST || direction == Direction.WEST) throw new IllegalArgumentException();
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        for(int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);

            BetweenCellsWidget southCellWidget = new BetweenCellsWidget(Orientation.HORIZONTAL);
            BetweenCellObject southWallSegment =  cell.getNeighborObstacle(direction);

            if(southWallSegment != null) {
                BlockWidget wallWidget = widgetFactory.create(southWallSegment, Orientation.HORIZONTAL);
                southCellWidget.setItem(wallWidget);
            }

            row.add(southCellWidget);
        }
        return row;
    }

    private void subscribeOnRobots() {
        Robot robot = field.getRobotOnField();
        robot.addRobotActionListener(new RobotController());
    }

    private class RobotController implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            CellItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            CellWidget to = widgetFactory.getWidget(event.getToCell());
            from.removeItem(robotWidget);
            to.addItem(robotWidget);
            robotWidget.requestFocus();
        }

        @Override
        public void robotUnfrozenChanged(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);
            robotWidget.setActive(robot.isUnfrozen());
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            CellWidget cellWidget = widgetFactory.getWidget(robot.getPosition());
            CellItemWidget batteryWidget = widgetFactory.getWidget(event.getBattery());
            cellWidget.removeItem(batteryWidget);
            widgetFactory.remove(event.getBattery());
        }
    }

    private class FieldController implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            Robot robot = event.getRobot();
            Cell teleport = event.getTeleport();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            CellItemWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }
    }
}
