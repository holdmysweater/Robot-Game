package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Объект, располагающийся в ячейке.
 */
public abstract class CellObject {

    /**
     * Позиция объекта.
     */
    protected AbstractCell position;

    /**
     * Получить позицию объекта {@link CellObject#position}.
     *
     * @return позиция объекта.
     */
    public AbstractCell getPosition() {
        return position;
    }

    /**
     * Установить позицию объекта {@link CellObject#position}.
     *
     * @param position позиция.
     * @return установлена ли позиция.
     */
    public boolean setPosition(AbstractCell position) {
        if (position != null && getPosition() != null && getPosition() != position) {
            return false;
        }

        if (position != null && !canLocateAtPosition(position)) {
            return false;
        }

        this.position = position;
        return true;
    }

    /**
     * Может ли объект располагаться в указанной позиции.
     *
     * @param abstractCell позиция.
     * @return может ли объект располагаться в указанной позиции.
     */
    public abstract boolean canLocateAtPosition(@NotNull AbstractCell abstractCell);
}
