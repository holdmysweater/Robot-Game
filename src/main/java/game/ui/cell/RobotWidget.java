package game.ui.cell;

import game.model.Direction;
import game.model.field.cell_objects.Robot;
import game.ui.cell.CellWidget.Layer;
import game.ui.utils.GameWidgetsUtils;
import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Виджет робота.
 * @see Robot
 */
public class RobotWidget extends CellItemWidget {

    /**
     * Робот.
     */
    private final Robot robot;

    /**
     * Цвет.
     */
    private final Color color;

    /**
     * Конструтор.
     * @param robot робот.
     * @param color цвет.
     */
    public RobotWidget(Robot robot, Color color) {
        super();
        this.robot = robot;
        this.color = color;
        setFocusable(true);
        addKeyListener(new KeyController());
    }

    @Override
    protected BufferedImage getImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getImageFile());
            image = ImageUtils.resizeImage(image, 60, 96);
            image = robotImageWithChargeText(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public Layer getLayer() {
        return CellWidget.Layer.BOTTOM;
    }

    /**
     * Сделать вижет актиынм
     * @param state состояние активности.
     */
    public void setActive(boolean state) {
        setFocusable(state);
        requestFocus();
        repaint();
    }

    @Override
    protected Dimension getDimension() {
        return new Dimension(60, 120);
    }

    /**
     * Получить цвет робота {@link RobotWidget#color}.
     * @return цвет робота.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Получить изобаржение с текстом заряда.
     * @param robotImage изображение робота.
     * @return изобаржение с текстом заряда.
     */
    private BufferedImage robotImageWithChargeText(BufferedImage robotImage) {
        BufferedImage img = new BufferedImage(robotImage.getWidth(), 120, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.drawImage(robotImage, 0, 0, null);

        if(cellItemState == State.DEFAULT) {
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(robotChargeTextColor());
            g.drawString(robotChargeText(), 5, 112);
        }

        return img;
    }

    /**
     * Получить текст заряда робота.
     * @return текст заряда робота.
     */
    private String robotChargeText() {
        return robot.getCharge() + "/" + robot.getMaxCharge();
    }

    /**
     * Получить цвет текста заряда.
     * @return цвет текста заряда.
     */
    private Color robotChargeTextColor() {
        return GameWidgetsUtils.chargeTextColor(robot.getCharge(), robot.getMaxCharge());
    }

    /**
     * Получить файл изображения робота.
     * @return файл изображения робота.
     */
    private File getImageFile() {
        File file = null;

        if (color == Color.BLUE) {
            file = robot.isUnfrozen() ? new File(ImageUtils.IMAGE_PATH + "RBBA.png") : new File(ImageUtils.IMAGE_PATH + "RBB.png");
        }

        return file;
    }

    /**
     * Внутренний класс-обработчик событий. Придает специфическое поведение виджету.
     */
    private class KeyController implements KeyListener {

        @Override
        public void keyTyped(KeyEvent arg0) {
        }

        @Override
        public void keyPressed(KeyEvent ke) {
            int keyCode = ke.getKeyCode();

            moveAction(keyCode);
            changeBatteryAction(keyCode);

            repaint();
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        private void changeBatteryAction(int keyCode) {
            if(keyCode == KeyEvent.VK_G) {
                robot.changeBattery();
            }
        }

        private void moveAction(int keyCode){
            Direction direction = directionByKeyCode(keyCode);
            System.out.println(color + " go to " + direction);
            if(direction != null && robot.isUnfrozen()) {
                robot.move(direction);
            }
        }

        private Direction directionByKeyCode(int keyCode) {
            Direction direction = null;
            switch (keyCode) {
                case KeyEvent.VK_W:
                    direction = Direction.NORTH;
                    break;
                case KeyEvent.VK_S:
                    direction = Direction.SOUTH;
                    break;
                case KeyEvent.VK_A:
                    direction = Direction.WEST;
                    break;
                case KeyEvent.VK_D:
                    direction = Direction.EAST;
                    break;
            }
            return direction;
        }
    }
}
