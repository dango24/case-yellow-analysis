package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

public class AnalyzedImage {

    private double result;
    private String message;
    private boolean analyzed;

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

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void setAnalyzed(boolean analyzed) {
        this.analyzed = analyzed;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public static AnalyzedImage AnalyzedImageFailure(String message) {
        return new AnalyzedImage(-1, message, false);
    }
}
