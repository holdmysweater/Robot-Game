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
        //TODO Проверка DONE
        assert this.smallObject == null;
        if (this.smallObject != null) {
            return false;
        }

        boolean success = smallObject.setPosition(this);

        assert success;
        if (!success) {
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
            result.unsetPosition();
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
    boolean canTakeSmallObject() {
        return smallObject == null;
    }
    //TODO Общий доступ??? DONE
    // TODO TODO - симметрично с большим объектом
    // TODO TODO - принять, а не изъять
}
