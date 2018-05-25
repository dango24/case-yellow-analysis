package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.Image;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public interface ImageUtils {

    String IMAGE_RESOLUTION_SCHEMA = "%s_%s";

    Logger logger = Logger.getLogger(ImageUtils.class);

    static Map<String, Object> createData(String identifier, GoogleVisionRequest googleVisionRequest) {
        Map<String,Object> data = new HashMap<>();
        data.put("identifier", identifier);
        data.put("file", googleVisionRequest);

        return data;
    }

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

    static String getImageResolution(VisionRequest visionRequest) {
        File imgFile = null;

        try {
            imgFile = convertBase64ToImage(visionRequest.getImage());
            return getImageResolution(imgFile);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to get image resolution: %s", e.getMessage());
            logger.error(errorMessage, e);
            throw new AnalyzeException(errorMessage);

        } finally {
            com.icarusrises.caseyellowanalysis.commons.FileUtils.deleteFile(imgFile);
        }
    }

    static String getImageResolution(File imgFile) throws IOException {
        BufferedImage img = ImageIO.read(imgFile);
        int width = img.getWidth();
        int height = img.getHeight();

        return String.format(IMAGE_RESOLUTION_SCHEMA, width, height);
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
            logger.error(e);
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
            throw new AnalyzeException(errorMessage, e);
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

    static List<ImageClassification> parseInceptionCommandOutput(String output) {
        return Stream.of(output.split("\n"))
                     .map(ImageUtils::generateImageClassification)
                     .collect(toList());
    }

    static ImageClassification generateImageClassification(String imageClassificationStr) {
        try {
            int confidenceIndex = imageClassificationStr.lastIndexOf(" ");
            String label = imageClassificationStr.substring(0, confidenceIndex);
            double confidence = Double.valueOf(imageClassificationStr.substring(confidenceIndex + 1));

            return new ImageClassification(label, confidence);

        } catch (Exception e) {
            String errorMessage = String.format("Failed to generate image classification from: %s", imageClassificationStr);
            logger.error(errorMessage);

            throw new AnalyzeException(errorMessage);
        }
    }
}
