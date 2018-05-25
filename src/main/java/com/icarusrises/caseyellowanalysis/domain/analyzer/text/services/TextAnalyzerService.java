package com.icarusrises.caseyellowanalysis.domain.analyzer.text.services;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.DescriptionMatch;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.HTMLParserRequest;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.HTMLParserResult;
import com.icarusrises.caseyellowanalysis.exceptions.AnalyzeException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.VisionRequest;

public interface TextAnalyzerService {
    DescriptionMatch isDescriptionExist(String user, String identifier, boolean startTest, GoogleVisionRequest visionRequest) throws AnalyzeException;
    HTMLParserResult retrieveResultFromHtml(String identifier, HTMLParserRequest htmlParserRequest) throws AnalyzeException;
    void startButtonSuccessfullyFound(String user, String identifier, Point imageCenterPoint, VisionRequest visionRequest);
    void startButtonFailed(String user, String identifier, Point imageCenterPoint, VisionRequest visionRequest);
}
