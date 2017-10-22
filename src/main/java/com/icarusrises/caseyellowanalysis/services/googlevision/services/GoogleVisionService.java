package com.icarusrises.caseyellowanalysis.services.googlevision.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RequestHandler;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class GoogleVisionService implements OcrService {

    @Value("${google_vision_url}")
    private String googleVisionUrl;

    @Value("${google_vision_key}")
    private String googleVisionKey;

    private RequestHandler requestHandler;
    private GoogleVisionRetrofitRequests googleVisionRetrofitRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(googleVisionUrl)
                                           .build();

        googleVisionRetrofitRequests = retrofit.create(GoogleVisionRetrofitRequests.class);
    }

    @Override
    public OcrResponse parseImage(String imgPath) throws IOException, RequestFailureException {

        GoogleVisionRequest googleVisionRequest = new GoogleVisionRequest(imgPath);
        JsonNode response = requestHandler.execute(googleVisionRetrofitRequests.ocrRequest(googleVisionKey, googleVisionRequest));
        OcrResponse ocrData = parseResponse(response);

        return ocrData;
    }

    private OcrResponse parseResponse(JsonNode response) throws IOException {
        JsonNode textAnnotations = response.at("/responses/0").get("textAnnotations");
        String wordsData = "{ \"textAnnotations\" : " + textAnnotations.toString() + "}";

        return new ObjectMapper().readValue(wordsData, OcrResponse.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
}
