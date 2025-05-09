package game.ui;

import game.model.field.core.*;
import org.jetbrains.annotations.NotNull;
import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;
import game.model.events.RobotActionEvent;
import game.model.events.RobotActionListener;
import game.ui.obstacle.BetweenCellsWidget;
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
                BetweenCellsWidget westCellWidget = widgetFactory.create(cell.getNeighborArea(Direction.WEST));
                row.add(westCellWidget);
            }

            row.add(cellWidget);

            BetweenCellsWidget eastCellWidget = widgetFactory.create(cell.getNeighborArea(Direction.EAST));
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

            BetweenCellsWidget betweenCellWidget = widgetFactory.create(cell.getNeighborArea(direction));
            row.add(betweenCellWidget);
        }
        return row;
    }

    private void subscribeOnRobots() {
        Robot robot = field.getRobot();
        robot.addRobotActionListener(new RobotController());
    }

    private class RobotController implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            CellItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            CellWidget to = widgetFactory.getWidget(event.getToCell());
            from.removeItem(robotWidget);
            if (!event.getRobot().isTeleported()) {
                to.addItem(robotWidget);
            }
            robotWidget.requestFocus();
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
            Cell teleport = robot.getPosition();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            CellItemWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }
    }
}
