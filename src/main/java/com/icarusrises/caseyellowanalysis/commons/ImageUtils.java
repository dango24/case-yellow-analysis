package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.lang.String.format;

public interface ImageUtils {

    Logger logger = Logger.getLogger(ImageUtils.class);

    static File getImgFromResources(String path) throws IOException {
        InputStream resourceAsStream = UploadFileUtils.class.getResourceAsStream(path);
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
        File tmpFile = createTmpPNGFile();

        try (FileOutputStream out = new FileOutputStream(tmpFile)) {
            byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(image.getContent());
            out.write(btDataFile);
            out.flush();

        } catch (IOException e) {
            logger.error(format("Failed to convert file to negative, error message: %s", e.getMessage(), e));
        }

        return tmpFile;
    }

    static String convertToMD5(File file)  {

        try (InputStream in = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(IOUtils.toByteArray(in));

            return DatatypeConverter.printHexBinary(md.digest());

        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error(String.format("Failed to convert to MD5, error: %s", e.getMessage(), e));
            return "UNKNOWN";
        }

    }
}
