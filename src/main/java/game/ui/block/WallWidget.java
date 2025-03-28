package game.ui.block;

import game.model.Orientation;
import game.ui.utils.ImageUtils;

import java.io.File;

/**
 * Виджет стены.
 * @see game.model.field.between_cells_objects.WallSegment
 */
public class WallWidget extends BlockWidget {

    /**
     * Конструктор.
     * @param orientation ориентация.
     */
    public WallWidget(Orientation orientation) {
        super(orientation);
    }

    @Override
    protected File getImageFile() {
        return (orientation == Orientation.VERTICAL) ? new File(ImageUtils.IMAGE_PATH + "WV.png") : new File(ImageUtils.IMAGE_PATH + "WH.png");
    }
}
