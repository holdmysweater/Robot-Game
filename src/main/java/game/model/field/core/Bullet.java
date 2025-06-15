package game.model.field.core;

import game.model.field.cell_objects.ICollidingObject;
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
        if (getMovementDirection() == null) {
            return false;
        }

        if (getField().visibleObjectOnField(this)) {
            _mobility.move();
            return true;
        } else {
            _mobility.stopMoving();
            removeItselfFromField();
            return false;
        }
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        _mobility.startMoving(direction, speed);
        return true;
    }

    //endregion


    //region ОБЪЕКТ С КОЛЛИЗИЕЙ

    @Override
    public void processCollisionWith(@NotNull ICollidingObject object) {
        // TODO processCollisionWith Robot
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
