package game.model.field.core;

import game.model.events.ExitPointActionEvent;
import game.model.events.ExitPointActionListener;
import game.model.events.FieldActionEvent;
import game.model.events.FieldActionListener;
import game.model.field.cell_objects.BigCellObject;
import game.model.field.population.Population;
import game.model.field.population.PopulationManager;
import game.model.field.population.PopulationWithListeners;
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
        createApproximatingRectangle();
        initiatePopulations();
    }

    //endregion

    //region ПОСТРОЕНИЕ ПОЛЯ

    public static final int DISTANCE_BETWEEN_CELLS = 10;

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
        int defaultCellWidth = Cell.DEFAULT_WIDTH;
        int defaultCellHeight = Cell.DEFAULT_HEIGHT;
        int centerX = (defaultCellWidth + DISTANCE_BETWEEN_CELLS) * x + (defaultCellWidth / 2);
        int centerY = (defaultCellHeight + DISTANCE_BETWEEN_CELLS) * y + (defaultCellHeight / 2);
        return new Point(centerX, centerY);
    }

    //endregion

    //region АППРОКСИМИРУЮЩИЙ ПРЯМОУГОЛЬНК

    /**
     * Аппроксимирующий прямоугольник поля.
     */
    ApproximatingRectangle approximatingRectangle;

    /**
     * Получить аппроксимирующий прямоугольник поля.
     *
     * @return аппроксимирующий прямоугольник.
     */
    public ApproximatingRectangle getApproximatingRectangle() {
        return approximatingRectangle;
    }

    /**
     * Проверяет, находится ли видимый объект на поле.
     *
     * @param fieldObject Видимый объект поля.
     * @return Находится ли объект на поле.
     */
    public boolean visibleObjectOnField(VisibleFieldObject fieldObject) {
        if (fieldObject.getField() != this) {
            return false;
        }

        ApproximatingRectangle objectApproximatingRectangle = fieldObject.getApproximatingRectangle();
        return objectApproximatingRectangle.intersects(getApproximatingRectangle());
    }

    /**
     * Создать аппроксимирующий прямоугольник.
     */
    private void createApproximatingRectangle() {
        int width = Cell.DEFAULT_WIDTH * getWidth() + DISTANCE_BETWEEN_CELLS * (getWidth() - 1);
        int height = Cell.DEFAULT_HEIGHT * getHeight() + DISTANCE_BETWEEN_CELLS * (getHeight() - 1);
        Point centerPoint = new Point(width / 2, height / 2);
        approximatingRectangle = new ApproximatingRectangle(centerPoint, width, height);
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
     * Получить координату ячейки.
     * Вернёт null, если ячейка не принадлежит полю.
     *
     * @param cell ячейка.
     * @return координата.
     */
    public Point getPoint(@NotNull Cell cell) {
        for (Map.Entry<Point, Cell> entry : cells.entrySet()) {
            if (cell.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
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
    );

    private void initiatePopulations() {
        // Создать менеджер популяций
        populationManager = new PopulationManager();

        // Для каждой популяции
        for (Population population : populationManager.getPopulations()) {
            // Назначить слушателя популяции, если популяция испускает сигналы
            if (population instanceof PopulationWithListeners) {
                addListenerForPopulation((PopulationWithListeners) population);
            }
        }
    }

    private boolean addListenerForPopulation(PopulationWithListeners population) {
        // Получить класс слушателя текущей популяции
        Class<? extends EventListener> populationObserverClass = pairsOfPopulationsAndTheirObserver.get(population.getClass());

        // Проверить что класс слушателя определён
        if (populationObserverClass == null) {
            return false;
        }

        // Получить экземпляр слушателя текущей популяции
        EventListener populationObserver;

        // Попробовать создать экземпляр
        try {
            populationObserver = populationObserverClass.getDeclaredConstructor(this.getClass()).newInstance(this);
            // Обработать ошибки, если они возникли при создании экземпляра класса
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.err.println(e);
            return false;
        }

        // Добавить слушателя для популяции
        population.addPopulationActionListener(populationObserver);
        return true;
    }

    /**
     * Добавить объект на поле.
     *
     * @param object объект.
     */
    public void addObject(@NotNull FieldObject object) {
        object.setField(this);
        populationManager.addObject(object);

        // Испустить событие о новом объекте
        fireObjectWasCreated(object);
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

        // Испустить событие о новом объекте
        fireObjectWasCreated(object);

        // Вернуть Правду
        return true;
    }

    /**
     * Удалить объект с поля
     *
     * @param object
     */
    public void removeObject(FieldObject object) {
        if (object instanceof CellObject) {
            removeCellObjectFromCells((CellObject) object);
        }
        populationManager.removeObject(object);
        fireObjectWasDestroyed(object);
    }

    private void removeCellObjectFromCells(@NotNull CellObject object) {
        Collection<Cell> objectCells;

        // Получить ячейки из объекта
        if (object instanceof MobileCellObject) {
            Map<Point, Cell> objectPositionResult = (Map<Point, Cell>) object.getPosition();
            objectCells = objectPositionResult.values();
        } else if (object instanceof StationaryCellObject) {
            Cell objectPositionResult = (Cell) object.getPosition();
            objectCells = Set.of(objectPositionResult);
        } else {
            return;
        }

        // Изъять этот объект из всех ячеек
        for (Cell cell : objectCells) {
            // Проверить, что в ячейке лежит тот объект, который будем изымать
            boolean thisObjectInCell = cell.getObject(object.getClass()) == object;
            assert thisObjectInCell : "Another object in Cell. Expected: " + object + ", Actual: " + cell;

            // Изъять объект, если это он лежит в ячейке
            if (thisObjectInCell) {
                CellObject cellObject = cell.takeObject(object.getClass());
            }
        }
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
        event.setFieldObject(teleport.getTeleportedRobot());

        for (FieldActionListener listener : fieldListListener) {
            listener.robotIsTeleported(event);
        }
    }

    /**
     * Оповестить слушателей {@link Field#fieldListListener}, что добавлен новый объект.
     *
     * @param object новый объект поля.
     */
    private void fireObjectWasCreated(@NotNull FieldObject object) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setFieldObject(object);

        for (FieldActionListener listener : fieldListListener) {
            listener.objectWasCreated(event);
        }
    }

    private void fireObjectWasDestroyed(@NotNull FieldObject object) {
        FieldActionEvent event = new FieldActionEvent(this);
        event.setFieldObject(object);

        for (FieldActionListener listener : fieldListListener) {
            listener.objectWasDestroyed(event);
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
