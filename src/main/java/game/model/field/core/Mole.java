package game.model.field.core;

import game.model.events.GameTickListener;
import game.model.events.MoleActionListener;
import game.model.field.cell_objects.NonInteractiveCellObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class Mole implements GameTickListener {

    //region РЫТЬ ЯМУ

    /**
     * Вероятность события "крот выроет яму когда проснётся"
     */
    private static int PROBABILITY = 25;


    /**
     * Разбудить крота.
     *
     * @return Яма, если была вырыта. В противном случае null.
     */
    private Hole wakeUp() {
        Random rand = new Random();
        if (rand.nextInt(1, 101) <= PROBABILITY) {
            return this.digHole();
        } else {
            return null;
        }
    }

    /**
     * Рыть яму на поле.
     *
     * @return Яма, если удалось её вырыть. В противном случае null.
     */
    private Hole digHole() {
        Cell cell = this.findCellForHole();
        if (cell == null) {
            return null;
        }
        Hole hole = new Hole();
        boolean success = cell.setObject(hole);
        assert success : "Hole must be installed.";
        if (!success) {
            return null;
        }
        this.fireMoleDigNewHole(hole);
        return hole;
    }


    /**
     * Получить свободную ячейку для новой ямы.
     *
     * @return Ячейка, если удалось её найти. В противном случае null.
     */
    private Cell findCellForHole() {
        if (this.field == null) {
            return null;
        }
        return this.field.getEmptyCell();
    }

    //endregion

    //region ПОЛЕ

    /**
     * Поле.
     */
    private Field field;

    /**
     * Заселить крота на поле.
     *
     * @param field поле.
     * @return успешность.
     */
    boolean setField(@NotNull Field field) {
        if (!this.canSetField(field)) {
            return false;
        }
        this.field = field;
        return true;
    }

    /**
     * Может заселиться на поле.
     *
     * @param field поле.
     * @return может заселиться на поле.
     */
    private boolean canSetField(@NotNull Field field) {
        return this.field == null || this.field == field;
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события игры.
     */
    private final ArrayList<MoleActionListener> moleListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void addMoleActionListener(MoleActionListener listener) {
        moleListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void removeMoleActionListener(MoleActionListener listener) {
        moleListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Mole#moleListListener}, что крот вырыл новую яму.
     *
     * @param hole яма.
     */
    private void fireMoleDigNewHole(Hole hole) {
        for (MoleActionListener listener : moleListListener) {
            listener.digNewHole(hole);
        }
    }

    //endregion

    //region ОБРАБОТКА ТИКОВ

    @Override
    public void gameTick() {
        wakeUp();
    }

    //endregion

}
