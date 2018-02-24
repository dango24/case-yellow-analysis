package com.icarusrises.caseyellowanalysis.domain.inception.model;

public class ImageClassification {

    private String label;
    private double confidence;

    public ImageClassification() {
    }

    public ImageClassification(String label, double confidence) {
        this.label = label;
        this.confidence = confidence;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "{" +
                "label='" + label + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
