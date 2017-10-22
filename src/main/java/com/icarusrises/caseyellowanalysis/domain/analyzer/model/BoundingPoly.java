package com.icarusrises.caseyellowanalysis.domain.analyzer.model;

import java.util.List;

public class BoundingPoly {

    private List<Point> vertices;

    public BoundingPoly() {
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public void setVertices(List<Point> vertices) {
        this.vertices = vertices;
    }
}
