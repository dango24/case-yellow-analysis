package com.icarusrises.caseyellowanalysis.domain.analyzer.text.model;

import com.icarusrises.caseyellowanalysis.services.googlevision.model.GoogleVisionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HTMLParserRequest {

    private String payload;
    private GoogleVisionRequest googleVisionRequest;
}
