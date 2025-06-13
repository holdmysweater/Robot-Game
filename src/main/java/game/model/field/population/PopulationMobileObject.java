package game.model.field.population;

import game.Debug;
import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.IMobileFieldObject;
import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

public class PopulationMobileObject extends Population<IMobileFieldObject, MobileObjectListener> {
    @Override
    void update() {
        for (IMobileFieldObject object : this.objects) {
            object.move();
        }
    }

    @Override
    void addObject(IMobileFieldObject object) {
        super.addObject(object);
        object.addMobileObjectActionListener(new PopulationMobileObject.MobileObjectObserver());
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link Mole}
     */
    private class MobileObjectObserver implements MobileObjectListener {
        @Override
        public void objectIsMoved(EventObject event) {
            fireMobileObjectMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link PopulationMole#populationListListener}, что вырыта яма.
     *
     * @param event яма.
     */
    private void fireMobileObjectMoved(@NotNull EventObject event) {
        Debug.log(Debug.Options.MobileObjectMoved, "Object " + event.getSource() + ": moved");

        for (MobileObjectListener listener : populationListListener) {
            listener.objectIsMoved(event);
        }
    }
}
