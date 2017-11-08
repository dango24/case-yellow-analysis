package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.commons.Utils;
import com.icarusrises.caseyellowanalysis.domain.images.services.OoklaImageParser;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class OoklaImageParserTest {

    private static final String OOKLA_IMG_LOCATION_0 = "/images/ookla_0_screenshot.PNG";
    private static final String OOKLA_IMG_LOCATION_1 = "/images/ookla_1_screenshot.PNG";
    private static final String OOKLA_IMG_LOCATION_2 = "/images/ookla_2_screenshot.PNG";
    private static final String OOKLA_IMG_LOCATION_3 = "/images/ookla_3_screenshot.PNG";
    private static final String OOKLA_IMG_LOCATION_4 = "/images/ookla_4_screenshot.PNG";
    private static final String OOKLA_IMG_LOCATION_5 = "/images/ookla_5_screenshot.PNG";

    private OoklaImageParser ooklaImageParser;

    @Autowired
    public void setOoklaImageParser(OoklaImageParser ooklaImageParser) {
        this.ooklaImageParser = ooklaImageParser;
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithNull() throws Exception {
        ooklaImageParser.parseSpeedTest(null);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithEmptyMap() throws Exception {
        ooklaImageParser.parseSpeedTest(Collections.EMPTY_MAP);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutFile() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("oren", "efes");
        ooklaImageParser.parseSpeedTest(map);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutExistFile() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", "oren_efes");
        ooklaImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest0() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_0).getAbsolutePath());

        assertEquals(String.valueOf(31.23), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest1() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_1).getAbsolutePath());

        assertEquals(String.valueOf(38.87), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest2() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_2).getAbsolutePath());

        assertEquals(String.valueOf(40.37), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest3() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_3).getAbsolutePath());

        assertEquals(String.valueOf(42.22), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest4() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_4).getAbsolutePath());

        assertEquals(String.valueOf(36.10), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

    @Test
    public void parseSpeedTest5() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION_5).getAbsolutePath());

        assertEquals(String.valueOf(39.17), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

}