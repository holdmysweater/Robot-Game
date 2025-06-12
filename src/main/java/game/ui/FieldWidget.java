package game.ui;

import game.model.events.*;
import game.model.field.core.*;
import org.jetbrains.annotations.NotNull;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.cell.*;

import javax.swing.*;

public class FieldWidget extends JPanel {

    // region КОНСТРУКТОРЫ

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

    //endregion

    // region СОЗДАНИЕ ПОЛЯ

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

    //endregion

    //region СЛУШАТЕЛЬ РОБОТА

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
        public void robotUnfrozenChanged(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);
            robotWidget.setActive(robot.isUnfrozen());
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            Robot robot = event.getRobot();
            CellWidget cellWidget = widgetFactory.getWidget(robot.getIdleCellPosition());
            CellItemWidget batteryWidget = widgetFactory.getWidget(event.getBattery());
            cellWidget.removeItem(batteryWidget);
            widgetFactory.remove(event.getBattery());
        }
    }

    //endregion

    //region СЛУШАТЕЛЬ ПОЛЯ

    private class FieldController implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            Robot robot = (Robot) event.getCellObject();
            Cell teleport = robot.getIdleCellPosition();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            CellItemWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }

        @Override
        public void holeWasCreated(@NotNull FieldActionEvent event) {
            Hole hole = (Hole) event.getCellObject();
            CellItemWidget holeWidget = widgetFactory.create(hole);
            Cell cell = hole.getPosition();
            CellWidget cellWidget = widgetFactory.getWidget(cell);
            cellWidget.addItem(holeWidget);
            cellWidget.revalidate();
        }
    }

    //endregion
}
