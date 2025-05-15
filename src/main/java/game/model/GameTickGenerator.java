package game.model;

import game.model.events.GameTickListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;

public class GameTickGenerator {

    //region КОНСТРУКТОР

    /**
     * Таймер тиков игры.
     */
    private Timer timer;

    /**
     * Конструктор.
     *
     * @param delay время тика в миллисекундах.
     */
    GameTickGenerator(@NotNull int delay) {
        this.timer = new Timer(delay, e -> this.fireNewGameTick());
        this.timer.setRepeats(true);
    }

    //endregion

    //region УПРАВЛЕНИЕ ТИКАМИ

    /**
     * Запуск генерации тиков.
     */
    void start() {
        timer.start();
    }

    /**
     * Остановка генерации тиков.
     */
    void stop() {
        timer.stop();
    }

    /**
     * Задаёт время одного тика.
     *
     * @param delay время тика в миллисекундах.
     */
    public void setTickDelay(@NotNull int delay) {
        timer.setDelay(delay);
    }

    //endregion

    //region СЛУШАТЕЛИ ТИКОВ
    /**
     * Список слушателей тиков игры.
     */
    public final ArrayList<GameTickListener> gameTickListeners = new ArrayList<>();

    /**
     * Добавить нового слушателя события тик игры.
     *
     * @param listener слушатель.
     */
    public void addTickListener(@NotNull GameTickListener listener) {
        gameTickListeners.add(listener);
    }

    /**
     * Удалить слушателя событий тик игры
     *
     * @param listener слушатель.
     */
    public void removeTickListener(@NotNull GameTickListener listener) {
        gameTickListeners.remove(listener);
    }

    /**
     * Оповестить слушателей {@link GameTickGenerator#gameTickListeners}, о новом тике игры.
     */
    private void fireNewGameTick() {
        for (GameTickListener listener : gameTickListeners) {
            listener.gameTick();
        }
    }

    //endregion

}