package com.icarusrises.caseyellowanalysis.services.googlevision.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icarusrises.caseyellowanalysis.configuration.ConfigurationManager;
import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import com.icarusrises.caseyellowanalysis.services.googlevision.model.OcrResponse;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RequestHandler;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@Profile("prod")
public class GoogleVisionService implements OcrService {

    @Value("${google_vision_url}")
    private String googleVisionUrl;

    private RequestHandler requestHandler;
    private ConfigurationManager configurationManager;
    private GoogleVisionRetrofitRequests googleVisionRetrofitRequests;

    @Autowired
    public GoogleVisionService(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(googleVisionUrl)
                                           .build();

        googleVisionRetrofitRequests = retrofit.create(GoogleVisionRetrofitRequests.class);
    }

    @Override
    public OcrResponse parseImage(GoogleVisionRequest googleVisionRequest) throws IOException, RequestFailureException {
        String googleVisionKey = configurationManager.googleVisionKey();
        JsonNode response = requestHandler.execute(googleVisionRetrofitRequests.ocrRequest(googleVisionKey, googleVisionRequest));
        return parseResponse(response);
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
