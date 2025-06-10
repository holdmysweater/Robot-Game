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
    protected T position;

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
    abstract boolean setPosition(@NotNull T position);

    /**
     * Удалить позицию у объекта {@link CellObject#position}.
     */
    void unsetPosition() {
        this.position = null;
    }

    //endregion

}
