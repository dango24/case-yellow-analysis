package com.icarusrises.caseyellowanalysis.domain.nonflash;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AtntNonFlashAnalyzerTest {

    private static AtntNonFlashAnalyzer atntNonFlashAnalyzer;

    @BeforeClass
    public static void setUp() throws Exception {
        atntNonFlashAnalyzer = new AtntNonFlashAnalyzer(null);
    }

    @Test
    public void analyze() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("nonFlashResult", "Your Download speed is 124.9 Mega bits per second and your Upload speed is 6.07Mega bits per second");
        double actualResult = atntNonFlashAnalyzer.parseSpeedTest(data);

        assertEquals("124.9", String.valueOf(actualResult));
    }

}