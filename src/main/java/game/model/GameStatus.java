package game.model;

/**
 * Статус игры.
 */
public enum GameStatus {

    /**
     * Игра завершена победой.
     */
    WIN,

    /**
     * Игра завершена поражением
     */
    LOSS,

    /**
     * Игра прервана.
     */
    GAME_ABORTED,

    /**
     * Игра идёт.
     */
    GAME_IS_ON
}
