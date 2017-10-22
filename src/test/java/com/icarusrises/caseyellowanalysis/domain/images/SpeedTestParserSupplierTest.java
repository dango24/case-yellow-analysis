package com.icarusrises.caseyellowanalysis.domain.images;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.exceptions.SpeedTestParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class SpeedTestParserSupplierTest {

    private static final String HOT_IDENTIFIER = "hot";
    private static final String BEZEQ_IDENTIFIER = "bezeq";
    private static final String OOKLA_IDENTIFIER = "ookla";

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Test(expected = SpeedTestParserException.class)
    public void getSpeedTestParserWithNull() throws Exception {
        speedTestParserSupplier.getSpeedTestParser(null);
    }

    @Test(expected = SpeedTestParserException.class)
    public void getSpeedTestParserWithDummyIdentifier() throws Exception {
        speedTestParserSupplier.getSpeedTestParser("oren_efes");
    }

    @Test
    public void getSpeedTestParserWithHot() throws Exception {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(HOT_IDENTIFIER);

        assertNotNull(speedTestParser);
        assertTrue(speedTestParser.getClass().equals(HotImageParser.class));
    }

    @Test
    public void getSpeedTestParserWithOokla() throws Exception {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(OOKLA_IDENTIFIER);

        assertNotNull(speedTestParser);
        assertTrue(speedTestParser.getClass().equals(OoklaImageParser.class));
    }

    @Test
    public void getSpeedTestParserWithBezeq() throws Exception {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser(BEZEQ_IDENTIFIER);

        assertNotNull(speedTestParser);
        assertTrue(speedTestParser.getClass().equals(BezeqImageParser.class));
    }

}
