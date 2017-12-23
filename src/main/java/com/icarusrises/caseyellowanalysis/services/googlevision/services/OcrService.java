package com.icarusrises.caseyellowanalysis.services.googlevision.services;


import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.IOException;

public interface OcrService {
    OcrResponse parseImage(String imgPath) throws IOException, RequestFailureException;
    OcrResponse parseImage(GoogleVisionRequest googleVisionRequest) throws IOException, RequestFailureException;
}
