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
     * Стандартная ширина объекта.
     */
    protected static int DEFAULT_WIDTH = -1;

    /**
     * Стандартная высота объекта.
     */
    protected static int DEFAULT_HEIGHT = -1;

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
     * {@link VisibleFieldObject#DEFAULT_WIDTH} и {@link VisibleFieldObject#DEFAULT_HEIGHT}.
     *
     * @param centerPoint центральная точка.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(Point centerPoint) {
        return this.setApproximatingRectangle(centerPoint, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Установить аппроксимирующий прямоугольник {@link VisibleFieldObject#approximatingRectangle}.
     * Прямоугольник будет создан со значениями по умолчанию:
     * {@link VisibleFieldObject#DEFAULT_WIDTH} и {@link VisibleFieldObject#DEFAULT_HEIGHT}.
     *
     * @param centerX значение X центральной координаты.
     * @param centerY значение Y центральной координаты.
     * @return успешность установки.
     */
    boolean setApproximatingRectangle(int centerX, int centerY) {
        return this.setApproximatingRectangle(centerX, centerY, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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

    //endregion

    //endregion

}
