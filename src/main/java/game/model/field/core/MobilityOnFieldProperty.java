package game.model.field.core;

import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.IMobileFieldObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventObject;

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
    public boolean move() {
        if (getMovementDirection() == null) {
            System.out.println(_owner + " didn't move because direction is null");
            return false;
        }

        _owner.getApproximatingRectangle().move(_direction, _speed);
        fireObjectIsMoved();
        return true;
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

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события перемещения объекта.
     */
    private final ArrayList<MobileObjectListener> mobileObjectListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями перемещения объекта.
     *
     * @param listener слушатель.
     */
    @Override
    public void addMobileObjectActionListener(MobileObjectListener listener) {
        mobileObjectListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями перемещения объекта.
     *
     * @param listener слушатель.
     */
    @Override
    public void removeMobileObjectActionListener(MobileObjectListener listener) {
        mobileObjectListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link MobilityOnFieldProperty#mobileObjectListListener}, что робот переместился.
     */
    private void fireObjectIsMoved() {
        EventObject event = new EventObject(_owner);

        for (MobileObjectListener listener : mobileObjectListListener) {
            listener.objectIsMoved(event);
        }
    }

    //endregion
}