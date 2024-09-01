package main.java.org.flinkjo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {
    public static BufferedImage decodeBase64ToBufferedImage(String base64String) {
        BufferedImage bufferedImage = null;
        try {
            base64String = base64String.trim();
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            bufferedImage = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
