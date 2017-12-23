package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.commons.Utils;
import com.icarusrises.caseyellowanalysis.domain.images.services.HotImageParser;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class HotImageParserTest {

    private static final String HOT_IMG_LOCATION_0 = "/images/hot_0_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_1 = "/images/hot_1_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_2 = "/images/hot_2_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_3 = "/images/hot_3_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_4 = "/images/hot_4_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_5 = "/images/hot_5_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_6 = "/images/hot_6_screenshot.PNG";

    private HotImageParser hotImageParser;

    @Autowired
    public void setHotImageParser(HotImageParser hotImageParser) {
        this.hotImageParser = hotImageParser;
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithNull() throws Exception {
        hotImageParser.parseSpeedTest(null);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithEmptyMap() throws Exception {
        hotImageParser.parseSpeedTest(Collections.EMPTY_MAP);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutFile() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("oren", "efes");
        hotImageParser.parseSpeedTest(map);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutExistFile() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("file", "oren_efes");
        hotImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_0).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(30.56), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_1).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(34.63), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_2).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(38.98), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_3).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(41.81), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest4() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_4).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(35.44), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest5() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_5).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(37.04), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest6() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(Utils.getImgFromResources(HOT_IMG_LOCATION_6).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(38.92), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

}