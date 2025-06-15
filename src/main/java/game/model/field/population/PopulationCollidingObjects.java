package game.model.field.population;

import game.model.field.cell_objects.ICollidingObject;
import game.model.field.core.VisibleFieldObject;

import java.util.List;

public class PopulationCollidingObjects extends Population<ICollidingObject> {

    @Override
    void update() {
        List<ICollidingObject> objectsList = List.copyOf(objects);
        for (int i = 0; i < objectsList.size(); i++) {
            VisibleFieldObject object1;
            if (objectsList.get(i) instanceof VisibleFieldObject) {
                object1 = (VisibleFieldObject) objectsList.get(i);
            } else {
                continue;
            }

            for (int j = i + 1; j < objectsList.size(); j++) {
                VisibleFieldObject object2;
                if (objectsList.get(i) instanceof VisibleFieldObject) {
                    object2 = (VisibleFieldObject) objectsList.get(j);
                } else {
                    continue;
                }

                if (object1.intersects(object2)) {
                    ((ICollidingObject) object1).processCollisionWith((ICollidingObject) object2);
                    ((ICollidingObject) object2).processCollisionWith((ICollidingObject) object1);
                }
            }
        }
        super.update();
    }
}
