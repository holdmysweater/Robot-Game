package game.model.field.population;

import game.model.field.core.Mole;

public class PopulationMole extends Population<Mole> {
    @Override
    void update() {
        for (Mole mole : this.objects) {
            mole.update();
        }
    }
}
