package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.domain.analyzer.image.services.parsers.HotImageParser;
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
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
@Ignore
public class HotImageParserTest {

    private static final String HOT_IMG_LOCATION_0 = "/images/hot_0_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_1 = "/images/hot_1_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_2 = "/images/hot_2_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_3 = "/images/hot_3_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_4 = "/images/hot_4_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_5 = "/images/hot_5_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_6 = "/images/hot_6_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_7 = "/images/hot_7_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_8 = "/images/hot_8_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_9 = "/images/hot_9_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_10 = "/images/hot_10_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_11 = "/images/hot_11_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_12 = "/images/hot_12_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_13 = "/images/hot_13_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_14 = "/images/hot_14_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_15 = "/images/hot_15_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_16 = "/images/hot_16_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_17 = "/images/hot_17_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_18 = "/images/hot_18_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_19 = "/images/hot_19_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_20 = "/images/hot_20_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_21 = "/images/hot_21_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_22 = "/images/hot_22_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_23 = "/images/hot_23_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_24 = "/images/hot_24_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_25 = "/images/hot_25_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_26 = "/images/hot_26_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_27 = "/images/hot_27_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_28 = "/images/hot_28_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_29 = "/images/hot_29_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_30 = "/images/hot_30_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_31 = "/images/hot_31_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_32 = "/images/hot_32_screenshot.PNG";
    private static final String HOT_IMG_LOCATION_7__ = "/images/hot_start_button_1_screenshot.PNG";

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
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_0).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(30.56), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest1() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_1).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(34.63), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest2() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_2).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(38.98), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest3() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_3).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(41.81), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest4() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_4).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(35.44), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest5() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_5).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(37.04), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest6() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_6).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(38.92), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest7() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_7).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(101.21), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest8() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_8).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(101.73), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test (expected = SpeedTestParserException.class)
    public void parseSpeedTest9() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_9).getAbsolutePath());
        map.put("file", googleVisionRequest);

        hotImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest10() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_10).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(62.16), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest11() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_11).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(62.16), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest12() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_12).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(62.16), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest13() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_13).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(62.16), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest14() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_14).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(62.16), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test (expected = SpeedTestParserException.class)
    public void parseSpeedTest15() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_15).getAbsolutePath());
        map.put("file", googleVisionRequest);

        hotImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest16() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_16).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(41.21), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest17() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_17).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(41.21), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest18() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_18).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(22.21), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest19() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_19).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(21.17), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest20() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_20).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(23.02), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest21() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_21).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(18.69), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest22() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_22).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(14.09), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest23() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_23).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(19.01), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest24() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_24).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(19.93), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest25() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_25).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(20.77), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest26() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_26).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(100.84), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest27() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_27).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(102.10), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest28() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_28).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(102.07), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest29() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_29).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(102.10), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest30() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_30).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(102.05), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

    @Test (expected = SpeedTestParserException.class)
    public void parseSpeedTest31() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_31).getAbsolutePath());
        map.put("file", googleVisionRequest);

        hotImageParser.parseSpeedTest(map);
    }

    @Test (expected = SpeedTestParserException.class)
    public void parseSpeedTest32() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_32).getAbsolutePath());
        map.put("file", googleVisionRequest);

        hotImageParser.parseSpeedTest(map);
    }

    @Test
    @Ignore
    public void parseSpeedTest77() throws Exception {
        Map<String, Object> map = new HashMap<>();
        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(getImgFromResources(HOT_IMG_LOCATION_7__).getAbsolutePath());
        map.put("file", googleVisionRequest);

        assertEquals(String.valueOf(38.92), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

}