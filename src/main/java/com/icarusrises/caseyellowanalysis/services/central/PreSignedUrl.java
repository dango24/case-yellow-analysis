package com.icarusrises.caseyellowanalysis.services.central;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreSignedUrl {

    private URL preSignedUrl;
    private String key;
}
