package game.model.field.core;

import game.model.field.cell_objects.BigCellObject;
import game.model.field.cell_objects.Projectile;

public class Turret extends BigCellObject {

    //region КОНСТРУКТОРЫ

    /**
     * Создать турель.
     *
     * @param direction направление турели.
     */
    public Turret(Direction direction) {
        setDirection(direction);
    }

    //endregion

    //region НАПРАВЛЕНИЕ

    /**
     * Направление турели.
     */
    Direction direction;

    /**
     * Установить направление турели.
     *
     * @param direction направление.
     */
    void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Получить направление турели.
     *
     * @return направление.
     */
    public Direction getDirection() {
        return direction;
    }

    //endregion


    //region СОЗДАНИЕ СНАРЯДОВ

    /**
     * Количество тиков для одного выстрела.
     */
    private static int FREQUENCY = 25;

    /**
     * Количество без создания снарядов.
     */
    private int tick_count_without_projectile = 0;

    /**
     * Обновить крота.
     *
     * @return Яма, если была вырыта. В противном случае null.
     */
    public Projectile update() {
        tick_count_without_projectile++;
        tick_count_without_projectile = tick_count_without_projectile % FREQUENCY;
        if (tick_count_without_projectile == 0) {
            return createProjectile();
        } else {
            return null;
        }
    }

    private Projectile createProjectile() {
        return null;
    }

    //endregion
}
