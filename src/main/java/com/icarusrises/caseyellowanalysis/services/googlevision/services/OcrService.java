package com.icarusrises.caseyellowanalysis.services.googlevision.services;


import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;

import java.io.IOException;

public interface OcrService {
    OcrResponse parseImage(String imgPath) throws IOException, RequestFailureException;
}
