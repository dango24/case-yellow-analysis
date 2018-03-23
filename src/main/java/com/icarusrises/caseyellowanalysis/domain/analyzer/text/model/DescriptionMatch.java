package com.icarusrises.caseyellowanalysis.domain.analyzer.text.model;

import com.icarusrises.caseyellowanalysis.domain.analyzer.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DescriptionMatch {

    private boolean isMatchedDescription;
    private DescriptionLocation descriptionLocation;

    public DescriptionMatch() {
        this(true);
    }

    private DescriptionMatch(boolean found) {
        this(found, DescriptionLocation.defaultDescriptionLocation());
    }

    public DescriptionMatch(String description, Point center) {
        this(true, new DescriptionLocation(description, center));
    }

    public static DescriptionMatch notFound() {
        return new DescriptionMatch(false);
    }
}
