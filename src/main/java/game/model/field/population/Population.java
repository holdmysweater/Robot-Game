package game.model.field.population;

import java.util.HashSet;
import java.util.Set;

public abstract class Population<T> {

    //region ОБНОВЛЕНИЕ

    /**
     * Обновляет объекты популяции.
     */
    void update() {
        removeObjects();
    }

    //endregion

    //region ОБЪЕКТЫ

    /**
     * Множество объектов популяции.
     */
    protected Set<T> objects = new HashSet<T>();

    /**
     * Добавление объекта в популяцию.
     *
     * @param object объект популяции.
     */
    void addObject(T object) {
        objects.add(object);
    }


    /**
     * Удалить объект из популяции.
     *
     * @param object объект
     */
    void removeObject(T object) {
        needRemoveObjects.add(object);
    }

    /**
     * Список объектов, которые нужно убрать из популяции.
     */
    private Set<T> needRemoveObjects = new HashSet<>();

    /**
     * Убирает объекты из популяции, на основании списка объектов, подлежащих изъятию {@link Population#needRemoveObjects}.
     */
    private void removeObjects() {
        for (T object : needRemoveObjects) {
            objects.remove(object);
        }
        needRemoveObjects.clear();
    }

    //endregion

}
