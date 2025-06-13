package game;

import java.util.EnumSet;
import java.util.Set;

/**
 * Класс Debug для удобной отладочной печати с поддержкой цветного вывода.
 *
 * <p>Использование:
 * <ul>
 *   <li>Включите нужные опции отладки с помощью {@link #enable(Options)} или групповых методов, например, {@link #enableRobotOptions()}.</li>
 *   <li>Вызывайте {@link #log(Options, String)} для вывода сообщений; сообщения будут выводиться только если соответствующая опция включена.</li>
 *   <li>Используйте {@link #enableAll()} или {@link #disableAll()} для быстрого включения или выключения всех опций.</li>
 *   <li>Для вывода цветного текста используйте методы, например, {@link ConsoleColor#red(String)} или {@link ConsoleColor#green(String)}.</li>
 * </ul>
 */
public class Debug {

    //region Перечисление опций

    /**
     * Возможные типы отладочных сообщений. У каждой опции есть свой цвет.
     */
    public enum Options {

        //region Опции робота
        RobotKeyPressed(ConsoleColor.BRIGHT_GREEN),
        RobotMoveStarted(ConsoleColor.GREEN),
        RobotLeftDepartingCell(ConsoleColor.GREEN),
        RobotMoveFinished(ConsoleColor.BRIGHT_GREEN),
        RobotMoveFailed(ConsoleColor.BRIGHT_RED),
        RobotChangeBatteryFailed(ConsoleColor.YELLOW),
        //endregion

        //region Опции мобильного объекта
        MobileObjectMoved(ConsoleColor.PURPLE),
        MobileObjectMoveFailed(ConsoleColor.WHITE),
        //endregion

        //region Опции крота
        MoleUpdated(ConsoleColor.WHITE),
        MoleDigStart(ConsoleColor.WHITE),
        MoleDigSuccess(ConsoleColor.BRIGHT_YELLOW);
        //endregion

        private final String color;

        Options(String color) {
            this.color = color;
        }

        /**
         * Получить цвет ANSI для этой опции.
         *
         * @return ANSI строка цвета
         */
        public String getColor() {
            return color;
        }

        /**
         * Получить имя опции с цветом.
         *
         * @return строка с цветом
         */
        public String toColoredString() {
            return color + this.name() + ConsoleColor.RESET;
        }
    }

    //endregion

    //region Поля

    /**
     * Множество включённых опций отладки.
     */
    private static final Set<Options> enabledOptions = EnumSet.noneOf(Options.class);

    //endregion

    //region Включение/выключение отдельных опций

    /**
     * Включить определённую опцию отладки.
     *
     * @param option опция для включения
     */
    public static void enable(Options option) {
        enabledOptions.add(option);
    }

    /**
     * Выключить определённую опцию отладки.
     *
     * @param option опция для выключения
     */
    public static void disable(Options option) {
        enabledOptions.remove(option);
    }

    /**
     * Проверить, включена ли опция.
     *
     * @param option опция для проверки
     * @return true, если опция включена, иначе false
     */
    public static boolean isEnabled(Options option) {
        return enabledOptions.contains(option);
    }

    //endregion

    //region Включение/выключение всех опций

    /**
     * Включить все опции отладки.
     */
    public static void enableAll() {
        for (Options option : Options.values()) {
            enabledOptions.add(option);
        }
    }

    /**
     * Выключить все опции отладки.
     */
    public static void disableAll() {
        enabledOptions.clear();
    }

    //endregion

    //region Групповое включение/выключение опций

    /**
     * Включить все опции отладки, связанные с роботом.
     */
    public static void enableRobotOptions() {
        enabledOptions.add(Options.RobotKeyPressed);
        enabledOptions.add(Options.RobotMoveStarted);
        enabledOptions.add(Options.RobotLeftDepartingCell);
        enabledOptions.add(Options.RobotMoveFinished);
        enabledOptions.add(Options.RobotMoveFailed);
        enabledOptions.add(Options.RobotChangeBatteryFailed);
    }

