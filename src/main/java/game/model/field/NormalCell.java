package game.model.field;

import game.model.field.cell_objects.Battery;
import org.jetbrains.annotations.NotNull;

/**
 * Ячейка выхода.
 */
public class NormalCell extends Cell {
    /**
     * Маленький объект, расположенный в ячейке.
     */
    protected Battery smallObject = null;

    /**
     * Добавить маленький объект в ячейку {@link NormalCell#smallObject}.
     * @param smallObject объект, добавляемый в ячейку.
     */
    public boolean setSmallObject(@NotNull Battery smallObject) {
        if (this.smallObject != null) throw new RuntimeException("Cell already has a small object.");
        boolean isPositionSetSuccess = smallObject.setPosition(this);
        if(!isPositionSetSuccess) return false;
        this.smallObject = smallObject;
        return true;
    }

    /**
     * Изъять маленький объект из ячейки.
     * @return запрашиваемый объект. null - если объект не содержится в ячейке {@link NormalCell#smallObject}.
     */
    public Battery takeSmallObject() {
        Battery result = smallObject;
        smallObject = null;
        return result;
    }

    /**
     * Может принять маленький объект.
     * @return может приянть маленький объект.
     */
    public boolean canTakeSmallObject() {
        return smallObject == null;
    }

}
