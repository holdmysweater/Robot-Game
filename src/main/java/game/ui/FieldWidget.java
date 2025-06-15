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

        paintField();
        subscribeOnRobots();
        field.addFieldActionListener(new FieldController());
    }

    private void paintField() {
        ApproximatingRectangle fieldApproximatingRectangle = field.getApproximatingRectangle();
        setPreferredSize(new Dimension(fieldApproximatingRectangle.getWidth(), fieldApproximatingRectangle.getHeight()));

        // Add all cells
        for (int y = 0; y < field.getHeight(); ++y) {
            for (int x = 0; x < field.getWidth(); ++x) {
                Cell cell = field.getCell(new game.model.field.core.Point(x, y));
                ApproximatingRectangle cellApproximatingRectangle = cell.getApproximatingRectangle();
                Point northWestPoint = cellApproximatingRectangle.getPointNorthWest();
                Point centralPoint = cellApproximatingRectangle.getCenter();
                int px = centralPoint.getX() - (cellApproximatingRectangle.getWidth() / 2);
                int py = centralPoint.getY() - (cellApproximatingRectangle.getHeight() / 2);

                paintCell(cell);

                // Add vertical walls (between cells)
                if (x > 0) {
                    BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.WEST);
                    paintBetweenCellsArea(betweenCellsArea);
                }
                // Add horizontal walls (between cells)
                if (y > 0) {
                    BetweenCellsArea betweenCellsArea = cell.getNeighborArea(Direction.NORTH);
                    paintBetweenCellsArea(betweenCellsArea);
                }
            }
        }
    }

    private void paintCell(Cell cell) {
        ApproximatingRectangle cellApproximatingRectangle = cell.getApproximatingRectangle();
        Point northWestPoint = cellApproximatingRectangle.getPointNorthWest();

        CellWidget cellWidget = widgetFactory.create(cell);
        cellWidget.setBounds(northWestPoint.getX(), northWestPoint.getY(), cellApproximatingRectangle.getWidth(), cellApproximatingRectangle.getHeight());
        add(cellWidget, JLayeredPane.DEFAULT_LAYER);
    }

    private void paintBetweenCellsArea(BetweenCellsArea betweenCellsArea) {
        ApproximatingRectangle betweenCellsAreaApproximatingRectangle = betweenCellsArea.getApproximatingRectangle();
        Point northWestPoint = betweenCellsAreaApproximatingRectangle.getPointNorthWest();

        BetweenCellsWidget betweenCellsWidget = widgetFactory.create(betweenCellsArea);
        betweenCellsWidget.setBounds(northWestPoint.getX(), northWestPoint.getY(), betweenCellsAreaApproximatingRectangle.getWidth(), betweenCellsAreaApproximatingRectangle.getHeight());
        add(betweenCellsWidget, JLayeredPane.DEFAULT_LAYER);
    }

    private void paintVisibleFieldObject(VisibleFieldObject fieldObject, int layer) {
        ApproximatingRectangle approximatingRectangle = fieldObject.getApproximatingRectangle();
        Point northWestPoint = approximatingRectangle.getPointNorthWest();

        FieldItemWidget widget = widgetFactory.create(fieldObject);
        widget.setBounds(northWestPoint.getX(), northWestPoint.getY(), approximatingRectangle.getWidth(), approximatingRectangle.getHeight());
        add(widget, layer);
    }

    // --- ROBOT MOVEMENT ---
    private void subscribeOnRobots() {
        Robot robot = field.getRobot();
        robot.addRobotActionListener(new RobotController());
        robot.addMobileObjectActionListener(new MobileObjectObserver());
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
            if (!(event.getSource() instanceof VisibleFieldObject)) {
                return;
            }
            VisibleFieldObject visibleFieldObject = (VisibleFieldObject) event.getSource();
            paintVisibleFieldObject(visibleFieldObject, JLayeredPane.DRAG_LAYER);
            ApproximatingRectangle approx = visibleFieldObject.getApproximatingRectangle();
        }
    }

    private class RobotController implements RobotActionListener {

        @Override
        public void robotStartedMoving(@NotNull RobotActionEvent event) {
            FieldItemWidget robotWidget = widgetFactory.getWidget(event.getRobot());
            CellWidget from = widgetFactory.getWidget(event.getFromCell());
            from.removeItem(robotWidget);
            FieldWidget.this.add(robotWidget, JLayeredPane.DRAG_LAYER);
            paintVisibleFieldObject(event.getRobot(), JLayeredPane.DRAG_LAYER);
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

        @Override
        public void robotWasDestroyed(@NotNull RobotActionEvent event) {

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
            } else if (fieldObject instanceof Projectile) {
                projectileWasCreated((Projectile) fieldObject);
            }
        }

        private void holeWasCreated(@NotNull Hole hole) {
            FieldItemWidget holeWidget = widgetFactory.create(hole);
            Cell cell = hole.getPosition();
            CellWidget cellWidget = widgetFactory.getWidget(cell);
            cellWidget.addItem(holeWidget);
            cellWidget.revalidate();
        }

        private void projectileWasCreated(@NotNull Projectile projectile) {
            FieldItemWidget projectileWidget = widgetFactory.create(projectile);
            projectile.addMobileObjectActionListener(new MobileObjectObserver());
            FieldWidget.this.add(projectileWidget, JLayeredPane.DRAG_LAYER);
            paintVisibleFieldObject(projectile, JLayeredPane.DRAG_LAYER);
        }

        @Override
        public void objectWasDestroyed(@NotNull FieldActionEvent event) {
            FieldObject fieldObject = event.getFieldObject();
            if (fieldObject instanceof VisibleFieldObject) {
                widgetFactory.remove((VisibleFieldObject) fieldObject);
            }
        }
    }

    //endregion
}
