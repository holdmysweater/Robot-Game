package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Точка-координата на игровом поле {@link Field}
 */
public class Point {

    /**
     * Координата X.
     */
    private final int x;

    /**
     * Координата Y.
     */
    private final int y;

    /**
     * Создать координаты.
     *
     * @param x координата по оси Х.
     * @param y координата по оси Y.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Получить координату X ({@link Point#x}).
     *
     * @return координата X.
     */
    public int getX() {
        return x;
    }

    /**
     * Получить координату Y ({@link Point#y}).
     *
     * @return координата Y.
     */
    public int getY() {
        return y;
    }

    /**
     * Получить новую точку с заданным смещением в заданном направлении.
     *
     * @param direction направление.
     * @param delta     смещение.
     * @return новая точка с заданным смещением в заданном направлении.
     */
    public Point to(@NotNull Direction direction, int delta) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case NORTH:
                newY -= delta;
                break;
            case SOUTH:
                newY += delta;
                break;
            case EAST:
                newX += delta;
                break;
            case WEST:
                newX -= delta;
                break;
        }

        return new Point(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point p = (Point) o;

        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
