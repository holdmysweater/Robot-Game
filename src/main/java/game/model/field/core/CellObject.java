package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Объект, располагающийся в ячейке.
 */
public abstract class CellObject<T> extends VisibleFieldObject {

    //region ПОЗИЦИЯ

    /**
     * Позиция объекта.
     */
    private T position;

    /**
     * Получить позицию объекта {@link CellObject#position}.
     *
     * @return позиция объекта.
     */
    public T getPosition() {
        return position;
    }

    /**
     * Установить позицию объекта {@link CellObject#position}.
     *
     * @param position позиция.
     * @return установлена ли позиция.
     */
    boolean setPosition(@NotNull T position) {
        if (!canSetPosition(position)) {
            return false;
        }

        this.position = position;
        return true;
    }

    /**
     * Может ли объект располагаться в указанной позиции.
     *
     * @param cell позиция.
     * @return может ли объект располагаться в указанной позиции.
     */
    protected abstract boolean canSetPosition(@NotNull T cell);

    /**
     * Удалить позицию у объекта {@link CellObject#position}.
     */
    void unsetPosition() {
        this.position = null;
    }

    //endregion

}
