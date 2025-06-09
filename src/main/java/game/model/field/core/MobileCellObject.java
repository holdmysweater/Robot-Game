package game.model.field.core;

import game.model.field.cell_objects.IMobileFieldObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    //endregion

    //region ПОЗИЦИЯ

    boolean setPosition(@NotNull Map<Cell, CellObjectStatus> newPosition) {
        for (Map.Entry<Cell, CellObjectStatus> entry : newPosition.entrySet()) {
            if (!setPosition(entry.getKey(), entry.getValue())) {
                throw new RuntimeException("Can't set position in map");
            }
        }
        return true;
    }

    boolean setPosition(@NotNull Cell cell, @NotNull CellObjectStatus status) {
        return setPosition(new AbstractMap.SimpleImmutableEntry<>(cell, status));
    }

    boolean setPosition(@NotNull Map.Entry<Cell, CellObjectStatus> newEntry) {
        if (this.position == null) {
            this.position = new HashMap<>();
        }

        Cell cell = newEntry.getKey();
        CellObjectStatus status = newEntry.getValue();

        // Case 1: Set to IDLE -- must clear all and only have this entry
        if (status == CellObjectStatus.IDLE) {
            this.position.clear();
            this.position.put(cell, status);
            return true;
        }

        // If current is IDLE, we can't set to DEPARTING/ARRIVING directly
        if (this.position.containsValue(CellObjectStatus.IDLE)) {
            // Can't depart/arrive if only idle
            return false;
        }

        // If we're in the middle of a move (have one DEPARTING or ARRIVING)
        if (this.position.size() == 1) {
            Map.Entry<Cell, CellObjectStatus> existingEntry = this.position.entrySet().iterator().next();
            Cell existingCell = existingEntry.getKey();
            CellObjectStatus existingStatus = existingEntry.getValue();

            boolean validTransition =
                    (existingStatus == CellObjectStatus.DEPARTING && status == CellObjectStatus.ARRIVING) ||
                            (existingStatus == CellObjectStatus.ARRIVING && status == CellObjectStatus.DEPARTING);

            boolean areNeighbors = existingCell.isNeighbor(cell);

            if (validTransition && areNeighbors) {
                this.position.put(cell, status);
                return true;
            } else {
                return false; // Invalid transition
            }
        }

        // If we already have two entries, don't allow more
        if (this.position.size() >= 2) {
            return false;
        }

        return false;
    }

    boolean unsetPosition(Cell cell) {
        if (this.position == null) {
            return false;
        }
        return this.position.remove(cell) != null;
    }

    public Cell getIdleCellPosition() {
        if (this.position != null && this.position.size() == 1) {
            Map.Entry<Cell, CellObjectStatus> entry = this.position.entrySet().iterator().next();
            if (entry.getValue() == CellObjectStatus.IDLE) {
                return entry.getKey();
            }
        }
        return null;
    }

    //endregion
}
