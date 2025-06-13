package game.model.field.core;

import game.model.events.*;
import game.model.field.cell_objects.BigCellObject;
import game.model.field.population.Population;
import game.model.field.population.PopulationManager;
import game.model.field.population.PopulationMole;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Поле.
 */
public class Field {

    //region КОНСТРУКТОРЫ

    /**
     * Конструктор.
     *
     * @param width  ширина. Должна быть > 0.
     * @param height высота. Должна быть > 0.
     * @throws IllegalArgumentException если ширина, высота или координата ячейки переданы некорректные.
     */
    public Field(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Field width must be more than 0");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("Field height must be more than 0");
        }

        this.width = width;
        this.height = height;

        buildField();
        initiatePopulations();
    }

    //endregion

    //region ПОСТРОЕНИЕ ПОЛЯ

    static final int DISTANCE_BETWEEN_CELLS = 6;

    /**
     * Построить игровое поле.
     */
    private void buildField() {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Point p = new Point(x, y);

                Map<Direction, Cell> neighborCells = new HashMap<>();

                if (x > 0) {
                    neighborCells.put(Direction.WEST, getCell(p.to(Direction.WEST, 1)));
                }

                if (y > 0) {
                    neighborCells.put(Direction.NORTH, getCell(p.to(Direction.NORTH, 1)));
                }

                Cell cell = new Cell();
                cell.setApproximatingRectangle(this.calculateApproximatingRectangleCenter(p));
                boolean success = cell.setNeighbors(neighborCells);
                assert success : "Cell " + cell + " not successfully set";

                cells.put(p, cell);
            }
        }
    }

    /**
     * Рассчитать центра аппроксимирующего прямоугольника для клетки на поле.
     *
     * @param p координаты ячейки на поле.
     * @return координата центра аппроксимирующего прямоугольника для клетки.
     */
    private Point calculateApproximatingRectangleCenter(Point p) {
        int x = p.getX();
        int y = p.getY();
        int defaultCellWidth = Cell.getDefaultWidth();
        int defaultCellHeight = Cell.getDefaultHeight();
        int centerX = (defaultCellWidth + DISTANCE_BETWEEN_CELLS) * x + (defaultCellWidth / 2);
        int centerY = (defaultCellHeight + DISTANCE_BETWEEN_CELLS) * y + (defaultCellHeight / 2);
        return new Point(centerX, centerY);
    }

    //endregion

    //region ОБНОВЛЕНИЕ

    public void update() {
        populationManager.update();
    }

    //endregion

    //region СВОЙСТВА

    //region ШИРИНА

    /**
     * Ширина поля.
     */
    private final int width;

    /**
     * Получить ширину поля {@link Field#width}.
     *
     * @return ширина поля.
     */
    public int getWidth() {
        return width;
    }

    //endregion

    //region ВЫСОТА

    /**
     * Высота поля.
     */
    private final int height;

    /**
     * Получить высоту поля {@link Field#height}.
     *
     * @return высота поля.
     */
    public int getHeight() {
        return height;
    }

    //endregion

    //endregion

    //region ЯЧЕЙКИ

    /**
     * Ячейки поля.
     */
    private final Map<Point, Cell> cells = new HashMap<>();

    /**
     * Получить ячейку по заданной координате.
     *
     * @param point координата.
     * @return ячейка.
     */
    public Cell getCell(@NotNull Point point) {
        return cells.get(point);
    }

    /**
     * Получить случайную свободную клетку.
     *
     * @return Свободная клетка, если удалось её найти. В противном случае null.
     */
    public Cell getEmptyCell() {
        List<Cell> cellsList = new ArrayList<>(cells.values());
        Collections.shuffle(cellsList);
        for (Cell cell : cellsList) {
            if (cell.isEmpty()) {
                return cell;
            }
        }
        return null;
    }

    //endregion

    //region ПОПУЛЯЦИИ

    /**
     * Менеджер популяций.
     */
    private PopulationManager populationManager;

    private final Map<Class<? extends Population>, Class<? extends EventListener>> pairsOfPopulationsAndTheirObserver = Map.ofEntries(
            Map.entry(PopulationMole.class, MoleObserver.class)
    );

    private void initiatePopulations() {
        // Создать менеджер популяций
        populationManager = new PopulationManager();

        // Для каждой популяции
        for (Population population : populationManager.getPopulations()) {
            // Получить класс слушателя текущей популяции
            Class<? extends EventListener> populationObserverClass = pairsOfPopulationsAndTheirObserver.get(population.getClass());

            // Проверить что класс слушателя определён
            assert populationObserverClass == null : "Population observer class for population " + population.getClass().getSimpleName() + " not initialized.";
            if (populationObserverClass == null) {
                continue;
            }

            // Получить экземпляр слушателя текущей популяции
            EventListener populationObserver = null;
            try {
                // Попробовать создать экземпляр
                populationObserver = populationObserverClass.getDeclaredConstructor(this.getClass()).newInstance(this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                // Обработать ошибки, если они возникли при создании экземпляра класса
                System.err.println(e);
                assert populationObserver != null : "Can't create instance of " + populationObserverClass.getSimpleName();
            } finally {
                // Перейти к следующей популяции, если экземпляр слушателя не был создан
                if (populationObserver == null) {
                    continue;
                }
            }

            // Добавить слушателя для популяции
            population.addPopulationActionListener(populationObserver);
        }
    }

    /**
     * Добавить объект на поле.
     *
     * @param object объект.
     */
    public void addObject(@NotNull FieldObject object) {
        object.setField(this);
        populationManager.addObject(object);
    }

    /**
     * Добавить объект в ячейку на поле.
     *
     * @param object объект.
     * @param point  координата ячейки.
     * @return успешность добавления объекта в ячейку.
     */
    public boolean addObjectToCell(@NotNull CellObject object, @NotNull Point point) {
        // Добавить объект в ячейку
        Cell cell = getCell(point);
        boolean result = cell.setObject(object);

        // Вернуть Ложь, если не удалось добавить объект в ячейку
        if (!result) {
            return false;
        }

        // Установить поле для объекта
        object.setField(this);

        // Добавить объект в популяции
        populationManager.addObject(object);

        // Вернуть Правду
        return true;
    }

    //endregion

    //region РОБОТ

    /**
     * Получить робота на поле.
     *
     * @return робот на поле.
     */
    public Robot getRobot() {
        for (var cell : cells.entrySet()) {
            Robot robot = null;
            try {
                robot = (Robot) cell.getValue().getObject(Robot.class);
                if (robot != null) {
                    return robot;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    //endregion

    //region ТОЧКА ВЫХОДА

    /**
     * Получить точку выхода на поле.
     *
     * @return точка выхода на поле.
     */
    public ExitPoint getExitPoint() {
        for (var cell : cells.entrySet()) {
            ExitPoint exitPoint = null;
            try {
                exitPoint = (ExitPoint) cell.getValue().getObject(ExitPoint.class);
                if (exitPoint != null) {
                    return exitPoint;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Инициализирован ли слушатель точки выхода
     */
    boolean isInitiatedExitPoint = false;

    /**
     * Добавить слушателя на точку выхода.
     */
    void InitiateExitPointListener() {
        if (isInitiatedExitPoint) return;

        if (getExitPoint() == null) {
            throw new RuntimeException("No exit point found!");
        }

        getExitPoint().addExitPointActionListener(new ExitPointObserver());
        isInitiatedExitPoint = true;
    }

    //endregion

    //region СЛУШАТЕЛИ

    /**
     * Класс, реализующий наблюдение за событиями {@link ExitPointActionListener}.
     */
    class ExitPointObserver implements ExitPointActionListener {

        @Override
        public void robotIsTeleported(@NotNull ExitPointActionEvent event) {
            fireRobotIsTeleported(event.getTeleport());
        }
    }

    /**
     * Класс, реализующий наблюдение за событиями {@link Mole}
     */
    private class MoleObserver implements MoleActionListener {
        @Override
        public void holeWasCreated(@NotNull MoleActionEvent event) {
            fireHoleWasCreated(event.getHole());
        }
    }

    //endregion

    //region СИГНАЛЫ

    /**
     * Список слушателей, подписанных на события поля.
     */
    private final ArrayList<FieldActionListener> fieldListListener = new ArrayList<>();

    /**
     * Добавить нового слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void addFieldActionListener(FieldActionListener listener) {
        InitiateExitPointListener();
        fieldListListener.add(listener);
    }

    /**
     * Удалить слушателя за событиями поля.
     *
     * @param listener слушатель.
     */
    public void removeFieldCellActionListener(FieldActionListener listener) {
        fieldListListener.remove(listener);
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что робот телепортировался.
     *
     * @param teleport телепорт.
     */
    private void fireRobotIsTeleported(@NotNull ExitPoint teleport) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setCellObject(teleport.getTeleportedRobot());

        for (FieldActionListener listener : fieldListListener) {
            listener.robotIsTeleported(event);
        }
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что вырыта яма.
     *
     * @param hole яма.
     */
    private void fireHoleWasCreated(@NotNull Hole hole) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setCellObject(hole);

        for (FieldActionListener listener : fieldListListener) {
            listener.holeWasCreated(event);
        }
    }

    //endregion

    //region ПРОВЕРКА МАРШРУТА

    /**
     * Может ли робот дойти до точки выхода
     *
     * @return может ли робот дойти до точки выхода
     */
    public boolean canRobotGoToExitCell() {
        if (getRobot().isTeleported()) {
            return true;
        }
        Cell robotCell = getRobot().getIdleCellPosition();
        if (robotCell == null) {
            robotCell = getRobot().getArrivalCellPosition();
            if (robotCell == null) {
                return false;
            }
        }
        return canRobotGoFromTo(robotCell, getExitPoint().getPosition());
    }

    /**
     * Может ли робот дойти от заданной начальной ячейки до заданной конечной ячейки
     *
     * @param startCell начальная ячейка
     * @param endCell   конечная ячейка
     * @return может ли робот дойти от заданной начальной ячейки до заданной конечной ячейки
     */
    private boolean canRobotGoFromTo(@NotNull Cell startCell, @NotNull Cell endCell) {
        List<Cell> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();

        queue.add(startCell);
        while (!queue.isEmpty()) {
            Cell cell = queue.removeFirst();
            if (cell.equals(endCell)) {
                return true;
            }
            visited.add(cell);
            for (Map.Entry<Direction, BetweenCellsArea> entry : cell.getNeighborAreas().entrySet()) {
                if (entry.getValue().getObstacle() == null) {
                    Cell neigborCell = entry.getValue().getNeighborCell(entry.getKey());
                    if (neigborCell == null) {
                        continue;
                    }
                    if (neigborCell.getObject(BigCellObject.class) == null && !visited.contains(neigborCell)) {
                        queue.add(neigborCell);
                        visited.add(neigborCell);
                    }
                }
            }
        }
        return false;
    }

    //endregion

    //region OBJECT

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Field field = (Field) o;

        return width == field.width && height == field.height && Objects.equals(cells, field.cells);
    }

    @Override
    public String toString() {
        return "Field{" + "cells=" + cells + ", width=" + width + ", height=" + height + '}';
    }

    //endregion
}
