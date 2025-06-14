package game.ui.cell;

import game.ui.cell.CellWidget.Layer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Виджет объекта для виджета ячейки.
 */
public abstract class FieldItemWidget extends JPanel {

    /**
     * Конструктор.
     */
    public FieldItemWidget() {
        setState(State.DEFAULT);
        setOpaque(false);
    }

    /**
     * Состояние виджета.
     */
    public enum State {
        /**
         * Обычный.
         */
        DEFAULT,

        /**
         * Маленький.
         */
        SMALL
    }

    /**
     * Состояние виджета.
     */
    protected State cellItemState = State.DEFAULT;

    /**
     * Установить состояние виджета {@link FieldItemWidget#cellItemState}
     *
     * @param state состояние виджета.
     */
    void setState(State state) {
        cellItemState = state;
        setPreferredSize(getDimension());
        repaint();
        revalidate();
    }

    /**
     * Получить состояние виджета {@link FieldItemWidget#cellItemState}.
     *
     * @return состояние виджета.
     */
    public State getState() {
        return cellItemState;
    }

    /**
     * Получить изображение виджета.
     *
     * @return изображение виджета.
     */
    protected abstract BufferedImage getImage();

    /**
     * Получить слой на котором располагается виджет.
     *
     * @return слой на котором располагается виджет.
     */
    public abstract Layer getLayer();

    /**
     * Получить размеры виджета.
     *
     * @return размеры виджета.
     */
    protected abstract Dimension getDimension();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getImage(), 0, 0, null);
    }
}
