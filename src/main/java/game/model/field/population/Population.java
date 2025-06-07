package game.model.field.population;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public abstract class Population<T, K extends EventListener> {

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

    //region СЛУШАТЕЛИ

    /**
     * Список слушателей, подписанных на события популяции.
     */
    protected final ArrayList<K> populationListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void addPopulationActionListener(K listener) {
        populationListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void removePopulationActionListener(K listener) {
        populationListListener.remove(listener);
    }

    //endregion
}
