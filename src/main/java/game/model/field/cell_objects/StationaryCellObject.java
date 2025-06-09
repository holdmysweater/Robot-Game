package game.model.field.cell_objects;

import game.model.field.core.Cell;
import game.model.field.core.CellObject;
import org.jetbrains.annotations.NotNull;

public abstract class StationaryCellObject extends CellObject<Cell> {

    @Override
    protected boolean canSetPosition(@NotNull Cell cell) {
        return getPosition() == null;
    }
}
