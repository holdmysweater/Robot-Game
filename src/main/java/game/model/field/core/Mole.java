package game.model.field.core;

import game.Debug;

import java.util.Random;

public class Mole extends FieldObject {

    //region РЫТЬ ЯМУ

    /**
     * Задержка перед тем, как вырыть яму
     */
    private static final int DELAY = 300;

    private int currentDelay = 0;

    /**
     * Вероятность события "крот выроет яму когда проснётся"
     */
    private static int PROBABILITY = 25;

    /**
     * Обновить крота.
     *
     * @return Яма, если была вырыта. В противном случае null.
     */
    public Hole update() {
        currentDelay++;

        if (currentDelay < DELAY) {
            return null;
        }

        Debug.log(Debug.Options.MoleUpdated, "Mole " + this + ": updated");

        Random rand = new Random();
        if (rand.nextInt(1, 101) <= PROBABILITY) {
            currentDelay = 0;
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
        Debug.log(Debug.Options.MoleDigStart, "Mole" + this + ": dig hole start");

        Cell cell = this.findCellForHole();
        if (cell == null) {
            return null;
        }
        Point cellPoint = getField().getPoint(cell);
        if (cellPoint == null) {
            return null;
        }

        Hole hole = new Hole();
        boolean success = getField().addObjectToCell(hole, cellPoint);
        assert success : "Hole must be installed.";
        if (!success) {
            return null;
        }

        Debug.log(Debug.Options.MoleDigSuccess, "Mole" + this + ": dig hole success");

        return hole;
    }


    /**
     * Получить свободную ячейку для новой ямы.
     *
     * @return Ячейка, если удалось её найти. В противном случае null.
     */
    private Cell findCellForHole() {
        if (getField() == null) {
            return null;
        }
        return getField().getEmptyCell();
    }

    //endregion
}
