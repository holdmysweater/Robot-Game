package game.model.field.core;

import game.model.field.cell_objects.NonInteractiveCellObject;
import org.jetbrains.annotations.NotNull;

public class Hole extends NonInteractiveCellObject {
    @Override
    protected boolean canSetPosition(@NotNull Cell cell) {
        return getPosition() == null;
    }
}
