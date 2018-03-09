package com.icarusrises.caseyellowanalysis.commons;

import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface UploadFileUtils {

    Logger logger = Logger.getLogger(UploadFileUtils.class);

    String DELIMITER = "-";
    String FILE_EXTENSION = ".png";
    String INCEPTION_DIR = "inception-snapshots";

    static String generateInceptionSnapshotPath(String label, double confidence, String snapshotHash, String inceptionModel) {

        return new StringBuilder().append(INCEPTION_DIR)
                                  .append("/")
                                  .append(snapshotHash)
                                  .append(DELIMITER)
                                  .append(inceptionModel)
                                  .append(DELIMITER)
                                  .append(confidence)
                                  .append(DELIMITER)
                                  .append(label.trim().replaceAll(" ", DELIMITER))
                                  .append(FILE_EXTENSION)
                                  .toString();
    }

    static void uploadObject(URL url, String fileToUploadPath) {
        try {
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");

            uploadObject(connection, new File(fileToUploadPath));

        } catch (IOException e) {
            logger.error("Failed to upload file, " + e.getMessage(), e);
            throw new RequestFailureException("Failed to upload file, " + e.getMessage(), e);
        }
    }

    static void uploadObject(HttpURLConnection connection, File fileToUpload) {
        int responseCode;

        try (DataOutputStream dataStream = new DataOutputStream(connection.getOutputStream())) {
            dataStream.write(IOUtils.toByteArray(new FileInputStream(fileToUpload)));
            responseCode = connection.getResponseCode(); // Invoke request

            if (isRequestSuccessful(responseCode)) {
                logger.info("Upload object succeed, Service returned response code " + responseCode);
            } else {
                logger.error("Failed to upload file, responseCode is " + responseCode);
                throw new RequestFailureException("Failed to upload file, responseCode is " + responseCode);
            }

        } catch (IOException e) {
            logger.error("Failed to upload file, " + e.getMessage(), e);
            throw new RequestFailureException("Failed to upload file, " + e.getMessage(), e);
        }
    }

    static boolean isRequestSuccessful(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }
}
