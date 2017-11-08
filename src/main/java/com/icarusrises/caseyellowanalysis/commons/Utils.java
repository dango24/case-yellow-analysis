package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.AnalyzerException;
import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

import static java.util.Objects.isNull;

public interface Utils {

    static File getImgFromResources(String path) throws IOException {
        InputStream resourceAsStream = Utils.class.getResourceAsStream(path);
        BufferedImage image = ImageIO.read(resourceAsStream);
        File tmpFile = File.createTempFile("test_", new File(path).getName());
        ImageIO.write(image, "PNG", tmpFile);

        return tmpFile;
    }

    static MultipartFile retrieveSinglePart(MultipartRequest request) {
        if (isNull(request.getFileMap())) {
            throw new AnalyzerException("Error, request not contain image file");
        } else if (request.getFileMap().size() > 1) {
            throw new AnalyzerException("Not support more then one img file for request");
        }

        return request.getFileMap().values().iterator().next();
    }

    static File writeToFile(String identifier, MultipartFile file) {

        try {
            byte[] bytes = file.getBytes();
            File tempFileLocation = new File(System.getProperty("java.io.tmpdir"), identifier );
            Files.write(tempFileLocation.toPath(), bytes);

            return tempFileLocation;

        } catch (IOException e) {
            throw new IORuntimeException("Failed to write file from request to tmp dir, " + e.getMessage(), e);
        }
    }
}
