package com.icarusrises.caseyellowanalysis.services.central;


import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.SpeedTestNonFlashMetaData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.WordIdentifier;

import java.util.Set;

/**
 * Created by dango on 6/3/17.
 */
public interface CentralService {
    PreSignedUrl generatePreSignedUrl(String fileKey);
    Set<WordIdentifier> getTextIdentifiers(String identifier, boolean startTest);
    SpeedTestNonFlashMetaData getSpeedTestNonFlashMetaData(String identifier);
}
