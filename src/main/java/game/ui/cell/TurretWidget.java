package game.ui.cell;

import game.model.field.core.Direction;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TurretWidget extends CellItemWidget {

    private final Direction direction;

    public TurretWidget(Direction direction) {
        this.direction = direction;
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "turret.png"));
            image = ImageUtils.resizeImage(image, 120, 120);

            double theta = switch (direction) {
                case EAST -> 0;
                case NORTH -> -Math.PI / 2;
                case WEST -> Math.PI;
                case SOUTH -> Math.PI / 2;
                default -> 0;
            };

            if (theta != 0) {
                int w = image.getWidth();
                int h = image.getHeight();
                BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = rotated.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.translate(w / 2.0, h / 2.0);
                g2d.rotate(theta);
                g2d.translate(-w / 2.0, -h / 2.0);
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();
                image = rotated;
            }
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
