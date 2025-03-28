package game.ui.cell;

import game.model.field.cell_objects.Battery;
import game.ui.utils.GameWidgetsUtils;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Виджет батареи.
 * @see Battery
 */
public class BatteryWidget extends CellItemWidget {

    /**
     * Источник питания.
     */
    protected final Battery battery;

    /**
     * Конструктор.
     * @param battery источник питания.
     */
    public BatteryWidget(Battery battery) {
        this.battery = battery;
        setToolTipText("Заряд: " + battery.getCharge() + "/" + battery.getMaxCharge());
    }

    @Override
    public CellWidget.Layer getLayer() {
        return CellWidget.Layer.TOP;
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getImageFile());
            image = powerSupplyImageWithChargeText(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Получить изображение источника питания с наложенным текстом заряда.
     * @param powerSupplyImage изображение источник питания.
     * @return изображение источника питания с наложенным текстом заряда.
     */
    private BufferedImage powerSupplyImageWithChargeText(BufferedImage powerSupplyImage) {
        BufferedImage img = new BufferedImage(powerSupplyImage.getWidth(), 120, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.drawImage(powerSupplyImage, 0, 0, null);

        if (cellItemState == State.DEFAULT) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(powerSupplyChargeTextColor());
            g.drawString(powerSupplyChargeText(), 40, 100);
        }

        return img;
    }

    /**
     * Получить текст заряда источника питания.
     *
     * @return текст заряда источника питания.
     */
    private String powerSupplyChargeText() {
        return battery.getCharge() + "/" + battery.getMaxCharge();
    }

    /**
     * Получить цвет текста заряда источника питания.
     *
     * @return цвет текста заряда источника питания.
     */
    private Color powerSupplyChargeTextColor() {
        return GameWidgetsUtils.chargeTextColor(battery.getCharge(), battery.getMaxCharge());
    }

    /**
     * Получить файл изображения.
     *
     * @return файл изображения.
     */
    protected File getImageFile() {
        File file = null;
        if (cellItemState == State.SMALL) {
            file = new File(ImageUtils.IMAGE_PATH + "BS.png");
        } else if (cellItemState == State.DEFAULT) {
            file = new File(ImageUtils.IMAGE_PATH + "BD.png");
        }
        return file;
    }

    @Override
    protected Dimension getDimension() {
        Dimension dimension = null;

        if(cellItemState == State.SMALL) {
            dimension = new Dimension(36, 66);
        } else if (cellItemState == State.DEFAULT) {
            dimension = new Dimension(120, 120);
        }

        return dimension;
    }
}
