package game.ui.cell;

import game.model.field.core.Cell;
import game.ui.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Виджет ячейки.
 *
 * @see Cell
 */
public class CellWidget extends JPanel {

    /**
     * Слой.
     */
    public enum Layer {
        /**
         * Верхний.
         */
        TOP,

        /**
         * Нижний.
         */
        BOTTOM
    }

    private final Map<Layer, CellItemWidget> items = new HashMap<>();

    /**
     * Размер виджета ячейки.
     */
    private static final int CELL_SIZE = 120;

    /**
     * Конструктор.
     */
    public CellWidget() {
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setBackground(ImageUtils.BACKGROUND_COLOR);
    }

    /**
     * Добавить элемент в виджет ячейки.
     *
     * @param item виджет объекта для ячейки.
     * @throws IllegalArgumentException если объектов добавляется больше 2.
     */
    public void addItem(CellItemWidget item) {
        if (items.size() > 2) throw new IllegalArgumentException();
        int index = -1;

        if (items.containsKey(Layer.BOTTOM)) {
            item.setState(CellItemWidget.State.SMALL);
        } else {
            item.setState(CellItemWidget.State.DEFAULT);
        }

        if (items.containsKey(Layer.TOP)) {
            item.setState(CellItemWidget.State.DEFAULT);
            items.get(Layer.TOP).setState(CellItemWidget.State.SMALL);
            index = 0;
        }

        items.put(item.getLayer(), item);
        add(item, index);
    }

    /**
     * Удалить виджет из ячейки.
     *
     * @param item удаляемый виджет.
     */
    public void removeItem(CellItemWidget item) {
        if (items.containsValue(item)) {
            int index = 0;

            if (item.getLayer() == Layer.BOTTOM) {
                if (items.containsKey(Layer.TOP)) {
                    items.get(Layer.TOP).setState(CellItemWidget.State.DEFAULT);
                }
            }

            if (item.getLayer() == Layer.TOP) {
                if (items.containsKey(Layer.BOTTOM)) {
                    index = 1;
                    items.get(Layer.BOTTOM).setState(CellItemWidget.State.DEFAULT);
                }
            }

            remove(index);
            items.remove(item.getLayer());
            repaint();
        }
    }
}
