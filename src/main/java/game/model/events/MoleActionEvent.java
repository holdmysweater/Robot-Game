package game.model.events;

import org.jetbrains.annotations.NotNull;
import game.model.field.core.Hole;
import game.model.field.core.Mole;

import java.util.EventObject;

public class MoleActionEvent extends EventObject {

    //region КОНСТРУКТОР

    /**
     * Создаёт событие.
     *
     * @param source объект, на котором изначально произошло событие
     * @throws IllegalArgumentException если source равен null
     */
    public MoleActionEvent(Object source) {
        super(source);
    }

    //endregion

    //region КРОТ

    /**
     * Крот.
     */
    private Mole mole;

    /**
     * Установить крота {@link MoleActionEvent#mole}.
     *
     * @param mole крот.
     */
    public void setMole(@NotNull Mole mole) {
        this.mole = mole;
    }

    /**
     * Получить крота {@link MoleActionEvent#mole}.
     *
     * @return крот.
     */
    public Mole getMole() {
        return mole;
    }

    //endregion

    //region ЯМА

    /**
     * Яма.
     */
    private Hole hole;

    /**
     * Установить яму {@link MoleActionEvent#hole}.
     *
     * @param hole яма.
     */
    public void setHole(@NotNull Hole hole) {
        this.hole = hole;
    }

    /**
     * Получить яму {@link MoleActionEvent#hole}.
     *
     * @return яма.
     */
    public Hole getHole() {
        return hole;
    }

    //endregion
}
