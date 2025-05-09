package game.model.field.cell_objects;

public abstract class InteractiveCellObject extends StationaryCellObject {

    //region ДЕЙСТВИЯ

    public abstract void execute(NonStationaryCellObject object);

    //endregion

}
