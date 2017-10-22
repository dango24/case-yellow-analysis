package com.icarusrises.caseyellowanalysis.domain;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.ImageAnalyzerService;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.icarusrises.caseyellowanalysis.commons.Utils.getImgFromResources;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class ImageAnalyzerServiceImplTest {

    private static final String HOT_IMG_LOCATION = "/images/hot_0_screenshot.png";
    private static final String BEZEQ_IMG_LOCATION = "/images/bezeq_0_screenshot.png";
    private static final String OOKLA_IMG_LOCATION = "/images/ookla_0_screenshot.png";

    private File imageFile;
    private ImageAnalyzerService imageAnalyzerService;

    @Autowired
    public void setImageAnalyzerService(ImageAnalyzerService imageAnalyzerService) {
        this.imageAnalyzerService = imageAnalyzerService;
    }

    @Test
    public void analyzeHotImage() throws Exception {
        File imgFile = getImgFromResources(HOT_IMG_LOCATION);
        OcrResponse ocrResponse = imageAnalyzerService.analyzeImage(imgFile);
        assertEquals(70, ocrResponse.getTextAnnotations().size());

        Map<String, Long> words =
                ocrResponse.getTextAnnotations()
                        .stream()
                        .map(WordData::getDescription)
                        .collect(groupingBy(Function.identity(), counting()));

        assertEquals(2, (long)words.get("Mbps"));
        assertNotNull(words.get("30.56"));
    }

    @Test
    public void analyzeBezeqImage() throws Exception {
        File imgFile = getImgFromResources(BEZEQ_IMG_LOCATION);
        OcrResponse ocrResponse = imageAnalyzerService.analyzeImage(imgFile);
        assertEquals(125, ocrResponse.getTextAnnotations().size());

        Set<String> words =
            ocrResponse.getTextAnnotations()
                       .stream()
                       .map(WordData::getDescription)
                       .collect(toSet());

        assertTrue(words.contains("שוב"));
        assertTrue(words.contains("בדוק"));
        assertTrue(words.contains("28.5"));
        assertTrue(words.contains("Mb/s"));
    }

    @Test
    public void analyzeOoklaImage() throws Exception {
        File imgFile = getImgFromResources(OOKLA_IMG_LOCATION);
        OcrResponse ocrResponse = imageAnalyzerService.analyzeImage(imgFile);
        assertEquals(163, ocrResponse.getTextAnnotations().size());

        Set<String> words =
                ocrResponse.getTextAnnotations()
                        .stream()
                        .map(WordData::getDescription)
                        .collect(toSet());

        assertTrue(words.contains("TEST"));
        assertTrue(words.contains("AGAIN"));
        assertTrue(words.contains("31.23"));
        int testIndex = ocrResponse.getTextAnnotations().indexOf(new WordData("TEST"));
        assertEquals("AGAIN", ocrResponse.getTextAnnotations().get(testIndex +1).getDescription());
    }

}