package game.model.field.population;

import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.IMobileFieldObject;

public class PopulationMobileObject extends Population<IMobileFieldObject, MobileObjectListener> {
    @Override
    void update() {
        for (IMobileFieldObject object : this.objects) {
            object.move();
        }
    }
}
