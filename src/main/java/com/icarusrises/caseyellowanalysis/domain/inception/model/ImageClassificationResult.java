package com.icarusrises.caseyellowanalysis.domain.inception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageClassificationResult {

    private String message;
    private ImageClassificationStatus status;

    public ImageClassificationResult(ImageClassificationStatus status) {
        this(null, status);
    }
}
