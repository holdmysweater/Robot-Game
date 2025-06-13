package game.ui;

import game.model.events.*;
import game.model.field.core.*;
import game.model.field.core.Point;
import game.model.field.core.Robot;
import org.jetbrains.annotations.NotNull;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.cell.*;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

/**
 * Виджет поля, поддерживающий покадровое движение робота через JLayeredPane.
 */
public class FieldWidget extends JLayeredPane {

    private final Field field;
    private final WidgetFactory widgetFactory;
    private final JPanel gridPanel;

    // Размеры клетки
    private static final int CELL_WIDTH = Cell.DEFAULT_WIDTH;
    private static final int CELL_HEIGHT = Cell.DEFAULT_HEIGHT;

    public FieldWidget(@NotNull Field field, @NotNull WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;

        // --- Сетка ---
        gridPanel = new JPanel();
        gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setOpaque(false);
        fillField();

        int w = field.getWidth() * CELL_WIDTH;
        int h = field.getHeight() * CELL_HEIGHT;
        setPreferredSize(new Dimension(w, h));
        setLayout(null);

        gridPanel.setBounds(0, 0, w, h);
        add(gridPanel, JLayeredPane.DEFAULT_LAYER);

        subscribeOnRobots();
        field.addFieldActionListener(new FieldController());
    }

    // --- СОЗДАНИЕ ПОЛЯ ---
    private void fillField() {
        if (field.getHeight() > 0) {
            JPanel startRowWalls = createRowWalls(0, Direction.NORTH);
            gridPanel.add(startRowWalls);
        }

        for (int i = 0; i < field.getHeight(); ++i) {
            JPanel row = createRow(i);
            gridPanel.add(row);
            JPanel rowWalls = createRowWalls(i, Direction.SOUTH);
            gridPanel.add(rowWalls);
        }
    }

    private JPanel createRow(int rowIndex) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        for (int i = 0; i < field.getWidth(); ++i) {
            Point point = new Point(i, rowIndex);
            Cell cell = field.getCell(point);
            CellWidget cellWidget = widgetFactory.create(cell);

            if (i == 0) {
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
        if (direction == Direction.EAST || direction == Direction.WEST) throw new IllegalArgumentException();
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);

        for (int i = 0; i < field.getWidth(); ++i) {
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
        robot.addMobileObjectActionListener(new MobileObjectObserver());
    }

    /**
     * Перемещает виджет робота в пиксельную позицию на JLayeredPane (во время движения).
     */
    private void updateRobotWidgetPosition(Robot robot, int pixelX, int pixelY) {
        RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);

        // Удаляем из любого родителя
        Container parent = robotWidget.getParent();
        if (parent != null) parent.remove(robotWidget);

        // Добавляем на слой движения, если еще не там
        if (robotWidget.getParent() != this) {
            this.add(robotWidget, JLayeredPane.DRAG_LAYER);
        }

        // Центрируем по координате центра из модели
        robotWidget.setBounds(pixelX, pixelY, robotWidget.getWidth(), robotWidget.getHeight());
        robotWidget.repaint();
        this.repaint();
    }

    /**
     * При прибытии в ячейку перемещает виджет робота обратно в CellWidget.
     */
    private void snapRobotWidgetToCell(Robot robot, Cell cell) {
        RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);

        // Удаляем с JLayeredPane (если был)
        if (robotWidget.getParent() == this) {
            this.remove(robotWidget);
        }

        // Добавляем обратно в CellWidget
        CellWidget cellWidget = widgetFactory.getWidget(cell);
        cellWidget.addItem(robotWidget);
        cellWidget.revalidate();
        cellWidget.repaint();

        this.repaint();
    }

    /**
     * Перемещение робота покадрово (вызывается когда робот "движется").
     */
    private class MobileObjectObserver implements MobileObjectListener {

        @Override
        public void objectIsMoved(EventObject event) {
            Robot robot = (Robot) event.getSource();
            ApproximatingRectangle approx = robot.getApproximatingRectangle();
            Point center = approx.getCenter();

            RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);
            int widgetW = robotWidget.getWidth();
            int widgetH = robotWidget.getHeight();

            // Центрируем по пиксельной координате из модели (минус половина размера виджета)
            int pixelX = center.getX() - widgetW / 2;
            int pixelY = center.getY() - widgetH / 2;

            updateRobotWidgetPosition(robot, pixelX, pixelY);
        }
    }

    /**
     * Когда робот прибыл в ячейку (вызывается только после завершения движения).
     */
    private class RobotController implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            // Снимаем с JLayeredPane и добавляем в CellWidget
            snapRobotWidgetToCell(event.getRobot(), event.getToCell());
            CellItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
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
