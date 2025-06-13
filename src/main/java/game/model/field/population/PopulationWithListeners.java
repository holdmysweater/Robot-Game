package game.model.field.population;

import java.util.ArrayList;
import java.util.EventListener;

public abstract class PopulationWithListeners<T, K extends EventListener> extends Population<T> {

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
