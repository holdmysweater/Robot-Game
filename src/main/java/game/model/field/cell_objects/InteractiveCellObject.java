package game.model.field.cell_objects;

public abstract class InteractiveCellObject extends LowProfileCellObject {

    //region ДЕЙСТВИЯ

    public abstract void execute(SmallCellObject object);

    //endregion

}
