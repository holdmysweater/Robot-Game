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
     * Соседние ячейки.
     */
    private final Map<Direction, AbstractCell> neighborCells = new EnumMap<>(Direction.class);

    /**
     * Получить соседние ячейки {@link AbstractCell#neighborCells}.
     *
     * @return соседние ячейки.
     */
    public final Map<Direction, AbstractCell> getNeighborCells() {
        return Collections.unmodifiableMap(neighborCells);
    }

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public AbstractCell getNeighborCell(@NotNull Direction direction) {
        return neighborCells.get(direction);
    }


    boolean setNeighbor(@NotNull Map<Direction, AbstractCell> neighborCells) {
        for (Direction direction : neighborCells.keySet()) {
             if (!setNeighbor(neighborCells.get(direction), direction)) return false;
        }

        // Создать недостающее количество промежуточных областей для ячейки
        createBetweenCellsAreas();
        return true;
    }


    /**
     * Установить ячейку соседней {@link AbstractCell#neighborCells}.
     * Взять её область между ячейками.
     *
     * @param neighborCell соседняя ячейка.
     * @param direction    направление.
     * @throws IllegalArgumentException если переданная ячейка не может быть соседней.
     * @return успешность.
     */
    private boolean setNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        // Вернуть true если объект уже установлена связь с этой ячейкой в этом направлении
        if (neighborCells.get(direction) == neighborCell) return true;

        // Вернуть false если у любой из клеток нет возможности установить соседство
        if (!canEstablishNeighbor(neighborCell, direction) ||
                !neighborCell.canEstablishNeighbor(this, direction.getOppositeDirection()))
            return false;

        // Запомнить ячейку как соседа
        neighborCells.put(direction, neighborCell);

        // Вернуть false если не удалось установить связь с соседом
        if (!neighborCell.setNeighbor(this, direction.getOppositeDirection())) return false;

        // Если есть область между ячейками, то добавить в неё нового соседа
        BetweenCellsArea betweenCellsArea = neighborAreas.get(direction);
        if (betweenCellsArea != null) {
            betweenCellsArea.addNeighbor(neighborCell, direction.getOppositeDirection());
        }
        // Иначе запомнить область между ячейками соседа
        else {
            neighborAreas.put(direction, neighborCell.getNeighborArea(direction.getOppositeDirection()));
        }

        // Успешно завершить установку связи с соседом.
        return true;
    }

    boolean canEstablishNeighbor(@NotNull AbstractCell neighborCell, @NotNull Direction direction) {
        // Вернуть true если уже установлена связь с этим объектом в этом направлении
        if (neighborCells.get(direction) == neighborCell) return true;

        // Вернуть false если связь уже установлена с другим объектом
        if (neighborCells.get(direction) != null) return false;

        // Вернуть false если связь устанавливается с самим собой, связь уже имеется с этим объектом или по этому направлению
        if (neighborCell == this || neighborCells.containsKey(direction) || neighborCells.containsValue(neighborCell)) {
            return false;
        }

        // Вернуть false если область между ячейками известна и она уже имеет связь c другой клеткой
        if (neighborAreas.get(direction) != null &&
                neighborAreas.get(direction).getNeighborCell(direction.getOppositeDirection()) != null &&
                neighborAreas.get(direction).getNeighborCell(direction.getOppositeDirection()) != neighborCell)
            return false;

        // Может быть соседом
        return true;
    }
    /**
     * Получить направление с соседней ячейкой.
     *
     * @param other соседняя ячейка.
     * @return направление.
     */
    public Direction getNeighborDirection(@NotNull AbstractCell other) {
        for (var cell : neighborCells.entrySet()) {
            if (cell.getValue().equals(other)) return cell.getKey();
        }
        return null;
    }

    /**
     * Является ли ячейка соседом.
     *
     * @param other соседняя ячейка.
     * @return Является ли ячейка соседом.
     */
    public boolean isNeighbor(@NotNull AbstractCell other) {
        return neighborCells.containsValue(other);
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
            neighborAreas.put(direction, new BetweenCellsArea(this, direction));
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
