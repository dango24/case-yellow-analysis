package com.icarusrises.caseyellowanalysis.domain.images.model;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;

public class WordResult {

    private String description;
    private Point centralizedLocation;

    public WordResult() {
    }

    public WordResult(String description, Point centralizedLocation) {
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
        return "WordResult{" +
                "description='" + description + '\'' +
                ", centralizedLocation=" + centralizedLocation +
                '}';
    }
}
