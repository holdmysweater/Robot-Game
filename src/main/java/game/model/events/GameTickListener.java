package game.model.events;

import java.util.EventListener;

/**
 * Интерфейс слушателя событий тиков игры.
 */
public interface GameTickListener extends EventListener {

    /**
     * Новый игровой тик.
     */
    void gameTick();
}
