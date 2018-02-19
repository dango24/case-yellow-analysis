package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Utils {

    static File getImgFromResources(String path) throws IOException {
        InputStream resourceAsStream = Utils.class.getResourceAsStream(path);
        BufferedImage image = ImageIO.read(resourceAsStream);
        File tmpFile = File.createTempFile("test_", new File(path).getName());
        ImageIO.write(image, "PNG", tmpFile);

        return tmpFile;
    }

    static File createTmpPNGFile() throws IORuntimeException {
        try {
            return File.createTempFile("tmpImage", ".PNG");
        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }
}
