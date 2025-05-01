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
        // Perform all my checks before initiate connection TODO translate

        // Return TRUE because connection already exist with THIS small object
        if (this.getSmallObject() == smallObject) return true;

        // Return FALSE if cell can't take small object
        if (!this.canSetSmallObject()) {
            return false;
        }

        // Established connection
        boolean success = smallObject.setPosition(this);

        // Return FALSE if connection was failed
        if (!success) {
            return false;
        }

        // Remember small object
        this.smallObject = smallObject;
        // Connection is established correctly
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
