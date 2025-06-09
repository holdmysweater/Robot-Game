package game.model.field.core;

import game.model.field.cell_objects.IMobileFieldObject;
import org.jetbrains.annotations.NotNull;

public class MobilityOnFieldProperty implements IMobileFieldObject {

    /**
     * Владелец свойства.
     */
    private final VisibleFieldObject _owner;

    public MobilityOnFieldProperty(VisibleFieldObject owner) {
        _owner = owner;
    }

    protected int _speed;
    protected Direction _direction;

    @Override
    public void move() {
        if (getMovementDirection() == null) {
            System.out.println(_owner + " didn't move because direction is null");
            return;
        }

        _owner.getApproximatingRectangle().move(_direction, _speed);
    }

    @Override
    public boolean startMoving(@NotNull Direction direction, int speed) {
        if (getMovementDirection() != null) {
            return false;
        }

        _speed = speed;
        _direction = direction;
        return true;
    }

    @Override
    public boolean stopMoving() {
        if (getMovementDirection() == null) {
            return false;
        }

        _speed = 0;
        _direction = null;
        return true;
    }

    @Override
    public int getMovementSpeed() {
        return _speed;
    }

    @Override
    public Direction getMovementDirection() {
        return _direction;
    }
}