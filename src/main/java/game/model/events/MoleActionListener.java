package game.model.events;

import java.util.EventListener;

import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс слушателя события класса крот {@link Mole}
 */
public interface MoleActionListener extends EventListener {
    /**
     * Крот вырыл яму
     *
     * @param event объект события класса крота.
     */
    void holeWasCreated(@NotNull MoleActionEvent event);
}
