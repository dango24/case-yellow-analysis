package com.icarusrises.caseyellowanalysis.domain.analyzer.image.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundingPoly {

    private List<Point> vertices;
}
