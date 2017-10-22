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
public class OoklaImageParserTest {

    private static final String OOKLA_IMG_LOCATION = "/images/ookla_0_screenshot.png";

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
    public void parseSpeedTest() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("file", Utils.getImgFromResources(OOKLA_IMG_LOCATION).getAbsolutePath());

        assertEquals(String.valueOf(31.23), String.valueOf(ooklaImageParser.parseSpeedTest(map)));
    }

}