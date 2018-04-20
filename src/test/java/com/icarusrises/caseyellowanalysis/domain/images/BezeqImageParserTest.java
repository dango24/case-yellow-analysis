package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.parsers.BezeqImageParser;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.icarusrises.caseyellowanalysis.commons.ImageUtils.getImgFromResources;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
@Ignore
public class BezeqImageParserTest {

    private static final String BEZEQ_IMG_LOCATION_0 = "/images/bezeq_0_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_1 = "/images/bezeq_1_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_2 = "/images/bezeq_2_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_3 = "/images/bezeq_3_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_4 = "/images/bezeq_4_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_5 = "/images/bezeq_5_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_6 = "/images/bezeq_6_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_7 = "/images/bezeq_7_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_8 = "/images/bezeq_8_screenshot.PNG";
    private static final String BEZEQ_IMG_LOCATION_9 = "/images/bezeq_9_screenshot.PNG";

    private BezeqImageParser bezeqImageParser;

    @Autowired
    public void setBezeqImageParser(BezeqImageParser bezeqImageParser) {
        this.bezeqImageParser = bezeqImageParser;
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithNull() throws Exception {
        bezeqImageParser.parseSpeedTest(null);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithEmptyMap() throws Exception {
        bezeqImageParser.parseSpeedTest(Collections.EMPTY_MAP);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutFile() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("oren", "efes");
        bezeqImageParser.parseSpeedTest(map);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutExistFile() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("file", "oren_efes");
        bezeqImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest() throws Exception {
        Map<String, Object> map = new HashMap<>();

        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_0).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(28.5), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_1).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(37.5), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_2).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(35.6), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_3).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(41.0), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest4() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_4).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(5.1), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest5() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_5).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(37.0), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest6() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_6).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(21.6), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest7() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_7).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(46.6), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test (expected = SpeedTestParserException.class)
    public void parseSpeedTest8() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_8).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(1.0), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest9() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(BEZEQ_IMG_LOCATION_9).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(0), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

}