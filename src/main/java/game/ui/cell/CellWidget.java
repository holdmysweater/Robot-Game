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

    private final Map<Layer, FieldItemWidget> items = new HashMap<>();

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
    public void addItem(FieldItemWidget item) {
        if (items.size() > 2) throw new IllegalArgumentException();
        int index = -1;

        if (items.containsKey(Layer.BOTTOM)) {
            item.setState(FieldItemWidget.State.SMALL);
        } else {
            item.setState(FieldItemWidget.State.DEFAULT);
        }

        if (items.containsKey(Layer.TOP)) {
            item.setState(FieldItemWidget.State.DEFAULT);
            items.get(Layer.TOP).setState(FieldItemWidget.State.SMALL);
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
    public void removeItem(FieldItemWidget item) {
        if (items.containsValue(item)) {
            int index = 0;

            if (item.getLayer() == Layer.BOTTOM) {
                if (items.containsKey(Layer.TOP)) {
                    items.get(Layer.TOP).setState(FieldItemWidget.State.DEFAULT);
                }
            }

            if (item.getLayer() == Layer.TOP) {
                if (items.containsKey(Layer.BOTTOM)) {
                    index = 1;
                    items.get(Layer.BOTTOM).setState(FieldItemWidget.State.DEFAULT);
                }
            }

            remove(index);
            items.remove(item.getLayer());
            repaint();
        }
    }
}
