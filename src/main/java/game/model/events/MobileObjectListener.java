package game.model.events;

import java.util.EventListener;
import java.util.EventObject;

public interface MobileObjectListener extends EventListener {
    /**
     * Объект переместился.
     *
     * @param event событие.
     */
    void objectIsMoved(EventObject event);
}
