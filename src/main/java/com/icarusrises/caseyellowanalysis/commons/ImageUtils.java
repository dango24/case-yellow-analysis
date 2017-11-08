package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public interface ImageUtils {

    static byte[] createImageBase64Encode(String imgPath) throws IOException {
        File imageFile = new File(imgPath);
        byte[] imageBase64Encode = Base64.getEncoder().encode(FileUtils.readFileToByteArray(imageFile));

        return imageBase64Encode;
    }

    static File convertToNegative(File imgFile)throws IOException {
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
            imgFile = File.createTempFile("Oren_efes", "Output.PNG");
            ImageIO.write(img, "PNG", imgFile);

            return imgFile;

        }catch(IOException e){
            throw new AnalyzerException("Oren Mega Efes");
        }
    }
}
