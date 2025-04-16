package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Объект, располагающийся в ячейке.
 */
public abstract class CellObject {

    /**
     * Позиция объекта.
     */
    private AbstractCell position;

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
    boolean setPosition(@NotNull AbstractCell position) {
        //TODO Проверки DONE
        assert !(position != null && getPosition() != null && getPosition() != position);
        if (position != null && getPosition() != null && getPosition() != position) {
            return false;
        }

        assert !(position != null && !canLocateAtPosition(position));
        if (position != null && !canLocateAtPosition(position)) {
            return false;
        }

        this.position = position;
        return true;
    }

    /**
     * Удалить позицию у объекта {@link CellObject#position}.
     */
    void unsetPosition() {
        this.position = null;
    }

    /**
     * Может ли объект располагаться в указанной позиции.
     *
     * @param cell позиция.
     * @return может ли объект располагаться в указанной позиции.
     */
    protected abstract boolean canLocateAtPosition(@NotNull AbstractCell cell);
    // TODO TODO - написать, что ячейка уже дала разрешение на помещение объекта в себя
}
