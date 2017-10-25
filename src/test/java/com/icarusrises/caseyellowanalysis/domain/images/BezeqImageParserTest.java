package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.commons.Utils;
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
public class BezeqImageParserTest {

    private static final String BEZEQ_IMG_LOCATION = "/images/bezeq_0_screenshot.PNG";

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
        Map<String, String> map = new HashMap<>();
        map.put("oren", "efes");
        bezeqImageParser.parseSpeedTest(map);
    }

    @Test(expected = SpeedTestParserException.class)
    public void parseSpeedTestWithoutExistFile() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", "oren_efes");
        bezeqImageParser.parseSpeedTest(map);
    }

    @Test
    public void parseSpeedTest() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(BEZEQ_IMG_LOCATION).getAbsolutePath());

        assertEquals(String.valueOf(28.5), String.valueOf(bezeqImageParser.parseSpeedTest(map)));
    }

}