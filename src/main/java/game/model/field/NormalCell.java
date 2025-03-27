package game.model.field;

import org.jetbrains.annotations.NotNull;

/**
 * Ячейка выхода.
 */
public class NormalCell extends Cell {
    /**
     * Маленький объект, расположенный в ячейке.
     */
    protected CellObject smallObject = null;

    /**
     * Добавить маленький объект в ячейку {@link NormalCell#smallObject}.
     * @param cellObject объект, добавляемый в ячейку.
     */
    public boolean addSmallObject(@NotNull CellObject cellObject) {
        if (smallObject != null) throw new RuntimeException("Cell already has a small object.");
        boolean isPositionSetSuccess = cellObject.setPosition(this);
        if(!isPositionSetSuccess) return false;
        smallObject = cellObject;
        return true;
    }

    /**
     * Изъять маленький объект из ячейки.
     * @return запрашиваемый объект. null - если объект не содержится в ячейке {@link NormalCell#smallObject}.
     */
    public CellObject takeSmallObject() {
        CellObject result = smallObject;
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
