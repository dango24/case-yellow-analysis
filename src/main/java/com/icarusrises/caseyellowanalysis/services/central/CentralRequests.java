package com.icarusrises.caseyellowanalysis.services.central;

import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.SpeedTestNonFlashMetaData;
import com.icarusrises.caseyellowanalysis.domain.analyzer.text.model.WordIdentifier;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Set;

public interface CentralRequests {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/pre-signed-url")
    Call<PreSignedUrl> generatePreSignedUrl(@Query("file_key") String fileKey);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/text-identifiers")
    Call<Set<WordIdentifier>> getTextIdentifiers(@Query("identifier")String identifier, @Query("startTest")boolean startTest);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/speedtest-non-flash-meta-data")
    Call<SpeedTestNonFlashMetaData> getSpeedTestNonFlashMetaData(@Query("identifier")String identifier);
}