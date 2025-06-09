package game.model.field.core;

import game.model.field.cell_objects.Projectile;
import org.jetbrains.annotations.NotNull;

public class Bullet extends Projectile {

    //region ПЕРЕМЕЩЕНИЕ

    @Override
    public void move() {
        // TODO move in Bullet
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        // TODO startMoving in Bullet
        return true;
    }

    //endregion
}
