package game.model.field.core;

import game.model.field.cell_objects.BigCellObject;

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

    @Override
    public int getDefaultHeight() {
        return Cell.DEFAULT_HEIGHT;
    }

    @Override
    public int getDefaultWidth() {
        return Cell.DEFAULT_WIDTH;
    }

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
     * Скорость пули.
     */
    private static int PROJECTILE_SPEED = 10;

    /**
     * Получить скорость пули.
     *
     * @return скорость пуля.
     */
    private int getProjectileSpeed() {
        return PROJECTILE_SPEED;
    }

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
        Bullet bullet = new Bullet(getDirection(), getProjectileSpeed());
        bullet.setApproximatingRectangle(getProjectileStartPosition(getDirection()));
        getField().addObject(bullet);
        return bullet;
    }

    private Point getProjectileStartPosition(Direction direction) {
        Point turretCentralPoint = getApproximatingRectangle().getCenter();
        return switch (direction) {
            case NORTH, SOUTH -> turretCentralPoint.to(direction, getDefaultHeight() / 2);
            case EAST, WEST -> turretCentralPoint.to(direction, getDefaultWidth() / 2);
        };
    }

    //endregion
}
