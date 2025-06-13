package game.model.field.cell_objects;

import game.model.events.MobileObjectListener;
import game.model.field.core.Direction;
import game.model.field.core.MobilityOnFieldProperty;
import game.model.field.core.VisibleFieldObject;
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
