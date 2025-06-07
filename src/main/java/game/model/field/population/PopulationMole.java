package game.model.field.population;

import game.model.events.MoleActionEvent;
import game.model.events.MoleActionListener;
import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;

public class PopulationMole extends Population<Mole, MoleActionListener> {
    @Override
    void update() {
        for (Mole mole : this.objects) {
            mole.update();
        }
    }

    @Override
    void addObject(Mole object) {
        super.addObject(object);
        object.addMoleActionListener(new MoleObserver());
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link Mole}
     */
    private class MoleObserver implements MoleActionListener {
        @Override
        public void holeWasCreated(@NotNull MoleActionEvent event) {
            fireHoleWasCreated(event);
        }
    }

    /**
     * Оповестить слушателей {@link PopulationMole#populationListListener}, что вырыта яма.
     *
     * @param event яма.
     */
    private void fireHoleWasCreated(@NotNull MoleActionEvent event) {
        for (MoleActionListener listener : populationListListener) {
            listener.holeWasCreated(event);
        }
    }
}
