package game.model.events;

import java.util.EventListener;

import game.model.field.core.Hole;
import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс слушателя события класса крот {@link Mole}
 */
public interface MoleActionListener extends EventListener {
    /**
     * Крот выкопал новую яму.
     *
     * @param hole яма.
     */
    void digNewHole(@NotNull Hole hole);
}
