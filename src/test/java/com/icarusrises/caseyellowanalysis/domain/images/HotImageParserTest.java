package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.commons.Utils;
import com.icarusrises.caseyellowanalysis.domain.images.services.HotImageParser;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class HotImageParserTest {

    private static final String HOT_IMG_LOCATION = "/images/hot_0_screenshot.PNG";

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
        Map<String, String> map = new HashMap<>();
        map.put("oren", "efes");
        hotImageParser.parseSpeedTest(map);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutExistFile() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", "oren_efes");
        hotImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(HOT_IMG_LOCATION).getAbsolutePath());

        assertEquals(String.valueOf(30.56), String.valueOf(hotImageParser.parseSpeedTest(map)));
    }

}