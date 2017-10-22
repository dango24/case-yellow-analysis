package com.icarusrises.caseyellowanalysis.services.googlevision.model;


import com.icarusrises.caseyellowanalysis.domain.analyzer.model.WordData;

import java.util.List;

public class OcrResponse {

    private List<WordData> textAnnotations;

    public OcrResponse() {
    }

    public List<WordData> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<WordData> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }
}

