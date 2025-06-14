package game.model.field.population;

import game.model.field.core.Turret;

public class PopulationTurret extends Population<Turret> {
    @Override
    void update() {
        for (Turret turret : objects) {
            turret.update();
        }
        super.update();
    }
}
