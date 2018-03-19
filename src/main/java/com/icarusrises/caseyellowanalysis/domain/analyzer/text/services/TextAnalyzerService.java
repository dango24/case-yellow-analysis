package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(String identifier, boolean startTest, GoogleVisionRequest visionRequest) throws AnalyzeException;
    String retrieveResultFromHtml(String identifier, String htmlPayload) throws AnalyzeException;
}
