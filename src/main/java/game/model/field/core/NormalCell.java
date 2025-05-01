package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Ячейка выхода.
 */
public class NormalCell extends AbstractCell {

    //region МЛЕНЬКИЙ ОБЪЕКТ

    /**
     * Маленький объект, расположенный в ячейке.
     */
    private Battery smallObject = null;

    /**
     * Получить маленький объект.
     *
     * @return маленький объект.
     */
    public Battery getSmallObject() {
        return smallObject;
    }

    /**
     * Добавить маленький объект в ячейку {@link NormalCell#smallObject}.
     *
     * @param smallObject объект, добавляемый в ячейку.
     */
    public boolean setSmallObject(@NotNull Battery smallObject) {
        if (!this.canSetSmallObject()) {
            return false;
        }

        boolean success = smallObject.setPosition(this);
        if (!success) {
            return false;
        }

        this.smallObject = smallObject;

        return true;
    }

    /**
     * Может принять маленький объект.
     *
     * @return может принять маленький объект.
     */
    boolean canSetSmallObject() {
        return getSmallObject() == null;
    }

    /**
     * Изъять маленький объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link NormalCell#smallObject}.
     */
    public Battery takeSmallObject() {
        Battery result = smallObject;

        if (result != null) {
            result.unsetPosition();
            smallObject = null;
        }

        return result;
    }

    //endregion
}
