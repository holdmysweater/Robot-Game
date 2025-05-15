package game.ui.cell;

import game.ui.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HoleWidget extends CellItemWidget {

    private BufferedImage staticImage;
    private List<BufferedImage> gifFrames;
    private int currentFrame = 0;
    private boolean showGif = true;
    private Timer frameTimer;

    public HoleWidget() {
        try {
            // Load the static image
            staticImage = ImageIO.read(new File(ImageUtils.IMAGE_PATH + "hole.png"));
            staticImage = ImageUtils.resizeImage(staticImage, 120, 120);

            gifFrames = extractGifFrames(new File(ImageUtils.IMAGE_PATH + "digging.gif"));

            frameTimer = new Timer(20, e -> {
                if (showGif && gifFrames != null && !gifFrames.isEmpty()) {
                    if (currentFrame < gifFrames.size() - 1) {
                        currentFrame++;
                    } else {
                        frameTimer.stop();
                    }
                    repaint();
                }
            });
            frameTimer.start();

            new Timer(2500, e -> {
                showGif = false;
                repaint();
                ((Timer) e.getSource()).stop();
                frameTimer.stop();
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<BufferedImage> extractGifFrames(File gifFile) throws IOException {
        List<BufferedImage> frames = new ArrayList<>();
        ImageInputStream inputStream = ImageIO.createImageInputStream(gifFile);
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(inputStream);

        int frameCount = reader.getNumImages(true);
        for (int i = 0; i < frameCount; i++) {
            BufferedImage frame = reader.read(i);
            frames.add(ImageUtils.resizeImage(frame, 120, 120));
        }

        reader.dispose();
        inputStream.close();
        return frames;
    }

    @Override
    protected BufferedImage getImage() {
        if (showGif && gifFrames != null && !gifFrames.isEmpty()) {
            return gifFrames.get(currentFrame);
        } else {
            return staticImage;
        }
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