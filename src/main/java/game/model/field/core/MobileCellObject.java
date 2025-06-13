package game.model.field.core;

import game.model.events.MobileObjectListener;
import game.model.field.cell_objects.IMobileFieldObject;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public abstract class MobileCellObject extends CellObject<Map<Cell, CellObjectStatus>> implements IMobileFieldObject {

    //region ПЕРЕМЕЩЕНИЕ

    /**
     * Способность перемещаться.
     */
    protected final MobilityOnFieldProperty _mobility = new MobilityOnFieldProperty(this);

    @Override
    public boolean stopMoving() {
        return _mobility.stopMoving();
    }

    @Override
    public int getMovementSpeed() {
        return _mobility.getMovementSpeed();
    }

    @Override
    public Direction getMovementDirection() {
        return _mobility.getMovementDirection();
    }

    @Override
    public void addMobileObjectActionListener(MobileObjectListener listener) {
        _mobility.addMobileObjectActionListener(listener);
    }

    @Override
    public void removeMobileObjectActionListener(MobileObjectListener listener) {
        _mobility.removeMobileObjectActionListener(listener);
    }


    //endregion

    //region ПОЗИЦИЯ

    /**
     * Устанавливает новую позицию объекта на основе переданной карты ячеек и их статусов.
     * Для каждой пары (ячейка, статус) вызывает setPosition(Cell, CellObjectStatus).
     * Если хотя бы для одной пары установка не удалась, выбрасывает исключение.
     *
     * @param newPosition карта, где ключ — ячейка, а значение — статус объекта в этой ячейке
     * @return true, если все позиции были успешно установлены
     * @throws RuntimeException если установка хотя бы одной позиции не удалась
     */
    @Override
    boolean setPosition(@NotNull Map<Cell, CellObjectStatus> newPosition) {
        for (Map.Entry<Cell, CellObjectStatus> entry : newPosition.entrySet()) {
            if (!setPosition(entry.getKey(), entry.getValue())) {
                throw new RuntimeException("Не удалось установить позицию в карте");
            }
        }
        return true;
    }

    /**
     * Устанавливает позицию объекта для одной ячейки и статуса.
     *
     * @param cell   ячейка, для которой обновляется статус
     * @param status статус, который нужно установить для ячейки
     * @return true, если позиция была успешно установлена; false в противном случае
     */
    boolean setPosition(@NotNull Cell cell, @NotNull CellObjectStatus status) {
        return setPosition(new AbstractMap.SimpleImmutableEntry<>(cell, status));
    }

    /**
     * Устанавливает позицию для одной ячейки и статуса с учетом новых правил перехода состояний:
     * <ul>
     *   <li>Если статус IDLE — очищает карту позиций и добавляет только эту запись.</li>
     *   <li>Если текущая позиция содержит только IDLE и новый статус DEPARTING для той же ячейки — заменяет IDLE на DEPARTING.</li>
     *   <li>Если текущая позиция содержит только DEPARTING и приходит ARRIVING для соседней ячейки — добавляет ARRIVING.</li>
     *   <li>Если в позиции DEPARTING+ARRIVING и приходит IDLE для ячейки ARRIVING — карта сбрасывается и остаётся только IDLE.</li>
     *   <li>Во всех остальных случаях возвращает false.</li>
     * </ul>
     *
     * @param newEntry пара (ячейка, статус) для установки
     * @return true, если установка успешна; false в остальных случаях
     */
    boolean setPosition(@NotNull Map.Entry<Cell, CellObjectStatus> newEntry) {
        if (this.position == null) {
            this.position = new HashMap<>();
        }

        Cell cell = newEntry.getKey();
        CellObjectStatus status = newEntry.getValue();

        // Переход в состояние "покой" — очищаем карту и добавляем только эту запись
        if (status == CellObjectStatus.IDLE) {
            // Если было ARRIVING и ячейка совпадает — сбросить в IDLE
            if (this.position.size() == 1 && this.position.containsValue(CellObjectStatus.ARRIVING)) {
                Map.Entry<Cell, CellObjectStatus> entry = this.position.entrySet().iterator().next();
                if (entry.getKey().equals(cell)) {
                    this.position.clear();
                    this.position.put(cell, status);
                    if (getApproximatingRectangle() == null) {
                        this.createApproximatingRectangle(cell);
                    }
                    return true;
                }
            }
            // Если было DEPARTING+ARRIVING и приходит IDLE для ячейки ARRIVING — сбросить в IDLE
            if (this.position.size() == 2) {
                for (Map.Entry<Cell, CellObjectStatus> entry : this.position.entrySet()) {
                    if (entry.getKey().equals(cell) && entry.getValue() == CellObjectStatus.ARRIVING) {
                        this.position.clear();
                        this.position.put(cell, status);
                        if (getApproximatingRectangle() == null) {
                            this.createApproximatingRectangle(cell);
                        }
                        return true;
                    }
                }
            }
            // Обычный переход в IDLE
            this.position.clear();
            this.position.put(cell, status);
            if (getApproximatingRectangle() == null) {
                this.createApproximatingRectangle(cell);
            }
            return true;
        }

        // Если текущий статус — только IDLE и новый DEPARTING для той же ячейки — заменить
        if (this.position.size() == 1 && this.position.containsValue(CellObjectStatus.IDLE)) {
            Map.Entry<Cell, CellObjectStatus> entry = this.position.entrySet().iterator().next();
            if (status == CellObjectStatus.DEPARTING && entry.getKey().equals(cell)) {
                this.position.clear();
                this.position.put(cell, status);
                return true;
            } else {
                return false;
            }
        }

        // Если текущий статус — только DEPARTING и приходит ARRIVING для соседа — добавить в карту
        if (this.position.size() == 1 && this.position.containsValue(CellObjectStatus.DEPARTING)) {
            Map.Entry<Cell, CellObjectStatus> departingEntry = this.position.entrySet().iterator().next();
            Cell departingCell = departingEntry.getKey();
            if (status == CellObjectStatus.ARRIVING && !departingCell.equals(cell) && departingCell.isNeighbor(cell)) {
                this.position.put(cell, status);
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * Удаляет запись позиции для указанной ячейки.
     *
     * @param cell ячейка, для которой требуется удалить запись
     * @return true, если запись была удалена; false, если такой записи не было
     */
    boolean unsetPosition(Cell cell) {
        if (this.position == null) {
            return false;
        }
        return this.position.remove(cell) != null;
    }

    /**
     * Возвращает ячейку, в которой объект находится в состоянии покоя (IDLE), если такая есть.
     *
     * @return ячейка с состоянием IDLE или null, если не найдено
     */
    public Cell getIdleCellPosition() {
        if (this.position != null && this.position.size() == 1) {
            Map.Entry<Cell, CellObjectStatus> entry = this.position.entrySet().iterator().next();
            if (entry.getValue() == CellObjectStatus.IDLE) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Возвращает ячейку, в которую объект прибывает (ARRIVING), если такая есть.
     *
     * @return ячейка с состоянием ARRIVING или null, если не найдено
     */
    public Cell getArrivalCellPosition() {
        if (this.position != null) {
            for (Map.Entry<Cell, CellObjectStatus> entry : this.position.entrySet()) {
                if (entry.getValue() == CellObjectStatus.ARRIVING) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /**
     * Возвращает ячейку, из которой объект убывает (DEPARTING), если такая есть.
     *
     * @return ячейка с состоянием DEPARTING или null, если не найдено
     */
    public Cell getDepartCellPosition() {
        if (this.position != null) {
            for (Map.Entry<Cell, CellObjectStatus> entry : this.position.entrySet()) {
                if (entry.getValue() == CellObjectStatus.DEPARTING) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    //endregion

    //region АППРОКСИМИРУЮЩИЙ ПРЯМОУГОЛЬНИК

    /**
     * Создать аппроксимирующий прямоугольник по центру указанной ячейки.
     *
     * @param cell ячейка, в которой находится объект.
     */
    private void createApproximatingRectangle(Cell cell) {
        ApproximatingRectangle cellApproximationRectangle = cell.getApproximatingRectangle();
        this.setApproximatingRectangle(cellApproximationRectangle.getCenter());
    }

    //endregion
}
