package game.model;

import org.jetbrains.annotations.NotNull;
import game.model.events.*;
import game.model.field.ExitCell;
import game.model.field.Field;
import game.model.field.cell_objects.Robot;
import game.model.field.Labyrinth;

import java.util.ArrayList;

/**
 * Игра.
 */
public class Game {

    /**
     * Статус игры.
     */
    private GameStatus gameStatus;

    /**
     * Робот.
     */
    private Robot robot;

    /**
     * Игровое поле.
     */
    private Field gameField;

    public Game(Labyrinth labyrinth) {
        startGame(labyrinth);
    }

    /**
     * Старт новой игры
     *
     * @param labyrinth лабиринт, содержащий расстановку элементов на поле
     */
    public void startGame(@NotNull Labyrinth labyrinth) {
        setStatus(GameStatus.GAME_IS_ON);

        gameField = labyrinth.createField();

        gameField.addFieldActionListener(new FieldObserver());

        if (gameField == null) {
            throw new RuntimeException("No field created");
        }

        robot = gameField.getRobotOnField();
        robot.addRobotActionListener(new RobotObserver());

        if (robot == null) {
            throw new RuntimeException("No robot found");
        }

        robot.setUnfrozen(true);
    }

    /**
     * Прервать игру
     */
    public void abort() {
        robot.setUnfrozen(false);
        setStatus(GameStatus.GAME_ABORTED);
    }

    /**
     * Получить текущий статус игры {@link Game#gameStatus}
     *
     * @return текующий статус игры
     */
    public GameStatus getStatus() {
        return gameStatus;
    }

    private void setStatus(GameStatus status) {
        if (gameStatus != status) {
            gameStatus = status;
            fireGameStatusIsChanged(gameStatus);
        }
    }

    /**
     * Получить робота {@link Game#robot}.
     *
     * @return робот.
     */
    public Robot getRobot() {
        return robot;
    }

    /**
     * Получить игровое поле {@link Game#gameField}.
     *
     * @return игровое поле.
     */
    public Field getGameField() {
        return gameField;
    }

    /**
     * Обновить состояние игры.
     */
    private void updateGameState() {
        GameStatus status = determineOutcomeGame();
        setStatus(status);
        robot.setUnfrozen(status == GameStatus.GAME_IS_ON);
    }

    /**
     * Определить исход игры.
     *
     * @return статус игры.
     */
    private GameStatus determineOutcomeGame() {
        GameStatus result = GameStatus.GAME_IS_ON;

        if (!robot.isCapable()) {
            if (robot.isTeleported()) {
                result = GameStatus.WIN;
            } else {
                result = GameStatus.LOSS;
            }
        }

        return result;
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link RobotActionListener}.
     */
    private class RobotObserver implements RobotActionListener {

        @Override
        public void robotIsMoved(@NotNull RobotActionEvent event) {
            fireRobotIsMoved(event.getRobot());
            if (!(event.getToCell() instanceof ExitCell)) {
                updateGameState();
            }
        }

        @Override
        public void robotUnfrozenChanged(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }

        @Override
        public void robotChangedBattery(@NotNull RobotActionEvent event) {
            // Not implemented yet
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link FieldActionListener}.
     */
    private class FieldObserver implements FieldActionListener {

        @Override
        public void robotIsTeleported(@NotNull FieldActionEvent event) {
            updateGameState();
            robot.setUnfrozen(false);
            fireRobotIsTeleported();
        }
    }

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
        event.setRobot(robot);

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
}
