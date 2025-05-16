package game.model;

import game.model.field.core.Hole;
import game.model.field.core.Mole;
import org.jetbrains.annotations.NotNull;
import game.model.events.*;
import game.model.field.core.Field;
import game.model.field.core.Robot;
import game.model.labyrinths.Labyrinth;

import java.util.ArrayList;
import javax.swing.Timer;

/**
 * Игра.
 */
public class Game {

    //region КОНСТРУКТОРЫ

    public Game(Labyrinth labyrinth, int tickDelay) {
        this.gameTickGenerator = new GameTickGenerator(tickDelay);
        this.gameTickGenerator.addTickListener(new TickObserver());
        start(labyrinth);
    }

    //endregion

    //region СТАТУС ИГРЫ

    /**
     * Старт новой игры
     *
     * @param labyrinth лабиринт, содержащий расстановку элементов на поле
     */
    public void start(@NotNull Labyrinth labyrinth) {
        setStatus(GameStatus.GAME_IS_ON);

        gameField = labyrinth.createField();
        this.addTickListener(gameField);
        gameField.addFieldActionListener(new FieldObserver());

        if (gameField == null) {
            throw new RuntimeException("No field created");
        }

        if (getRobot() == null) {
            throw new RuntimeException("No robot found");
        }

        getRobot().addRobotActionListener(new RobotObserver());
        for (Mole mole : gameField.getMoles()) {
            mole.addMoleActionListener(new MoleObserver());
        }

        getRobot().setUnfrozen(true);
        this.gameTickGenerator.start();
    }

    /**
     * Остановка игры
     */
    private void stop() {
        this.gameTickGenerator.stop();
        getRobot().setUnfrozen(false);
    }

    /**
     * Статус игры.
     */
    private GameStatus gameStatus;

    /**
     * Задержка обновления статуса игры.
     */
    private final int GAME_STATUS_UPDATE_DELAY = 5;

    /**
     * Получить текущий статус игры {@link Game#gameStatus}
     *
     * @return текущий статус игры
     */
    public GameStatus getStatus() {
        return gameStatus;
    }

    /**
     * Установить текущий статус игры
     *
     * @param status новый статус игры
     */
    private void setStatus(GameStatus status) {
        if (gameStatus != status) {
            gameStatus = status;
            fireGameStatusIsChanged(gameStatus);
        }
    }

    /**
     * Обновить состояние игры с задержкой {@link Game#GAME_STATUS_UPDATE_DELAY}.
     */
    private void updateGameStatusWithDelay() {
        Timer timer = new Timer(GAME_STATUS_UPDATE_DELAY, e -> updateGameStatus());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Обновить состояние игры.
     */
    private void updateGameStatus() {
        GameStatus status = GameStatus.GAME_IS_ON;

        if (!getRobot().isCapable()) {
            if (getRobot().isTeleported()) {
                status = GameStatus.WIN;
            } else {
                status = GameStatus.LOSS_NO_CHARGE;
            }
            stop();
        } else if (!getGameField().canRobotGoToExitCell()) {
            status = GameStatus.LOSS_NO_WIN_PATH;
            stop();
        } else {
            getRobot().setUnfrozen(true);
        }

        setStatus(status);
    }

    //endregion

    //region ПОЛЕ

    /**
     * Игровое поле.
     */
    private Field gameField;

    /**
     * Получить игровое поле {@link Game#gameField}.
     *
     * @return игровое поле.
     */
    public Field getGameField() {
        return gameField;
    }

    //endregion

    //region РОБОТ

    /**
     * Получить робота {@link Field#getRobot()}.
     *
     * @return робот.
     */
    public Robot getRobot() {
        return gameField.getRobot();
    }

    //endregion

    //region СЛУШАТЕЛИ

    /**
     * Класс, реализующий наблюдение за событиями {@link RobotActionListener}.
     */
    private class RobotObserver implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            fireRobotIsMoved(event.getRobot());
            updateGameStatusWithDelay();
        }

        @Override
        public void robotUnfrozenChanged(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            updateGameStatusWithDelay();
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link FieldActionListener}.
     */
    private class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            getRobot().setUnfrozen(false);
            fireRobotIsTeleported();
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link Mole}
     */
    private class MoleObserver implements MoleActionListener {
        @Override
        public void digNewHole(@NotNull Hole hole) {
            updateGameStatusWithDelay();
        }
    }

    private class TickObserver implements GameTickListener {

        @Override
        public void gameTick() {
            fireNewGameTick();
        }
    }

    //endregion

    //region СИГНАЛЫ

    //region РОБОТ

    /**
     * Список слушателей, подписанных на события игры.
     */
    private final ArrayList<GameActionListener> gameActionListeners = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void addGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.add(listener);
    }

    /**
     * Удалить слушателя за событиями игры.
     *
     * @param listener слушатель.
     */
    public void removeGameActionListener(@NotNull GameActionListener listener) {
        gameActionListeners.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что робот переместился.
     *
     * @param robot робот, который переместился.
     */
    private void fireRobotIsMoved(@NotNull Robot robot) {
        GameActionEvent event = new GameActionEvent(this);
        event.setRobot(robot);

        for (GameActionListener listener : gameActionListeners) {
            listener.robotIsMoved(event);
        }
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что робот телепортировался.
     */
    private void fireRobotIsTeleported() {
        GameActionEvent event = new GameActionEvent(this);
        event.setRobot(getRobot());

        for (GameActionListener listener : gameActionListeners) {
            listener.robotIsTeleported(event);
        }
    }

    /**
     * Оповестить слушателей {@link Game#gameActionListeners}, что статус игры изменился.
     *
     * @param status статус игры.
     */
    private void fireGameStatusIsChanged(@NotNull GameStatus status) {
        GameActionEvent event = new GameActionEvent(this);
        event.setStatus(status);

        for (GameActionListener listener : gameActionListeners) {
            listener.gameStatusChanged(event);
        }
    }

    //endregion

    //region ИГРОВЫЕ ТИКИ

    /**
     * Генератор игровых тиков.
     */
    private final GameTickGenerator gameTickGenerator;

    //region СЛУШАТЕЛИ ТИКОВ
    /**
     * Список слушателей тиков игры.
     */
    private final ArrayList<GameTickListener> gameTickListeners = new ArrayList<>();

    /**
     * Добавить нового слушателя события тик игры.
     *
     * @param listener слушатель.
     */
    private void addTickListener(@NotNull GameTickListener listener) {
        gameTickListeners.add(listener);
    }

    /**
     * Удалить слушателя событий тик игры
     *
     * @param listener слушатель.
     */
    private void removeTickListener(@NotNull GameTickListener listener) {
        gameTickListeners.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Game#gameTickListeners}, о новом тике игры.
     */
    private void fireNewGameTick() {
        for (GameTickListener listener : gameTickListeners) {
            listener.gameTick();
        }
    }

    //endregion

    //endregion

    //endregion
}
