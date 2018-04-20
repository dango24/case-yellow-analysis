package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalyzedImage {

    private double result;
    private String message;
    private String path;
    private boolean analyzed;
    private String md5;

    public AnalyzedImage() {
        this(-1);
    }

    public AnalyzedImage(boolean analyzed) {
        this(analyzed, -1);
    }

    public AnalyzedImage(double result) {
        this(result, "SUCCESS", true);
    }

    public AnalyzedImage(boolean analyzed, double result) {
        this(result, "SUCCESS", analyzed);
    }

    public AnalyzedImage(double result, String message, boolean analyzed) {
        this.result = result;
        this.message = message;
        this.analyzed = analyzed;
    }

    public static AnalyzedImage AnalyzedImageFailure(String message) {
        return new AnalyzedImage(0, message, false);
    }
}
