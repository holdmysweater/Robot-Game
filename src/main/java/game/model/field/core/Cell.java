package game.model.field.core;

import game.model.field.cell_objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Ячейка.
 */
public class Cell extends VisibleFieldObject {

    //region РАЗМЕРЫ АПРОКСИМИРУЮЩЕГО ПРЯМОУГОЛЬНИКА

    static {
        DEFAULT_WIDTH = 100;
        DEFAULT_HEIGHT = 100;
    }

    //endregion

    //region ОБЪЕКТЫ ВНУТРИ ЯЧЕЙКИ

    /**
     * Конечные классы иерархии.
     */
    List<Class<? extends CellObject>> endOfHierarchyClasses = List.of(
        BigCellObject.class,
        SmallCellObject.class,
        LowProfileCellObject.class
    );

    /**
     * Объекты, расположенные в ячейке.
     */
    private Map<Class<? extends CellObject>, CellObject> objects = new HashMap<>();

    /**
     * Получить объекты.
     *
     * @return объекты.
     */
    public Map<Class<? extends CellObject>, CellObject> getObjects() {
        return Collections.unmodifiableMap(objects);
    }

    /**
     * Получить объект в ячейке {@link Cell#objects}.
     *
     * @param type класс объекта, который нужно получить.
     * @return первый найденный запрашиваемый объект, null - если объект не содержится в ячейке {@link Cell#objects}.
     */
    public CellObject getObject(Class<? extends CellObject> type) {
        for (Map.Entry<Class<? extends CellObject>, CellObject> entry : objects.entrySet()) {
            if (type.isAssignableFrom(entry.getValue().getClass())) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Поместить объект в ячейку {@link Cell#objects}.
     *
     * @param object объект, добавляемый в ячейку.
     * @return успешность.
     * @throws IllegalArgumentException если запрашиваемый класс не является поддерживаемым абстрактным классом.
     */
    public boolean setObject(@NotNull CellObject object) {
        if (object instanceof MobileCellObject) {
            return setObject((MobileCellObject) object, CellObjectStatus.IDLE);
        }

        Class<? extends CellObject> type = getType(object);

        if (!endOfHierarchyClasses.contains(type) || !this.canSetObject(type)) {
            return false;
        }

        boolean success = object.setPosition(this);
        if (!success) {
            return false;
        }

        objects.put(type, object);

        return true;
    }

    /**
     * Может принять объект.
     *
     * @param type класс объекта, который нужно проверить.
     * @return может принять объект.
     */
    public boolean canSetObject(@NotNull Class<? extends CellObject> type) {
        if (SmallCellObject.class.isAssignableFrom(type)) {
            return getObject(SmallCellObject.class) == null && getObject(BigCellObject.class) == null;
        }

        if (BigCellObject.class.isAssignableFrom(type)) {
            return getObjects().isEmpty();
        }

        if (LowProfileCellObject.class.isAssignableFrom(type)) {
            return getObject(LowProfileCellObject.class) == null && getObject(BigCellObject.class) == null;
        }

        return false;
    }

    /**
     * Изъять объект из ячейки.
     *
     * @return запрашиваемый объект, null - если объект не содержится в ячейке {@link Cell#objects}.
     */
    public CellObject takeObject(Class<? extends CellObject> type) {
        if (!endOfHierarchyClasses.contains(type)) {
            throw new IllegalArgumentException("Unsupported class type: " + type.getName());
        }

        CellObject result = objects.remove(type);

        if (result instanceof MobileCellObject) {
            mobileCellObjectStatus.remove(result);
        }

        if (result != null) {
            if (result instanceof MobileCellObject) {
                ((MobileCellObject) result).unsetPosition(this);
            }
            else {
                result.unsetPosition();
            }
        }

        return result;
    }

    /**
     * Проверяет пустая ли клетка.
     *
     * @return пустая ли клетка.
     */
    public boolean isEmpty() {
        return objects.isEmpty();
    }

    /**
     * Определить тип объекта в иерархии объектов в ячейке
     *
     * @param object объект
     * @return конечный класс иерархии, если он был определен, иначе CellObject
     */
    private Class<? extends CellObject> getType(CellObject object) {
        Class<? extends CellObject> type = object.getClass();

        switch (object) {
            case SmallCellObject smallCellObject -> type = SmallCellObject.class;
            case BigCellObject bigCellObject -> type = BigCellObject.class;
            case LowProfileCellObject lowProfileCellObject -> type = LowProfileCellObject.class;
            default -> {}
        }

        return type;
    }

    //endregion

    //region МОБИЛЬНЫЕ ОБЪЕКТЫ

    /**
     * Статусы мобильных объектов в ячейке.
     */
    private Map<MobileCellObject, CellObjectStatus> mobileCellObjectStatus = new HashMap<>();

    /**
     * Поместить мобильный объект в ячейку {@link Cell#objects}.
     *
     * @param object объект, добавляемый в ячейку.
     * @param status статус
     * @return успешность.
     * @throws IllegalArgumentException если запрашиваемый класс не является поддерживаемым абстрактным классом.
     */
    public boolean setObject(@NotNull MobileCellObject object, @NotNull CellObjectStatus status) {
        Class<? extends CellObject> type = getType(object);

        if (!endOfHierarchyClasses.contains(type) || !this.canSetObject(type)) {
            return false;
        }

        boolean success = object.setPosition(this, status);
        if (!success) {
            return false;
        }

        objects.put(type, object);
        mobileCellObjectStatus.put(object, status);

        return true;
    }

    /**
     * Обновить статус мобильного объекта в ячейке {@link Cell#objects}.
     *
     * @param object объект
     * @param status статус
     * @return успешность.
     */
    public boolean updateMobileObjectStatus(@NotNull MobileCellObject object, @NotNull CellObjectStatus status) {
        Class<? extends CellObject> type = getType(object);

        if (!endOfHierarchyClasses.contains(type) || !this.getObject(type).equals(object)) {
            return false;
        }

        boolean success = object.setPosition(this, status);
        if (!success) {
            return false;
        }

        mobileCellObjectStatus.put(object, status);

        return true;
    }

    /**
     * Получить статус объекта.
     *
     * @param object объект.
     * @return статус.
     */
    public CellObjectStatus getObjectStatus(@NotNull MobileCellObject object) {
        return mobileCellObjectStatus.get(object);
    }

    //endregion

    //region СОСЕДНИЕ ЯЧЕЙКИ

    /**
     * Получить соседнюю ячейку в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя ячейка, null, если в заданном направлении нет соседней ячейки.
     */
    public Cell getNeighborCell(@NotNull Direction direction) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area == null) {
            return null;
        } else {
            return area.getNeighborCell(direction);
        }
    }

    /**
     * Установить соседние ячейки.
     *
     * @param neighborCells список ячеек с соответствующими направлениями соседства.
     * @return успешность.
     */
    boolean setNeighbors(Map<Direction, Cell> neighborCells) {
        if (neighborCells != null) {
            for (Direction direction : neighborCells.keySet()) {
                if (!setNeighbor(neighborCells.get(direction), direction)) return false;
            }
        }

        surroundSelfWithBetweenCellsAreas();
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
    private boolean setNeighbor(@NotNull Cell neighborCell, @NotNull Direction direction) {
        if (this == neighborCell || this.intersects(neighborCell)) {
            return false;
        }
        BetweenCellsArea area = neighborCell.getNeighborArea(direction.getOppositeDirection());
        return switch (direction) {
            case NORTH -> area.setVerticalNeighbors(neighborCell, this);
            case SOUTH -> area.setVerticalNeighbors(this, neighborCell);
            case EAST -> area.setHorizontalNeighbors(this, neighborCell);
            case WEST -> area.setHorizontalNeighbors(neighborCell, this);
        };
    }

    /**
     * Является ли соседом.
     *
     * @param cell другая ячейка
     * @return является ли другая ячейка соседом
     */
    public boolean isNeighbor(@NotNull Cell cell) {
        for (Direction direction : Direction.values()) {
            if (getNeighborCell(direction) == cell) return true;
        }
        return false;
    }

    //endregion

    //region ОБЛАСТИ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Области, располагающиеся между ячейками.
     */
    private final Map<Direction, BetweenCellsArea> neighborAreas = new EnumMap<>(Direction.class);

    /**
     * Получить соседнюю область, располагающуюся между ячейками {@link Cell#neighborAreas} в заданном направлении.
     *
     * @param direction направление.
     * @return соседняя область, располагающийся между ячейками в заданном направлении.
     */
    public BetweenCellsArea getNeighborArea(@NotNull Direction direction) {
        return neighborAreas.get(direction);
    }

    /**
     * Получить множество соседних областей ячейки.
     *
     * @return множество соседних областей ячейки.
     */
    public Map<Direction, BetweenCellsArea> getNeighborAreas() {
        return Collections.unmodifiableMap(neighborAreas);
    }

    /**
     * Задать соседнюю область между ячейками.
     *
     * @param direction    направление.
     * @param neighborArea соседняя область между ячейками.
     * @return успешность.
     */
    boolean setNeighborArea(@NotNull Direction direction, @NotNull BetweenCellsArea neighborArea) {
        BetweenCellsArea area = neighborAreas.get(direction);
        if (area != null && !area.equals(neighborArea)) return false;
        neighborAreas.put(direction, neighborArea);
        return true;
    }

    /**
     * Окружить себя промежуточными областями.
     */
    private void surroundSelfWithBetweenCellsAreas() {
        Set<Direction> directions = new HashSet<>(List.of(Direction.values()));
        directions.removeAll(neighborAreas.keySet());

        for (Direction direction : directions) {
            BetweenCellsArea area = new BetweenCellsArea();
            switch (direction) {
                case NORTH -> area.setVerticalNeighbors(null, this);
                case SOUTH -> area.setVerticalNeighbors(this, null);
                case EAST -> area.setHorizontalNeighbors(this, null);
                case WEST -> area.setHorizontalNeighbors(null, this);
            }
        }
    }

    //endregion

    //region ПРЕПЯТСТВИЯ МЕЖДУ ЯЧЕЙКАМИ

    /**
     * Получить соседнее препятствие.
     *
     * @param direction направление.
     * @return соседнее препятствие, располагающееся между ячейками в заданном направлении.
     */
    public BetweenCellObject getNeighborObstacle(@NotNull Direction direction) {
        return getNeighborArea(direction).getObstacle();
    }

    /**
     * Установить соседнее препятствие.
     *
     * @param direction направление.
     * @param obstacle  соседнее препятствие.
     * @return успешность.
     */
    public boolean setNeighborObstacle(@NotNull Direction direction, @NotNull BetweenCellObject obstacle) {
        return getNeighborArea(direction).setObstacle(obstacle);
    }

    //endregion
}
