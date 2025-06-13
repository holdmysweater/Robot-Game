package game.model.field.core;

import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.Projectile;
import org.jetbrains.annotations.NotNull;

public class Bullet extends Projectile {

    //region КОНСТРУКТОР

    Bullet(Direction direction, int speed) {
        startMoving(direction, speed);
    }

    //endregion

    //region ПЕРЕМЕЩЕНИЕ

    @Override
    public boolean move() {
        // TODO move in Bullet
        return false;
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        // TODO startMoving in Bullet
        return true;
    }

    //endregion
}
