package com.icarusrises.caseyellowanalysis.services.infrastrucre;

import com.icarusrises.caseyellowanalysis.exceptions.RequestFailureException;
import retrofit2.Call;

public interface RequestHandler {

    void cancelRequest();
    <T extends Object> T execute(Call<T> request) throws RequestFailureException;
}
