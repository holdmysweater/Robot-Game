package game.model.field.core;

import org.jetbrains.annotations.NotNull;

/**
 * Ячейка выхода.
 */
public class NormalCell extends AbstractCell {
    /**
     * Маленький объект, расположенный в ячейке.
     */
    private Battery smallObject = null;

    /**
     * Добавить маленький объект в ячейку {@link NormalCell#smallObject}.
     *
     * @param smallObject объект, добавляемый в ячейку.
     */
    public boolean setSmallObject(@NotNull Battery smallObject) {
        //TODO Проверка
        if (this.smallObject != null) {
            throw new IllegalArgumentException("Cell already has a small object.");
        }

        boolean isPositionSetSuccess = smallObject.setPosition(this);

        if (!isPositionSetSuccess) {
            return false;
        }

        this.smallObject = smallObject;
        return true;
    }

    /**
     * Изъять маленький объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link NormalCell#smallObject}.
     */
    public Battery takeSmallObject() {
        Battery result = smallObject;

        if (result != null) {
            result.setPosition(null);
            smallObject = null;
        }

        return result;
    }

    /**
     * Получить маленький объект.
     *
     * @return маленький объект.
     */
    public Battery getSmallObject() {
        return smallObject;
    }

    /**
     * Может принять маленький объект.
     *
     * @return может принять маленький объект.
     */
    public boolean canTakeSmallObject() {
        return smallObject == null;
    }
    //TODO Общий доступ???
}
