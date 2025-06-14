package game.model.field.core;

import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.ICollidingObject;
import game.model.field.cell_objects.IMobileFieldObject;
import org.jetbrains.annotations.NotNull;

public abstract class Projectile extends VisibleFieldObject implements IMobileFieldObject, ICollidingObject {

    //region ПЕРЕМЕЩЕНИЕ

    /**
     * Способность перемещаться.
     */
    protected final MobilityOnFieldProperty _mobility = new MobilityOnFieldProperty(this);

    @Override
    public boolean stopMoving() {
        return _mobility.stopMoving();
    }

    @Override
    public int getMovementSpeed() {
        return _mobility.getMovementSpeed();
    }

    @Override
    public Direction getMovementDirection() {
        return _mobility.getMovementDirection();
    }

    @Override
    public void addMobileObjectActionListener(MobileObjectListener listener) {
        _mobility.addMobileObjectActionListener(listener);
    }

    @Override
    public void removeMobileObjectActionListener(MobileObjectListener listener) {
        _mobility.removeMobileObjectActionListener(listener);
    }

    //endregion

    //region ОБЪЕКТ С КОЛЛИЗИЕЙ

    @Override
    public void processCollisionWith(@NotNull ICollidingObject object) {
        // TODO processCollisionWith Robot
    }

    //endregion
}
