package com.icarusrises.caseyellowanalysis.domain.analyzer.model;

public class PinnedWord {

    private String description;
    private Point centralizedLocation;

    public PinnedWord() {
    }

    public PinnedWord(String description, Point centralizedLocation) {
        this.description = description;
        this.centralizedLocation = centralizedLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getCentralizedLocation() {
        return centralizedLocation;
    }

    public void setCentralizedLocation(Point centralizedLocation) {
        this.centralizedLocation = centralizedLocation;
    }

    @Override
    public String toString() {
        return "PinnedWord{" +
                "description='" + description + '\'' +
                ", centralizedLocation=" + centralizedLocation +
                '}';
    }
}
