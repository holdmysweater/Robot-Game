package game.model.field.cell_objects;

public abstract class SelfActivatingCellObject extends LowProfileCellObject {

    //region ДЕЙСТВИЯ

    public abstract void execute(SmallCellObject object);

    //endregion

}
