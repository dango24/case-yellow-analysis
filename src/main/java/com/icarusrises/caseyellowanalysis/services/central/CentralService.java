package com.icarusrises.caseyellowanalysis.services.central;


/**
 * Created by dango on 6/3/17.
 */
public interface CentralService {
    PreSignedUrl generatePreSignedUrl(String fileKey);
}
