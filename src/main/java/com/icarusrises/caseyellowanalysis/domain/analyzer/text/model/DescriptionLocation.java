package com.icarusrises.caseyellowanalysis.domain.analyzer.text.model;

import com.icarusrises.caseyellowanalysis.domain.analyzer.image.model.Point;
import lombok.Data;

@Data
public class DescriptionLocation {

    private Point center;
    private String description;

    public DescriptionLocation() {
        this("", new Point(0,0));
    }

    public DescriptionLocation(String word, Point center) {
        this.description = word;
        this.center = center;
    }

    public static DescriptionLocation defaultDescriptionLocation() {
        return new DescriptionLocation();
    }
}
