package game.ui;

import game.model.events.*;
import game.model.field.core.*;
import game.model.field.core.Point;
import game.model.field.core.Robot;
import game.ui.cell.CellWidget;
import game.ui.cell.FieldItemWidget;
import game.ui.cell.RobotWidget;
import game.ui.obstacle.BetweenCellsWidget;
import game.ui.utils.ImageUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

/**
 * Виджет поля, поддерживающий покадровое движение робота через JLayeredPane.
 */
public class FieldWidget extends JLayeredPane {

    private final Field field;
    private final WidgetFactory widgetFactory;

    public FieldWidget(@NotNull Field field, @NotNull WidgetFactory widgetFactory) {
        this.field = field;
        this.widgetFactory = widgetFactory;

        setLayout(null);
        setBackground(ImageUtils.BETWEEN_CELLS_COLOR);
        setOpaque(true);

        int cellW = Cell.DEFAULT_WIDTH;
        int cellH = Cell.DEFAULT_HEIGHT;
        int spacing = Field.DISTANCE_BETWEEN_CELLS;
        int w = field.getWidth() * cellW + (field.getWidth() - 1) * spacing;
        int h = field.getHeight() * cellH + (field.getHeight() - 1) * spacing;

        setPreferredSize(new Dimension(w, h));

        // Add all cells
        for (int y = 0; y < field.getHeight(); ++y) {
            for (int x = 0; x < field.getWidth(); ++x) {
                int px = x * (cellW + spacing);
                int py = y * (cellH + spacing);
                Cell cell = field.getCell(new game.model.field.core.Point(x, y));
                CellWidget cw = widgetFactory.create(cell);
                cw.setBounds(px, py, cellW, cellH);
                add(cw, JLayeredPane.DEFAULT_LAYER);

                // Add vertical walls (between cells)
                if (x < field.getWidth() - 1) {
                    BetweenCellsWidget wallE = widgetFactory.create(cell.getNeighborArea(Direction.EAST));
                    int wallW = spacing;
                    wallE.setBounds(px + cellW, py, wallW, cellH);
                    add(wallE, JLayeredPane.DEFAULT_LAYER);
                }
                // Add horizontal walls (between cells)
                if (y < field.getHeight() - 1) {
                    BetweenCellsWidget wallS = widgetFactory.create(cell.getNeighborArea(Direction.SOUTH));
                    int wallH = spacing;
                    wallS.setBounds(px, py + cellH, cellW, wallH);
                    add(wallS, JLayeredPane.DEFAULT_LAYER);
                }
            }
        }

        subscribeOnRobots();
        field.addFieldActionListener(new FieldController());
    }

    // --- ROBOT MOVEMENT ---
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
        if (robotWidget.getParent() != this) {
            this.add(robotWidget, JLayeredPane.DRAG_LAYER);
        }
        robotWidget.setBounds(pixelX, pixelY, robotWidget.getWidth(), robotWidget.getHeight());
        robotWidget.repaint();
        this.repaint();
    }

    /**
     * При прибытии в ячейку перемещает виджет робота обратно в CellWidget.
     */
    private void snapRobotWidgetToCell(Robot robot, Cell cell) {
        RobotWidget robotWidget = (RobotWidget) widgetFactory.getWidget(robot);
        if (robotWidget.getParent() == this) {
            this.remove(robotWidget);
        }
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
            int pixelY = center.getY() - widgetH / 2 + 5;

            updateRobotWidgetPosition(robot, pixelX, pixelY);
        }
    }

    private class RobotController implements RobotActionListener {

        @Override
        public void robotStartedMoving(@NotNull RobotActionEvent event) {
            FieldItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            from.removeItem(robotWidget);
            robotWidget.requestFocus();
        }

        @Override
        public void robotFinishedMoving(@NotNull RobotActionEvent event) {
            snapRobotWidgetToCell(event.getRobot(), event.getToCell());
            FieldItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
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
            FieldItemWidget batteryWidget = widgetFactory.getWidget(event.getBattery());
            cellWidget.removeItem(batteryWidget);
            widgetFactory.remove(event.getBattery());
        }
    }

    //endregion

    //region СЛУШАТЕЛЬ ПОЛЯ

    private class FieldController implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            Robot robot = (Robot) event.getFieldObject();
            Cell teleport = robot.getIdleCellPosition();
            CellWidget teleportWidget = widgetFactory.getWidget(teleport);
            FieldItemWidget robotWidget = widgetFactory.getWidget(robot);
            teleportWidget.removeItem(robotWidget);
        }

        @Override
        public void objectWasCreated(@NotNull FieldActionEvent event) {
            FieldObject fieldObject = event.getFieldObject();
            if (fieldObject instanceof Hole) {
                holeWasCreated((Hole) fieldObject);
            }
        }

        private void holeWasCreated(@NotNull Hole hole) {
            FieldItemWidget holeWidget = widgetFactory.create(hole);
            Cell cell = hole.getPosition();
            CellWidget cellWidget = widgetFactory.getWidget(cell);
            cellWidget.addItem(holeWidget);
            cellWidget.revalidate();
        }
    }

    //endregion
}
