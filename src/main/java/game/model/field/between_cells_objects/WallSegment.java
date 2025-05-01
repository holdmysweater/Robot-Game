package game.model.field.between_cells_objects;

import game.model.field.core.BetweenCellObject;

import java.util.Objects;

/**
 * Сегмент стены.
 */
public class WallSegment extends BetweenCellObject {

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WallSegment wallSegment = (WallSegment) o;

        if (getPosition() == null || wallSegment.getPosition() == null) return false;

        return Objects.equals(getPosition(), wallSegment.getPosition());
    }

    @Override
    public String toString() {
        return "Wall{" + "position=" + getPosition() + '}';
    }

    //endregion OBJECT
}
