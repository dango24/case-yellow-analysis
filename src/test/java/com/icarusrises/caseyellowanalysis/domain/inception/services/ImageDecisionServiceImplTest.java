package com.icarusrises.caseyellowanalysis.domain.inception.services;

import com.icarusrises.caseyellowanalysis.commons.ImageUtils;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassification;
import com.icarusrises.caseyellowanalysis.domain.inception.model.ImageClassificationStatus;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ImageDecisionServiceImplTest {


    @Test
    public void generateDecisionHotUnready() throws Exception {

        String inceptions = "hot unready 0.981553\n" +
                "hot start 0.012136\n" +
                "hot end 0.00315987\n" +
                "fast end 0.0011577\n" +
                "speedof end 0.000833336";

        List<ImageClassification> imageClassifications = ImageUtils.parseInceptionCommandOutput(inceptions);
        ImageDecisionServiceImpl imageDecisionService = new ImageDecisionServiceImpl();

        assertEquals(ImageClassificationStatus.UNREADY, imageDecisionService.generateDecision(imageClassifications, "hot").getStatus());
    }

    @Test
    public void generateDecisionHotStart() throws Exception {

        String inceptions = "hot start 0.991494\n" +
                "hot unready 0.00420638\n" +
                "hot end 0.00268698\n" +
                "fast end 0.000638887\n" +
                "atnt end 0.000285313";

        List<ImageClassification> imageClassifications = ImageUtils.parseInceptionCommandOutput(inceptions);
        ImageDecisionServiceImpl imageDecisionService = new ImageDecisionServiceImpl();

        assertEquals(ImageClassificationStatus.START, imageDecisionService.generateDecision(imageClassifications, "hot").getStatus());
    }

    @Test
    public void generateDecisionHotMiddle() throws Exception {

        String inceptions = "hot end 0.00268698\n" +
                "hot unready 0.00420638\n" +
                "hot middle 0.991494\n" +
                "fast end 0.000638887\n" +
                "atnt end 0.000285313";

        List<ImageClassification> imageClassifications = ImageUtils.parseInceptionCommandOutput(inceptions);
        ImageDecisionServiceImpl imageDecisionService = new ImageDecisionServiceImpl();

        assertEquals(ImageClassificationStatus.MIDDLE, imageDecisionService.generateDecision(imageClassifications, "hot").getStatus());
    }

}