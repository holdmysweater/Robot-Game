package game.ui.cell;

import game.model.field.core.ExitCell;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Виджет ячейки выхода.
 *
 * @see ExitCell
 */
public class ExitWidget extends CellItemWidget {

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "exit.png"));
            image = ImageUtils.resizeImage(image, 120, 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public CellWidget.Layer getLayer() {
        return CellWidget.Layer.TOP;
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(120, 120);
    }
}
