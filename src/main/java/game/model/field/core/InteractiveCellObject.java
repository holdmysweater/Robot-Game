package game.model.field.core;

public abstract class InteractiveCellObject extends StationaryCellObject {

    //region ДЕЙСТВИЯ

    public abstract void execute(NonStationaryCellObject object);

    //endregion

}
