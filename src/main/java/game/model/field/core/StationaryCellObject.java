package game.model.field.core;

import org.jetbrains.annotations.NotNull;

public abstract class StationaryCellObject extends CellObject<Cell> {

    /**
     * Установить позицию объекта {@link CellObject#position}.
     *
     * @param position позиция.
     * @return установлена ли позиция.
     */
    @Override
    boolean setPosition(@NotNull Cell position) {
        if (!canSetPosition(position)) {
            return false;
        }

        this.position = position;
        return true;
    }

    protected boolean canSetPosition(@NotNull Cell cell) {
        return getPosition() == null;
    }
}
