package com.icarusrises.caseyellowanalysis.services.central;

import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.SpeedTestNonFlashMetaData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.WordIdentifier;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RequestHandler;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;
import java.util.Set;

@Service
public class CentralServiceImp implements CentralService {

    @Value("${central_url}")
    private String centralUrl;

    private RequestHandler requestHandler;
    private CentralRequests centralRequests;

    @PostConstruct
    public void init() {
        Retrofit retrofit = RetrofitBuilder.Retrofit(centralUrl)
                                           .build();

        centralRequests = retrofit.create(CentralRequests.class);
    }

    @Autowired
    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    public PreSignedUrl generatePreSignedUrl(String fileKey) {
        return requestHandler.execute(centralRequests.generatePreSignedUrl(fileKey));
    }

    @Override
    public Set<WordIdentifier> getTextIdentifiers(String identifier, boolean startTest) {
        return requestHandler.execute(centralRequests.getTextIdentifiers(identifier, startTest));
    }

    @Override
    public SpeedTestNonFlashMetaData getSpeedTestNonFlashMetaData(String identifier) {
        return requestHandler.execute(centralRequests.getSpeedTestNonFlashMetaData(identifier));
    }
}
