package game.model.field.cell_objects;

import game.model.events.MobileObjectListener;
import game.model.field.core.Direction;
import org.jetbrains.annotations.NotNull;

public interface IMobileFieldObject {
    boolean move();

    boolean startMoving(@NotNull Direction direction, int speed);

    boolean stopMoving();

    int getMovementSpeed();

    Direction getMovementDirection();

    void addMobileObjectActionListener(MobileObjectListener listener);

    void removeMobileObjectActionListener(MobileObjectListener listener);
}