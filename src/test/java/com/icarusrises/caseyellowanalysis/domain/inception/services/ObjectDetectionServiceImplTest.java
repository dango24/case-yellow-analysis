package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.commons.FileUtils;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.getImgFromResources;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
@Ignore
public class ObjectDetectionServiceImplTest {

    private static final String HOT_IMG_LOCATION = "/images/hot_start_button_1_screenshot.PNG";

    @Autowired
    private ObjectDetectionService objectDetectionService;

    @Test
    public void detectedObject() throws Exception {
        File imageFile = getImgFromResources(HOT_IMG_LOCATION);
        objectDetectionService.detectedObject("hot", new VisionRequest(imageFile.getAbsolutePath()));

        FileUtils.deleteFile(imageFile);
    }

}