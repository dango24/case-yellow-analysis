package com.icarusrises.caseyellowanalysis.services.central;

import com.icarusrises.caseyellowanalysis.services.infrastrucre.RequestHandler;
import com.icarusrises.caseyellowanalysis.services.infrastrucre.RetrofitBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import javax.annotation.PostConstruct;

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
}
