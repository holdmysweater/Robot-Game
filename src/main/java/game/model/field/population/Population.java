package game.model.field.population;

import java.util.HashSet;
import java.util.Set;

public abstract class Population<T> {

    //region ОБНОВЛЕНИЕ

    /**
     * Обновляет объекты популяции.
     */
    abstract void update();

    //endregion

    //region ОБЪЕКТЫ

    /**
     * Множество объектов популяции.
     */
    protected Set<T> objects = new HashSet<T>();

    /**
     * Добавление объекта в популяцию
     */
    void addObject(T object) {
        objects.add(object);
    }

    //endregion

}
