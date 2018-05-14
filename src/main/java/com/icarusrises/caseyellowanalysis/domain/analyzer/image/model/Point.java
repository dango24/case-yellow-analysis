package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {

    private int x;
    private int y;

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
