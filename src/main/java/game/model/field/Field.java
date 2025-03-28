package game.model.field;

import org.jetbrains.annotations.NotNull;
import game.model.Direction;
import game.model.Point;
import game.model.events.*;
import game.model.field.cell_objects.Robot;

import java.util.*;

/**
 * Поле.
 */
public class Field {

    /**
     * Ячейки поля.
     */
    private final Map<Point, Cell> cells = new HashMap<>();

    /**
     * Ширина поля.
     */
    private final int width;

    /**
     * Высота поля.
     */
    private final int height;

    /**
     * Ячейка выхода.
     */
    private final ExitCell exitCell;

    /**
     * Конструктор.
     * @param width ширина. Должна быть > 0.
     * @param height высота. Должна быть > 0.
     * @param exitPoint координата ячейки выхода.
     * @throws IllegalArgumentException если ширина, высота или координата ячейки переданы некорректные.
     */
    public Field(int width, int height, @NotNull Point exitPoint) {
        if(width <= 0) throw new IllegalArgumentException("Field width must be more than 0");
        if(height <= 0) throw new IllegalArgumentException("Field height must be more than 0");
        if(exitPoint.getX() >= width || exitPoint.getY() >= height)
            throw new IllegalArgumentException("exit point coordinates must be in range from 0 to weight or height");

        this.width = width;
        this.height = height;
        buildField(exitPoint);
        this.exitCell = (ExitCell) getCell(exitPoint);

        ((ExitCell) getCell(exitPoint)).addExitCellActionListener(new ExitCellObserver());
    }

    /**
     * Построить игровое поле.
     * @param exitPoint координата ячейки выхода.
     */
    private void buildField(Point exitPoint) { // !!! Непонятное название метода
                                // DONE: Переименовал метод setupField -> buildField
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                Point p = new Point(x, y);
                Cell cell = p.equals(exitPoint)? new ExitCell() : new NormalCell();
                if(x > 0) cell.setNeighbor(getCell(p.to(Direction.WEST, 1)), Direction.WEST);
                if(y > 0) cell.setNeighbor(getCell(p.to(Direction.NORTH, 1)), Direction.NORTH);
                cells.put(p, cell);
            }
        }
    }

    /**
     * Получить ширину поля {@link Field#width}.
     * @return ширина поля.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Получить высоту поля {@link Field#height}.
     * @return высота поля.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Получить ячейку по заданной координате.
     * @param point координата.
     * @return ячейка.
     */
    public Cell getCell(@NotNull Point point) {
        return cells.get(point);
    }

    /**
     * Получить робота на поле.
     * @return робот на поле.
     */
    public Robot getRobotOnField() {
        for(var i : cells.entrySet()) { // !!! Не лучше ли организовать итератор ячеек
            Robot robot = (Robot) i.getValue().getBigObject();
            if (robot != null) {
                return robot;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return width == field.width &&
                height == field.height &&
                Objects.equals(cells, field.cells) &&
                Objects.equals(exitCell, field.exitCell);
    }

    @Override
    public String toString() {
        return "Field{" +
                "cells=" + cells +
                ", width=" + width +
                ", height=" + height +
                ", exitPoint=" + exitCell +
                '}';
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link ExitCellActionListener}.
     */
    class ExitCellObserver implements ExitCellActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitCellActionEvent event) {
            fireRobotIsTeleported(event.getTeleport());
        }
    }

    /**
     * Список слушателей, подписанных на события поля.
     */
    private final ArrayList<FieldActionListener> fieldListListener = new ArrayList<>();

    /**
     * Добавить нвоого слушателя за событиями поля.
     * @param listener слушатель.
     */
    public void addFieldActionListener(FieldActionListener listener) {
        fieldListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями поля.
     * @param listener слушатель.
     */
    public void removeFieldCellActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что робот телепортировался.
     * @param teleport телепорт.
     */
    private void fireRobotIsTeleported(@NotNull Cell teleport) {
        for(FieldActionListener listener: fieldListListener) {
            FieldActionEvent event = new FieldActionEvent(listener);
            event.setTeleport(teleport);
            listener.robotIsTeleported(event);
        }
    }
}
