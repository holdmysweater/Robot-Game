package game.model.field.core;

/**
 * Видимый объект на поле.
 */
public abstract class VisibleFieldObject extends FieldObject {

    //region АППРОКСИМИРУЮЩИЙ ПРЯМОУГОЛЬНИК

    /**
     * Аппроксимирующий прямоугольник объекта.
     */

    private ApproximatingRectangle approximatingRectangle = null;

    //region СТАНДАРТНЫЕ РАЗМЕРЫ

    /**
     * Получить стандартную ширину объекта.
     *
     * @return стандартная ширина объекта.
     */
    public int getDefaultWidth() {
        return -1;
    }

    /**
     * Получить стандартную высоты объекта.
     *
     * @return стандартная высота объекта.
     */
    public int getDefaultHeight() {
        return -1;
    }

    //endregion

    //region ГЕТТЕРЫ

    /**
     * Получить аппроксимирующий прямоугольник объект.
     *
     * @return аппроксимирующий прямоугольник.
     */
    public ApproximatingRectangle getApproximatingRectangle() {
        return approximatingRectangle;
    }

    //endregion

    //region СЕТТЕРЫ

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     * Прямоугольник будет создан со значениями по умолчанию:
     * {@link VisibleFieldObject#getDefaultWidth()} и {@link VisibleFieldObject#getDefaultWidth()}.
     *
     * @param centerPoint центральная точка.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(Point centerPoint) {
        return this.setApproximatingRectangle(centerPoint, getDefaultWidth(), getDefaultHeight());
    }

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     * Прямоугольник будет создан со значениями по умолчанию:
     * {@link VisibleFieldObject#getDefaultWidth()} и {@link VisibleFieldObject#getDefaultHeight()}.
     *
     * @param centerX значение X центральной координаты.
     * @param centerY значение Y центральной координаты.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(int centerX, int centerY) {
        return this.setApproximatingRectangle(centerX, centerY, getDefaultWidth(), getDefaultHeight());
    }

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     *
     * @param centerX значение X центральной координаты.
     * @param centerY значение Y центральной координаты.
     * @param width   ширина.
     * @param height  высота.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(int centerX, int centerY, int width, int height) {
        return this.setApproximatingRectangle(new Point(centerX, centerY), width, height);
    }

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     *
     * @param centerPoint центральная координата.
     * @param width       ширина.
     * @param height      высоты.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(Point centerPoint, int width, int height) {
        if (width < 0 || height < 0) {
            return false;
        }
        this.approximatingRectangle = new ApproximatingRectangle(centerPoint, width, height);
        return true;
    }

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     *
     * @param approximatingRectangle аппроксимирующий прямоугольник.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(ApproximatingRectangle approximatingRectangle) {
        if (approximatingRectangle.getWidth() < 0 || approximatingRectangle.getHeight() < 0) {
            return false;
        }
        this.approximatingRectangle = approximatingRectangle.clone();
        return true;
    }

    //endregion

    //region ДЕЙСТВИЯ

    /**
     * Пересекается ли с другим объектом поля.
     *
     * @param other объект поля.
     * @return пересекаются ли объекты.
     */
    public boolean intersects(VisibleFieldObject other) {
        if (this.approximatingRectangle == null || other.approximatingRectangle == null) {
            return false;
        }
        return this.approximatingRectangle.intersects(other.approximatingRectangle);
    }

    /**
     * Центры объектов совпадают.
     *
     * @param other объект поля.
     * @return совпадают ли центры объектов.
     */
    boolean hasSameCenter(VisibleFieldObject other) {
        if (this.approximatingRectangle == null || other.approximatingRectangle == null) {
            return false;
        }
        return this.approximatingRectangle.getCenter().equals(other.approximatingRectangle.getCenter());
    }

    //endregion

    //endregion

}
