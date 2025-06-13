package game.model;

import game.model.events.*;
import game.model.field.core.Field;
import game.model.field.core.FieldObject;
import game.model.field.core.Hole;
import game.model.field.core.Robot;
import game.model.labyrinths.Labyrinth;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Игра.
 */
public class Game {

    //region КОНСТРУКТОРЫ

    public Game(Labyrinth labyrinth, int tickDelay) {
        this.timer = new Timer(tickDelay, e -> this.timeOut());
        this.timer.setRepeats(true);
        start(labyrinth);
    }

    //endregion

    //region ИГРОВЫЕ ТИКИ

    /**
     * Таймер тиков игры.
     */
    private Timer timer;

    /**
     * Обновить поле.
     */
    private void timeOut() {
        gameField.update();
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
        gameField.addFieldActionListener(new FieldObserver());

        if (gameField == null) {
            throw new RuntimeException("No field created");
        }

        if (getRobot() == null) {
            throw new RuntimeException("No robot found");
        }

        getRobot().addRobotActionListener(new RobotObserver());

        getRobot().setUnfrozen(true);
        timer.start();
    }

    /**
     * Остановка игры
     */
    private void stop() {
        timer.stop();
        getRobot().setUnfrozen(false);
    }

    /**
     * Статус игры.
     */
    private GameStatus gameStatus;

    /**
     * Задержка обновления статуса игры.
     */
    private final int GAME_STATUS_UPDATE_DELAY = 1;

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
        public void robotStartedMoving(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }

        @Override
        public void robotFinishedMoving(@NotNull RobotActionEvent event) {
            fireRobotIsMoved(event.getRobot());
            updateGameStatus();
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
            fireRobotIsTeleported();
            updateGameStatusWithDelay();
        }

        @Override
        public void objectWasCreated(@NotNull FieldActionEvent event) {
            FieldObject fieldObject = event.getFieldObject();
            if (fieldObject instanceof Hole) {
                updateGameStatus();
            }
        }
    }

    //endregion

    //region СИГНАЛЫ

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
}
