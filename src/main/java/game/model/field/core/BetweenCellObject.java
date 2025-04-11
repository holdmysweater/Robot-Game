package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

/**
 * Объект, располагающийся между ячейками {@link AbstractCell}
 */
public abstract class BetweenCellObject {

    /**
     * Позиция объекта между ячейками
     */
    protected BetweenCellsPosition position;

    /**
     * Получить позицию {@link BetweenCellObject#position}.
     *
     * @return позиция.
     */
    public BetweenCellsPosition getPosition() {
        return position;
    }

    /**
     * Установить позицию {@link BetweenCellObject#position}.
     *
     * @param position позиция.
     * @return Удалось ли установить позицию.
     */
    boolean setPosition(@NotNull BetweenCellsPosition position) {
        if (this.position != null && this.position.equals(position)) {
            return true;
        }

        if (this.position != null) {
            return false;
        }

        this.position = position;
        return true;
    }

    /**
     * Может ли находиться объект в позиции.
     *
     * @param newPosition проверяемая позиция.
     * @return может ли находиться объект в позиции.
     */
    public boolean canSetAtPosition(@NotNull BetweenCellsPosition newPosition) {
        Map<Direction, AbstractCell> neighborCells = newPosition.getNeighborCells();

        var iterator = neighborCells.entrySet().iterator();

        boolean result = true;
        while (iterator.hasNext() && result) {
            var i = iterator.next();
            BetweenCellObject neighborWall = i.getValue().getNeighborObstacle(i.getKey().getOppositeDirection());
            result = (neighborWall == null) || (neighborWall == this);
        }

        return result;
    }

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
}
