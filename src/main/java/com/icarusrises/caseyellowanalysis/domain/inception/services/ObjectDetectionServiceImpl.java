package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icarusrises.caseyellowanalysis.commons.FileUtils;
import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.commons.WordUtils;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ObjectDetectionData;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.convertBase64ToImage;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class ObjectDetectionServiceImpl implements ObjectDetectionService {

    private ObjectMapper objectMapper;
    private ObjectDetectionExecutor objectDetectionExecutor;

    @Autowired
    public ObjectDetectionServiceImpl(ObjectDetectionExecutor objectDetectionExecutor) {
        this.objectDetectionExecutor = objectDetectionExecutor;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public DescriptionMatch detectedObject(String identifier, VisionRequest visionRequest) {
        File imageFile = null;

        try {
            log.info(String.format("Start detect object for identifier: %s", identifier));
            imageFile = convertBase64ToImage(visionRequest.getImage());
            String output = objectDetectionExecutor.executeDetectedObject(imageFile.getAbsolutePath());
            List<Point> objectDetectionPoints = generateAllPointsFromObjectDetectionOutput(output);
            log.info(String.format("All object detection points: %s", objectDetectionPoints));
            Point centerImagePoint = getCenterPointFromImage(imageFile);

            List<Point> sortedObjectDetectionData =
                    objectDetectionPoints.stream()
                                         .sorted(comparing(point -> WordUtils.euclideanDistance(point, centerImagePoint)))
                                         .collect(toList());

            log.info(String.format("Found closest point to center location: %s", sortedObjectDetectionData.get(0)));

            return new DescriptionMatch("SUCCESS", sortedObjectDetectionData.get(0));

        } catch (IOException | AnalyzeException e) {
            log.error(String.format("Failed to detected object: %s", e.getMessage()), e);
            return DescriptionMatch.notFound();

        } finally {
            FileUtils.deleteFile(imageFile);
        }
    }

    private List<Point> generateAllPointsFromObjectDetectionOutput(String output) {

        return Stream.of(output.split("\n"))
                     .map(this::generateObjectDetectionDataFromRawData)
                     .collect(toList());
    }

    private Point generateObjectDetectionDataFromRawData(String rawData) {
        try {
            ObjectDetectionData objectDetectionData = objectMapper.readValue(rawData, ObjectDetectionData.class);
            log.info(objectDetectionData.toString());
            Point center = new Point(objectDetectionData.getCentroid().get(0), objectDetectionData.getCentroid().get(1));

            return center;

        } catch (IOException e) {
            String errorMessage = String.format("Failed to generate object detection data from raw data, %s", e.getMessage());
            log.info(errorMessage, e);
            throw new AnalyzeException(errorMessage, e);
        }
    }

    private Point getCenterPointFromImage(File imageFile) throws IOException {
        String imageResolution = ImageUtils.getImageResolution(imageFile);
        String[] resolution = imageResolution.split("_");

        int width = Integer.valueOf(resolution[0]) /2;
        int height = Integer.valueOf(resolution[1]) /2;

        return new Point(width, height);
    }
}
