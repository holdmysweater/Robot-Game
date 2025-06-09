package game.model.field.cell_objects;

import game.model.field.core.Direction;
import org.jetbrains.annotations.NotNull;

public interface IMobileFieldObject {
    void move();
    boolean startMoving(@NotNull Direction direction, int speed);
    boolean stopMoving();
    int getMovementSpeed();
    Direction getMovementDirection();
}