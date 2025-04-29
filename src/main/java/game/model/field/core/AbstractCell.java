package game.model.field.core;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Ячейка.
 */
public abstract class AbstractCell {
    /*---------- ОБЪЕКТ В ЯЧЕЙКЕ ----------*/
    /**
     * Большой объект, расположенный в ячейке.
     */
    private Robot bigObject = null;

    /**
     * Добавить большой объект в ячейку {@link AbstractCell#bigObject}.
     *
     * @param bigObject объект, добавляемый в ячейку.
     */
    public boolean setBigObject(@NotNull Robot bigObject) {
        // Perform all my checks before initiate connection

        // Return TRUE because connection already exist with THIS big object
        if (this.bigObject == bigObject) return true;

        // Return FALSE if cell can't take big object
        if (!this.canTakeBigObject()) {
            return false;
        }

        // Established connection
        boolean success = bigObject.setPosition(this);

        // Return FALSE if connection was failed
        if (!success) {
            return false;
        }

        // Remember big object
        this.bigObject = bigObject;
        // Connection is established correctly
        return true;
    }

    /**
     * Изъять большой объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link AbstractCell#bigObject}.
     */
    public Robot takeBigObject() {
        Robot result = bigObject;

        if (result != null) {
            result.unsetPosition();
            bigObject = null;
        }

        return result;
    }

    /**
     * Получить большой объект.
     *
     * @return большой объект.
     */
    public Robot getBigObject() {
        return bigObject;
    }

    /**
     * Может принять большой объект.
     *
     * @return может принять большой объект.
     */
    public boolean canTakeBigObject() {
        return getBigObject() == null;
    }
    // TODO TODO - принять, а не изъять (canPutObject)

    /*---------- СОСЕДНИЕ ЯЧЕЙКИ ----------*/

    /**
     * Получить соседние ячейки.
     *
     * @return соседние ячейки.
     */
    public final Map<Direction, AbstractCell> getNeighborCells() {
        Map<Direction, AbstractCell> neighborCells = new HashMap<Direction, AbstractCell>();
        for (Direction direction : Direction.values()) {
            AbstractCell cell = getNeighborCell(direction);
            if (cell != null) {
                neighborCells.put(direction, cell);
            }
        }
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area == null) {
            return null;
        } else {
            return area.getNeighborCell(direction);
        }
    }

    /**
     * Установить ячейки соседними.
     *
     * @param neighborCells список ячеек с соответствующими направлениями соседства.
     * @return успешность.
     */
    boolean setNeighbor(@NotNull Map<Direction, AbstractCell> neighborCells) {
        for (Direction direction : neighborCells.keySet()) {
            if (!setNeighbor(neighborCells.get(direction), direction)) return false;
        }

        // Создать недостающее количество промежуточных областей для ячейки
        createBetweenCellsAreas();
        return true;
    }


    /**
     * Установить ячейку соседней.
     * Взять её область между ячейками.
     *
     * @param neighborCell соседняя ячейка.
     * @param direction    направление.
     * @return успешность.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     */
    private boolean setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        // Вернуть true если у объекта уже установлена связь с этой ячейкой в этом направлении
        if (getNeighborCell(direction) == neighborCell) return true;

        // Вернуть false если у любой из клеток нет возможности установить соседство
        if (!canEstablishNeighbor(neighborCell, direction)) return false;

        // Получить соседнюю область от нового соседа
        BetweenCellsArea neighborArea = neighborCell.getNeighborArea(direction.getOppositeDirection());

        // Проверка случая, при котором обе ячейки имеют свои области.
        // Запрещённая операция. Попытка соединить друг с другом сегменты из клеток.
        if (neighborArea != null && getNeighborCell(direction) != null) {
            throw new RuntimeException("Try connecting two cells with their own neighbor area. Cells:"
                    + this + neighborArea + ".");
        }

        // Если у соседа нет области, то создать новую область для себя и соседа
        if (neighborArea == null) {
            BetweenCellsArea newNeighborArea = new BetweenCellsArea(this, direction.getOppositeDirection());
            neighborAreas.put(direction, newNeighborArea);
            boolean success = newNeighborArea.addNeighbor(neighborCell, direction);

            // Если установить связь не удалось, то выбросить исключение.
            // Ячейка не имеющая соседних областей должна всегда позволять устанавливать соединение.
            assert success;
            if (!success) return false;
        }
        // Иначе записать себя в эту область
        else {
            boolean success = neighborArea.addNeighbor(this, direction.getOppositeDirection());
            if (!success) return false;
            neighborAreas.put(direction, neighborArea);
        }

        // Успешно завершить установку связи с соседом.
        return true;
    }

    /**
     * Может установить соседство с ячейкой по заданному направлению.
     *
     * @param neighborCell клетка сосед.
     * @param direction    направление соседства.
     * @return возможность соседства.
     */
    boolean canEstablishNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        // Вернуть true если уже установлена связь с этим объектом в этом направлении
        if (getNeighborCell(direction) == neighborCell) return true;

        // Вернуть false если связь по этому направлению уже установлена с другим объектом
        if (getNeighborCell(direction) != null) return false;

        // Вернуть false если связь устанавливается с самим собой
        if (neighborCell == this) return false;

        // Может быть соседом
        return true;
    }

    /**
     * Является ли ячейка соседом.
     *
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull AbstractCell other) {
        return getNeighborCells().containsValue(other);
    }



    /*---------- ОБЪЕКТЫ МЕЖДУ ЯЧЕЙКАМИ ----------*/
    /**
     * Области, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellsArea> neighborAreas = new EnumMap<>(Direction.class);

    /**
     * Получить соседние области, располагающиеся между ячейками {@link AbstractCell#neighborAreas}.
     *
     * @return соседние области, располагающиеся между ячейками.
     */
    public Map<Direction, BetweenCellsArea> getNeighborAreas() {
        return Collections.unmodifiableMap(neighborAreas);
    }

    /**
     * Дополнить все недостающие области между ячейками.
     */
    private void createBetweenCellsAreas() {
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.removeAll(neighborAreas.keySet());
        for (Direction direction : directions) {
            neighborAreas.put(direction, new BetweenCellsArea(this, direction.getOppositeDirection()));
        }
    }

    /**
     * Получить соседнюю область, располагающийся между ячейками {@link AbstractCell#neighborAreas} в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя область, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellsArea getNeighborArea(@NotNull Direction direction) {
        return neighborAreas.get(direction);
    }
}
