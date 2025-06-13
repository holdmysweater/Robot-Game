package game.ui.obstacle;

import game.model.field.core.BetweenCellsArea;
import game.ui.utils.ImageUtils;
import org.jetbrains.annotations.NotNull;
import game.model.field.core.Orientation;

import javax.swing.*;
import java.awt.*;

/**
 * Виджет контейнера для виджетов между ячейками {@link BetweenCellsWidget}.
 */
public class BetweenCellsWidget extends JPanel {
    /**
     * Ориентация.
     */
    private final Orientation orientation;

    /**
     * Конструктор.
     *
     * @param betweenCellsArea область между клетками.
     */
    public BetweenCellsWidget(@NotNull BetweenCellsArea betweenCellsArea) {
        super(new BorderLayout());
        this.orientation = betweenCellsArea.getOrientation();
        if (betweenCellsArea.getObstacle() != null) { setItem(new WallWidget(orientation)); }
        setPreferredSize(getDimensionByOrientation());
        setBackground(ImageUtils.BETWEEN_CELLS_COLOR);
    }

    /**
     * Установить элемент.
     *
     * @param obstacleWidget элемент.
     * @throws IllegalArgumentException если ориентация объекта не совпадает с ориентацией контейнера.
     */
    private void setItem(@NotNull ObstacleWidget obstacleWidget) {
        if (obstacleWidget.getOrientation() != orientation) throw new IllegalArgumentException();
        add(obstacleWidget);
    }

    /**
     * Получить размеры виджета по ориентации {@link BetweenCellsWidget#orientation}
     *
     * @return размеры.
     */
    private Dimension getDimensionByOrientation() {
        return (orientation == Orientation.VERTICAL) ? new Dimension(5, 120) : new Dimension(125, 5);
    }
}
