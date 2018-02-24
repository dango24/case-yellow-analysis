package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.Image;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import static java.lang.String.format;

public interface ImageUtils {

    Logger logger = Logger.getLogger(ImageUtils.class);

    static byte[] createImageBase64Encode(String imgPath)  {
        try {
            File imageFile = new File(imgPath);
            byte[] imageBase64Encode = Base64.getEncoder().encode(FileUtils.readFileToByteArray(imageFile));

            return imageBase64Encode;

        } catch (IOException e) {
            throw new IORuntimeException(e.getMessage(), e);
        }
    }

    static File convertToNegative(File imgFile) {
        BufferedImage img = null;

        //read image
        try{
            img = ImageIO.read(imgFile);
        }catch(IOException e){
            System.out.println(e);
        }
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        //convertToNegative to negative
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int p = img.getRGB(x,y);
                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;
                //subtract RGB from 255
                int offset = -1;
                r = 255 - r - offset;
                g = 255 - g - offset;
                b = 255 - b - offset;
                //set new RGB value
                p = (a<<24) | (r<<16) | (g<<8) | b;
                img.setRGB(x, y, p);
            }
        }
        //write image
        try{
            imgFile = File.createTempFile("negative_file", "output.PNG");
            ImageIO.write(img, "PNG", imgFile);

            return imgFile;

        }catch(IOException e){
            String errorMessage = format("Failed to convert to negative: ", e.getMessage());
            logger.error(errorMessage);
            throw new AnalyzerException(errorMessage, e);
        }
    }

    static File convertBase64ToImage(Image image) {
        File tmpFile = Utils.createTmpPNGFile();

        try (FileOutputStream out = new FileOutputStream(tmpFile)) {
            byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(image.getContent());
            out.write(btDataFile);
            out.flush();

        } catch (IOException e) {
            logger.error(format("Failed to convert file to negative, error message: %s", e.getMessage(), e));
        }

        return tmpFile;
    }
}
