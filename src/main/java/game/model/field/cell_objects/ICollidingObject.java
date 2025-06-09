package game.model.field.cell_objects;

import org.jetbrains.annotations.NotNull;

public interface ICollidingObject {
    void processCollisionWith(@NotNull ICollidingObject object);
}
