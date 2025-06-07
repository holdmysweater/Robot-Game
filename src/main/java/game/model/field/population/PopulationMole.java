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
}