    /**
     * Выключить все опции отладки, связанные с роботом.
     */
    public static void disableRobotOptions() {
        enabledOptions.remove(Options.RobotKeyPressed);
        enabledOptions.remove(Options.RobotMoveStarted);
        enabledOptions.remove(Options.RobotMoveFinished);
        enabledOptions.remove(Options.RobotMoveFailed);
        enabledOptions.remove(Options.RobotChangeBatteryFailed);
    }

    /**
     * Включить все опции отладки, связанные с мобильным объектом.
     */
    public static void enableMobileObjectOptions() {
        enabledOptions.add(Options.MobileObjectMoved);
        enabledOptions.add(Options.MobileObjectMoveFailed);
    }

    /**
     * Выключить все опции отладки, связанные с мобильным объектом.
     */
    public static void disableMobileObjectOptions() {
        enabledOptions.remove(Options.MobileObjectMoved);
        enabledOptions.remove(Options.MobileObjectMoveFailed);
    }

    /**
     * Включить все опции отладки, связанные с кротом.
     */
    public static void enableMoleOptions() {
        enabledOptions.add(Options.MoleUpdated);
        enabledOptions.add(Options.MoleDigStart);
        enabledOptions.add(Options.MoleDigSuccess);
    }

    /**
     * Выключить все опции отладки, связанные с кротом.
     */
    public static void disableMoleOptions() {
        enabledOptions.remove(Options.MoleUpdated);
        enabledOptions.remove(Options.MoleDigStart);
        enabledOptions.remove(Options.MoleDigSuccess);
    }

    //endregion

    //region Логирование

    /**
     * Вывести сообщение, если соответствующая опция включена.
     * Название опции будет выведено в цвете.
     *
     * @param option  опция, к которой относится сообщение
     * @param message сообщение для вывода
     */
    public static void log(Options option, String message) {
        if (!isEnabled(option)) return;
        System.out.println("[" + option.toColoredString() + "] " + message);
    }

    //endregion

    /**
     * Класс с ANSI-кодами для цветного вывода в консоли.
     */
    public static class ConsoleColor {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";
        public static final String WHITE = "\u001B[37m";
        public static final String BRIGHT_BLACK = "\u001B[90m";
        public static final String BRIGHT_RED = "\u001B[91m";
        public static final String BRIGHT_GREEN = "\u001B[92m";
        public static final String BRIGHT_YELLOW = "\u001B[93m";
        public static final String BRIGHT_BLUE = "\u001B[94m";
        public static final String BRIGHT_PURPLE = "\u001B[95m";
        public static final String BRIGHT_CYAN = "\u001B[96m";
        public static final String BRIGHT_WHITE = "\u001B[97m";

        public static String red(String text)    { return RED + text + RESET; }
        public static String green(String text)  { return GREEN + text + RESET; }
        public static String yellow(String text) { return YELLOW + text + RESET; }
        public static String blue(String text)   { return BLUE + text + RESET; }
        public static String purple(String text) { return PURPLE + text + RESET; }
        public static String cyan(String text)   { return CYAN + text + RESET; }
        public static String white(String text)  { return WHITE + text + RESET; }
        public static String brightRed(String text)    { return BRIGHT_RED + text + RESET; }
        public static String brightGreen(String text)  { return BRIGHT_GREEN + text + RESET; }
        public static String brightYellow(String text) { return BRIGHT_YELLOW + text + RESET; }
        public static String brightBlue(String text)   { return BRIGHT_BLUE + text + RESET; }
        public static String brightPurple(String text) { return BRIGHT_PURPLE + text + RESET; }
        public static String brightCyan(String text)   { return BRIGHT_CYAN + text + RESET; }
        public static String brightWhite(String text)  { return BRIGHT_WHITE + text + RESET; }
    }
}