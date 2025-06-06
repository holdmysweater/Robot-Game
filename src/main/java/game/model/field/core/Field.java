package game.model.field.core;

import game.model.events.*;
import game.model.field.cell_objects.InteractiveCellObject;
import game.model.field.cell_objects.NonInteractiveCellObject;
import game.model.field.cell_objects.NonStationaryCellObject;
import game.model.field.population.PopulationManager;
import org.jetbrains.annotations.NotNull;

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
    }

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
                boolean success = cell.setNeighbors(neighborCells);
                assert success : "Cell " + cell + " not successfully set";

                cells.put(p, cell);
            }
        }
    }

    //endregion

    //region ОБНОВЛЕНИЕ

    public void update() {
        for (Mole mole : getMoles()) {
            mole.update();
        }
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
    private final PopulationManager populationManager = new PopulationManager();

    /**
     * Добавить объект в ячейку на поле.
     * @param object объект.
     * @param point координата ячейки.
     * @return успешность добавления объекта в ячейку.
     */
    public boolean addObjectToCell(@NotNull CellObject object, @NotNull Point point) {
        // Добавить объект в ячейку
        Cell cell = getCell(point);
        boolean result = cell.setObject(object);

        // Вернуть Ложь, если не удалось добавить объект в ячейку
        if (!result) { return false; }

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
                robot = (Robot) cell.getValue().getObject(NonStationaryCellObject.class);
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

    //region КРОТЫ

    /**
     * Множество заселённых на поле кротов.
     */
    private Set<Mole> moles = new HashSet<>();

    /**
     * Получить множество заселённый кротов.
     *
     * @return множество кротов.
     */
    public Set<Mole> getMoles() {
        return Collections.unmodifiableSet(moles);
    }

    /**
     * Заселить крота.
     *
     * @param mole крот.
     * @return удалось заселить крота.
     */
    public boolean addMole(Mole mole) {
        if (moles.contains(mole)) {
            return true;
        }
        if (!mole.setField(this)) {
            return false;
        }
        mole.addMoleActionListener(new MoleObserver());
        moles.add(mole);
        return true;
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
                exitPoint = (ExitPoint) cell.getValue().getObject(InteractiveCellObject.class);
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
        Cell robotCell = getRobot().getPosition();
        if (robotCell == null) {
            return false;
        }
        return canRobotGoFromTo(getRobot().getPosition(), getExitPoint().getPosition());
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
                    if (neigborCell.getObject(NonInteractiveCellObject.class) == null && !visited.contains(neigborCell)) {
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
