package com.icarusrises.caseyellowanalysis.services.central;

import retrofit2.Call;
import retrofit2.http.*;

public interface CentralRequests {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("central/google-vision-key")
    Call<GoogleVisionKey> googleVisionKey();
}