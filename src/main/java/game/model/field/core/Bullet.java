package game.model.field.core;

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
        _mobility.move();
        return true;
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        _mobility.startMoving(direction, speed);
        return true;
    }

    //endregion


    @Override
    public int getDefaultHeight() {
        return 40;
    }

    public int getDefaultWidth() {
        return 40;
    }
}
