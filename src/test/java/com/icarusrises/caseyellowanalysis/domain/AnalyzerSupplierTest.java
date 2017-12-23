package com.icarusrises.caseyellowanalysis.domain;

import com.icarusrises.caseyellowanalysis.CaseYellowAnalysisApplication;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParser;
import com.icarusrises.caseyellowanalysis.domain.analyzer.services.SpeedTestParserSupplier;
import com.icarusrises.caseyellowanalysis.domain.images.services.BezeqImageParser;
import com.icarusrises.caseyellowanalysis.domain.images.services.HotImageParser;
import com.icarusrises.caseyellowanalysis.domain.nonflash.AtntNonFlashAnalyzer;
import com.icarusrises.caseyellowanalysis.domain.nonflash.FastNonFlashAnalyzer;
import com.icarusrises.caseyellowanalysis.domain.nonflash.SpeedofNonFlashAnalyzer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseYellowAnalysisApplication.class)
@ActiveProfiles("dev")
public class AnalyzerSupplierTest {

    private SpeedTestParserSupplier speedTestParserSupplier;

    @Autowired
    public void setSpeedTestParserSupplier(SpeedTestParserSupplier speedTestParserSupplier) {
        this.speedTestParserSupplier = speedTestParserSupplier;
    }

    @Test
    public void getFastNonFlashAnalyzer() throws Exception {
        SpeedTestParser nonFlashAnalyzer = speedTestParserSupplier.getSpeedTestParser("fast");
        assertEquals(nonFlashAnalyzer.getClass(), (FastNonFlashAnalyzer.class));
    }

    @Test
    public void getAtntNonFlashAnalyzer() throws Exception {
        SpeedTestParser nonFlashAnalyzer = speedTestParserSupplier.getSpeedTestParser("atnt");
        assertEquals(nonFlashAnalyzer.getClass(), (AtntNonFlashAnalyzer.class));
    }

    @Test
    public void getSpeedOfNonFlashAnalyzer() throws Exception {
        SpeedTestParser nonFlashAnalyzer = speedTestParserSupplier.getSpeedTestParser("speedof");
        assertEquals(nonFlashAnalyzer.getClass(), (SpeedofNonFlashAnalyzer.class));
    }

    @Test
    public void getHotAnalyzer() throws Exception {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser("hot");
        assertEquals(speedTestParser.getClass(), (HotImageParser.class));
    }

    @Test
    public void getBezeqAnalyzer() throws Exception {
        SpeedTestParser speedTestParser = speedTestParserSupplier.getSpeedTestParser("bezeq");
        assertEquals(speedTestParser.getClass(), (BezeqImageParser.class));
    }

}