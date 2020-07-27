package com.idomsoft.identityvalidator.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    public static byte[] createByteArrayFromImage(String fileName, String fileType) {
        InputStream inputStream = ImageUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            BufferedImage bufferedImage  = ImageIO.read(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, fileType, outputStream );
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
