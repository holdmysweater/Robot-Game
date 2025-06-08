package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Объект, располагающийся между ячейками {@link Cell}
 */
public abstract class BetweenCellObject extends VisibleFieldObject {

    //region ПОЗИЦИЯ

    /**
     * Позиция объекта между ячейками
     */
    private BetweenCellsArea position;

    /**
     * Получить позицию {@link BetweenCellObject#position}.
     *
     * @return позиция.
     */
    protected BetweenCellsArea getPosition() {
        return position;
    }

    /**
     * Установить позицию {@link BetweenCellObject#position}.
     *
     * @param position позиция.
     * @return Удалось ли установить позицию.
     */
    boolean setPosition(@NotNull BetweenCellsArea position) {
        // Вернуть true если объект уже находится в этой позиции
        if (position.equals(this.position)) return true;

        // Вернуть false если объект не может быть размещён в этой позиции
        if (!canSetPosition(position)) return false;

        // Запомнить объект в этой позиции
        this.position = position;
        return true;
    }

    /**
     * Может ли находиться объект в позиции.
     *
     * @param newPosition проверяемая позиция.
     * @return может ли находиться объект в позиции.
     */
    protected boolean canSetPosition(@NotNull BetweenCellsArea newPosition) {
        return this.position == null;
    }

    //endregion

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BetweenCellObject that = (BetweenCellObject) o;

        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    //endregion
}
