package game.ui.cell;

import game.model.field.core.Direction;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BulletWidget extends FieldItemWidget {

    private final Direction direction;

    public BulletWidget(Direction direction) {
        this.direction = direction;
        this.setSize(getDimension());
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "bullet.png"));
            image = ImageUtils.resizeImage(image, 40, 40);

            double theta = switch (direction) {
                case NORTH -> 0;
                case WEST -> -Math.PI / 2;
                case SOUTH -> Math.PI;
                case EAST -> Math.PI / 2;
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
        return new Dimension(40, 40);
    }
}
