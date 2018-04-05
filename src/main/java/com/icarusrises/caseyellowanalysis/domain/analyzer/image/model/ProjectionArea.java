package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

public class ProjectionArea {

    private WordData upperLeft;
    private WordData upperRight;
    private WordData lowerLeft;
    private WordData lowerRight;

    public ProjectionArea() {
    }

    public ProjectionArea(WordData upperLeft, WordData upperRight, WordData lowerLeft, WordData lowerRight) {
        this.upperLeft = upperLeft;
        this.upperRight = upperRight;
        this.lowerLeft = lowerLeft;
        this.lowerRight = lowerRight;
    }

    public WordData getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(WordData upperLeft) {
        this.upperLeft = upperLeft;
    }

    public WordData getUpperRight() {
        return upperRight;
    }

    public void setUpperRight(WordData upperRight) {
        this.upperRight = upperRight;
    }

    public WordData getLowerLeft() {
        return lowerLeft;
    }

    public void setLowerLeft(WordData lowerLeft) {
        this.lowerLeft = lowerLeft;
    }

    public WordData getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(WordData lowerRight) {
        this.lowerRight = lowerRight;
    }
}
