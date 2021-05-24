package com.csf.java.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by root on 3/7/15.
 */
public class ImageUtils {

    public static byte[] bufferedImageToBytes(final BufferedImage theImage) {
        byte[] imageInByte = {};
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ImageIO.write(theImage, "png", bytesOut);
            bytesOut.flush();
            imageInByte = bytesOut.toByteArray();
            bytesOut.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return imageInByte;
    }

    public static BufferedImage bytesToBufferedImage(byte[] theBytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(theBytes);
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bImage;
    }
}
